package com.wts.controller.Desktop.Teacher;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.wts.entity.model.Courseroomteacher;
import com.wts.entity.model.Teacher;
import com.wts.interceptor.OverdueCheck;
import com.wts.interceptor.PageCheck;
import com.wts.interceptor.PermissionCheck;
import com.wts.util.ExportUtil;
import com.wts.validator.Query;
import com.wts.validator.courseRoomTeacher.CourseRoomTeacher_Exist;
import com.wts.validator.courseRoomTeacher.CourseRoomTeacher_Save;

import java.io.IOException;

import static com.wts.util.Util.PermissionString;


public class CourseRoomTeacherDesktop extends Controller {
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
        render("/static/html/desktop/Teacher/Desktop_Teacher_CourseRoomTeacher.html");
      }
    } else {
      String teacherId = ((Teacher) getSessionAttr("Teacher")).getId().toString();
      setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(), teacherId), 60 * 6 * 10);
      render("/static/html/desktop/Teacher/Desktop_Teacher_CourseRoomTeacher.html");
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
                    "Teacher.id AS tid," +
                    "semester.id AS sid," +
                    "course.name AS cname," +
                    "room.name AS rname," +
                    "Teacher.name AS tname," +
                    "semester.name AS sname,",
            "FROM ((((courseroomteacher " +
                    "LEFT JOIN course ON courseroomteacher.course_id = course.id) " +
                    "LEFT JOIN room ON courseroomteacher.room = room.id) " +
                    "LEFT JOIN Teacher ON courseroomteacher.teacher_id = Teacher.id) " +
                    "LEFT JOIN semester ON courseroomteacher.semester_id = semester.id) " +
                    "WHERE course.del = 0 " +
                    "AND room.del = 0 " +
                    "AND Teacher.del = 0 " +
                    "AND semester.del = 0 " +
                    "AND (course.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR room.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR Teacher.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR semester.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR Teacher.mobile LIKE '%" + getPara("keyword") + "%' " +
                    "OR Teacher.userId LIKE '%" + getPara("keyword") + "%')").getList());
  }

  /**
   * 计数
   */
  @Before({OverdueCheck.class, PermissionCheck.class})
  public void Total() {
    Long count = Db.queryLong("FROM ((((courseroomteacher " +
            "LEFT JOIN course ON courseroomteacher.course_id = course.id) " +
                    "LEFT JOIN room ON courseroomteacher.room = room.id) " +
                    "LEFT JOIN Teacher ON courseroomteacher.teacher_id = Teacher.id) " +
                    "LEFT JOIN semester ON courseroomteacher.semester_id = semester.id) " +
                    "WHERE course.del = 0 " +
                    "AND room.del = 0 " +
                    "AND Teacher.del = 0 " +
                    "AND semester.del = 0 " +
                    "AND (course.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR room.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR Teacher.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR semester.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR Teacher.mobile LIKE '%" + getPara("keyword") + "%' " +
                    "OR Teacher.userId LIKE '%" + getPara("keyword") + "%')");
    renderText(count.toString());
  }


  /**
   * 删除
   */
  @Before({OverdueCheck.class, PermissionCheck.class, CourseRoomTeacher_Exist.class})
  public void Delete() {
    Courseroomteacher.dao.deleteById(getPara("cid"), getPara("rid"), getPara("sid"), getPara("tid"));
    renderText("OK");
  }

  /**
   * 保存
   */
  @Before({OverdueCheck.class, PermissionCheck.class, CourseRoomTeacher_Save.class})
  public void Save() {
    Courseroomteacher object = new Courseroomteacher();
    object.set("course_id", getPara("cid"))
            .set("room_id", getPara("rid"))
            .set("semester_id", getPara("sid"))
            .set("teacher_id", getPara("tid"))
            .save();
    renderText("OK");
  }


  /**
   * 导出
   */
  @Before({OverdueCheck.class, PermissionCheck.class})
  public void Download() throws IOException {
    String[] title = {"课程名称", "班级名称", "教师姓名", "学期名称"};
    String fileName = "studentparentidentity";
    String SQL = "SELECT " +
            "course.name AS cname," +
            "room.name AS rname," +
            "Teacher.name AS tname," +
            "semester.name AS sname," +
            "FROM ((((courseroomteacher " +
            "LEFT JOIN course ON courseroomteacher.course_id = course.id) " +
            "LEFT JOIN room ON courseroomteacher.room = room.id) " +
            "LEFT JOIN Teacher ON courseroomteacher.teacher_id = Teacher.id) " +
            "LEFT JOIN semester ON courseroomteacher.semester_id = semester.id) " +
            "WHERE course.del = 0 " +
            "AND room.del = 0 " +
            "AND Teacher.del = 0 " +
            "AND semester.del = 0 " +
            "AND (course.name LIKE '%" + getPara("keyword") + "%' " +
            "OR room.name LIKE '%" + getPara("keyword") + "%' " +
            "OR Teacher.name LIKE '%" + getPara("keyword") + "%' " +
            "OR semester.name LIKE '%" + getPara("keyword") + "%' " +
            "OR Teacher.mobile LIKE '%" + getPara("keyword") + "%' " +
            "OR Teacher.userId LIKE '%" + getPara("keyword") + "%')";
    ExportUtil.export(title, fileName, SQL, getResponse());
  }

}
