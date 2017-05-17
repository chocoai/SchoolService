package com.wts.controller.Desktop.Teacher;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.wts.entity.model.Courseroomteachersemester;
import com.wts.entity.model.Teacher;
import com.wts.interceptor.OverdueCheck;
import com.wts.interceptor.PageCheck;
import com.wts.interceptor.PermissionCheck;
import com.wts.util.ExportUtil;
import com.wts.validator.Query;
import com.wts.validator.courseRoomTeacher.CourseRoomTeacher_Exist;
import com.wts.validator.courseRoomTeacher.CourseRoomTeacher_Save;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.wts.util.Util.PermissionString;


public class CourseRoomTeacherSemesterDesktop extends Controller {
  private static Logger logger = Logger.getLogger(CourseRoomTeacherSemesterDesktop.class);

  /**
   * 页面
   */
  @Before({OverdueCheck.class, PageCheck.class})
  public void Page() {
    // 未登录
    if (getSessionAttr("Teacher") == null) {
      // cookie过期
      if (getCookie("dit").equals("")) {
        redirect("/desktop");
      } else {
        Teacher teacher = Teacher.dao.findById(getCookie("dit"));
        setSessionAttr("Teacher", teacher);
        setCookie("dit", teacher.getId().toString(), 60 * 60 * 3);
        setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(), teacher.getId().toString()), 60 * 6 * 10);
        render("/static/html/desktop/Teacher/Desktop_Teacher_CourseRoomTeacherSemester.html");
      }
    } else {
      String teacherId = ((Teacher) getSessionAttr("Teacher")).getId().toString();
      setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(), teacherId), 60 * 6 * 10);
      render("/static/html/desktop/Teacher/Desktop_Teacher_CourseRoomTeacherSemester.html");
    }
  }

  /**
   * 权限
   */
  public void Permission() {
    String teacherId = ((Teacher) getSessionAttr("Teacher")).getId().toString();
    setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(), teacherId), 60 * 6 * 10);
    setCookie("name", ((Teacher) getSessionAttr("Teacher")).getName(), 60 * 6 * 10);
  }

  /**
   * 查找
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Query.class})
  public void Query() {
    renderJson(Db.paginate(
            getParaToInt("pageCurrent"),
            getParaToInt("pageSize"),
            "SELECT " +
                    "course.id AS cid," +
                    "room.id AS rid," +
                    "teacher.id AS tid," +
                    "semester.id AS sid," +
                    "course.name AS cname," +
                    "room.name AS rname," +
                    "teacher.name AS tname," +
                    "semester.name AS sname,",
            "FROM ((((courseroomteachersemester " +
                    "LEFT JOIN course ON courseroomteachersemester.course_id = course.id) " +
                    "LEFT JOIN room ON courseroomteachersemester.room_id = room.id) " +
                    "LEFT JOIN teacher ON courseroomteachersemester.teacher_id = teacher.id) " +
                    "LEFT JOIN semester ON courseroomteachersemester.semester_id = semester.id) " +
                    "WHERE course.del = 0 " +
                    "AND room.del = 0 " +
                    "AND teacher.del = 0 " +
                    "AND semester.del = 0 " +
                    "AND (course.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR room.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR teacher.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR semester.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR teacher.mobile LIKE '%" + getPara("keyword") + "%' " +
                    "OR teacher.userId LIKE '%" + getPara("keyword") + "%')").getList());
  }

  /**
   * 计数
   */
  @Before({OverdueCheck.class, PermissionCheck.class})
  public void Total() {
    Long count = Db.queryLong("SELECT COUNT(*) FROM ((((courseroomteachersemester " +
            "LEFT JOIN course ON courseroomteachersemester.course_id = course.id) " +
                    "LEFT JOIN room ON courseroomteachersemester.room_id = room.id) " +
                    "LEFT JOIN teacher ON courseroomteachersemester.teacher_id = teacher.id) " +
                    "LEFT JOIN semester ON courseroomteachersemester.semester_id = semester.id) " +
                    "WHERE course.del = 0 " +
                    "AND room.del = 0 " +
                    "AND teacher.del = 0 " +
                    "AND semester.del = 0 " +
                    "AND (course.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR room.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR teacher.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR semester.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR teacher.mobile LIKE '%" + getPara("keyword") + "%' " +
                    "OR teacher.userId LIKE '%" + getPara("keyword") + "%')");
    renderText(count.toString());
  }


  /**
   * 删除
   */
  @Before({OverdueCheck.class, PermissionCheck.class, CourseRoomTeacher_Exist.class})
  public void Delete() {
    Courseroomteachersemester.dao.deleteById(getPara("cid"), getPara("rid"), getPara("sid"), getPara("tid"));
    logger.warn("function:" + this.getClass().getSimpleName() + "/Delete;" +
            "teacher_id:" + ((Teacher) getSessionAttr("Teacher")).getId().toString() + ";" +
            "course_id:" + getPara("cid") + ";" +
            "room_id:" + getPara("rid") + ";" +
            "semester_id:" + getPara("sid") + ";" +
            "teacher_id:" + getPara("tid") + ";");
    renderText("OK");
  }

  /**
   * 保存
   */
  @Before({OverdueCheck.class, PermissionCheck.class, CourseRoomTeacher_Save.class})
  public void Save() {
    Courseroomteachersemester object = new Courseroomteachersemester();
    object.set("course_id", getPara("cid"))
            .set("room_id", getPara("rid"))
            .set("semester_id", getPara("sid"))
            .set("teacher_id", getPara("tid"))
            .save();
    logger.warn("function:" + this.getClass().getSimpleName() + "/Save;" +
            "teacher_id:" + ((Teacher) getSessionAttr("Teacher")).getId().toString() + ";" +
            "course_id:" + getPara("cid") + ";" +
            "room_id:" + getPara("rid") + ";" +
            "semester_id:" + getPara("sid") + ";" +
            "teacher_id:" + getPara("tid") + ";");
    renderText("OK");
  }

  /**
   * 导出
   */
  @Before({OverdueCheck.class, PermissionCheck.class})
  public void Download() throws IOException {
    String[] title = {"课程名称", "班级名称", "教师姓名", "学期名称"};
    String fileName = "CourseRoomTeacherSemester";
    String SQL = "SELECT " +
            "course.name AS 课程名称," +
            "room.name AS 班级名称," +
            "teacher.name AS 教师姓名," +
            "semester.name AS 学期名称," +
            "FROM ((((courseroomteachersemester " +
            "LEFT JOIN course ON courseroomteachersemester.course_id = course.id) " +
            "LEFT JOIN room ON courseroomteachersemester.room_id = room.id) " +
            "LEFT JOIN teacher ON courseroomteachersemester.teacher_id = teacher.id) " +
            "LEFT JOIN semester ON courseroomteachersemester.semester_id = semester.id) " +
            "WHERE course.del = 0 " +
            "AND room.del = 0 " +
            "AND teacher.del = 0 " +
            "AND semester.del = 0 " +
            "AND (course.name LIKE '%" + getPara("keyword") + "%' " +
            "OR room.name LIKE '%" + getPara("keyword") + "%' " +
            "OR teacher.name LIKE '%" + getPara("keyword") + "%' " +
            "OR semester.name LIKE '%" + getPara("keyword") + "%' " +
            "OR teacher.mobile LIKE '%" + getPara("keyword") + "%' " +
            "OR teacher.userId LIKE '%" + getPara("keyword") + "%')";
    logger.warn("function:" + this.getClass().getSimpleName() + "/Download;" +
            "teacher_id:" + ((Teacher) getSessionAttr("Teacher")).getId().toString() + ";" +
            "file_name:" + fileName + ";" +
            "sql:" + SQL + ";");
    ExportUtil.export(title, fileName, SQL, getResponse());
  }

}
