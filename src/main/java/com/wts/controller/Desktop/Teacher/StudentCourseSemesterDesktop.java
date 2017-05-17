package com.wts.controller.Desktop.Teacher;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.wts.entity.model.Studentcoursesemester;
import com.wts.entity.model.Teacher;
import com.wts.interceptor.OverdueCheck;
import com.wts.interceptor.PageCheck;
import com.wts.interceptor.PermissionCheck;
import com.wts.util.ExportUtil;
import com.wts.validator.Query;
import com.wts.validator.studentCourseSemester.StudentCourseSemester_Exist;
import com.wts.validator.studentCourseSemester.StudentCourseSemester_Save;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.wts.util.Util.PermissionString;


public class StudentCourseSemesterDesktop extends Controller {
  private static Logger logger = Logger.getLogger(StudentCourseSemesterDesktop.class);

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
        render("/static/html/desktop/Teacher/Desktop_Teacher_StudentCourseSemester.html");
      }
    } else {
      String teacherId = ((Teacher) getSessionAttr("Teacher")).getId().toString();
      setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(), teacherId), 60 * 6 * 10);
      render("/static/html/desktop/Teacher/Desktop_Teacher_StudentCourseSemester.html");
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
                    "student.id AS sid," +
                    "semester.id AS xid," +
                    "course.name AS cname," +
                    "student.name AS sname," +
                    "semester.name AS iname," +
                    "student.number AS snumber," +
                    "student.code AS scode",
            "FROM (((studentcoursesemester " +
                    "LEFT JOIN student ON studentcoursesemester.student_id = student.id) " +
                    "LEFT JOIN course ON studentcoursesemester.course_id = course.id) " +
                    "LEFT JOIN semester ON studentcoursesemester.semester_id = semester.id) " +
                    "WHERE student.del = 0 " +
                    "AND course.del = 0 " +
                    "AND semester.del = 0 " +
                    "AND (course.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR student.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR semester.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR student.number LIKE '%" + getPara("keyword") + "%' " +
                    "OR student.code LIKE '%" + getPara("keyword") + "%')").getList());
  }

  /**
   * 计数
   */
  @Before({OverdueCheck.class, PermissionCheck.class})
  public void Total() {
    Long count = Db.queryLong("SELECT COUNT(*) FROM (((studentcoursesemester " +
            "LEFT JOIN student ON studentcoursesemester.student_id = student.id) " +
            "LEFT JOIN course ON studentcoursesemester.course_id = course.id) " +
            "LEFT JOIN semester ON studentcoursesemester.semester_id = semester.id) " +
            "WHERE student.del = 0 " +
            "AND course.del = 0 " +
            "AND semester.del = 0 " +
            "AND (course.name LIKE '%" + getPara("keyword") + "%' " +
            "OR student.name LIKE '%" + getPara("keyword") + "%' " +
            "OR semester.name LIKE '%" + getPara("keyword") + "%' " +
            "OR student.number LIKE '%" + getPara("keyword") + "%' " +
            "OR student.code LIKE '%" + getPara("keyword") + "%')");
    renderText(count.toString());
  }


  /**
   * 删除
   */
  @Before({OverdueCheck.class, PermissionCheck.class, StudentCourseSemester_Exist.class})
  public void Delete() {
    Studentcoursesemester.dao.deleteById(getPara("cid"), getPara("xid"), getPara("sid"));
    logger.warn("function:" + this.getClass().getSimpleName() + "/Delete;" +
            "teacher_id:" + ((Teacher) getSessionAttr("Teacher")).getId().toString() + ";" +
            "semester_id:" + getPara("xid") + ";" +
            "course_id:" + getPara("cid") + ";" +
            "student_id:" + getPara("sid") + ";");
    renderText("OK");
  }

  /**
   * 保存
   */
  @Before({OverdueCheck.class, PermissionCheck.class, StudentCourseSemester_Save.class})
  public void Save() {
    Studentcoursesemester object = new Studentcoursesemester();
    object.set("student_id", getPara("sid"))
            .set("course_id", getPara("cid"))
            .set("semester_id", getPara("xid"))
            .save();
    logger.warn("function:" + this.getClass().getSimpleName() + "/Save;" +
            "teacher_id:" + ((Teacher) getSessionAttr("Teacher")).getId().toString() + ";" +
            "semester_id:" + getPara("xid") + ";" +
            "course_id:" + getPara("cid") + ";" +
            "student_id:" + getPara("sid") + ";");
    renderText("OK");
  }


  /**
   * 导出
   */
  @Before({OverdueCheck.class, PermissionCheck.class})
  public void Download() throws IOException {
    String[] title = {"课程名称", "学生姓名", "学期名称", "家长电话", "学生身份证号码", "学生学籍号码"};
    String fileName = "StudentCourseSemester";
    String SQL = "SELECT " +
            "course.name AS 课程名称," +
            "student.name AS 学生姓名," +
            "semester.name AS 学期名称," +
            "student.number AS 学生身份证号码," +
            "student.code AS 学生学籍号码" +
            "FROM (((studentcoursesemester " +
            "LEFT JOIN student ON studentcoursesemester.student_id = student.id) " +
            "LEFT JOIN course ON studentcoursesemester.course_id = course.id) " +
            "LEFT JOIN semester ON studentcoursesemester.semester_id = semester.id) " +
            "WHERE student.del = 0 " +
            "AND course.del = 0 " +
            "AND semester.del = 0 " +
            "AND (course.name LIKE '%" + getPara("keyword") + "%' " +
            "OR student.name LIKE '%" + getPara("keyword") + "%' " +
            "OR semester.name LIKE '%" + getPara("keyword") + "%' " +
            "OR student.number LIKE '%" + getPara("keyword") + "%' " +
            "OR student.code LIKE '%" + getPara("keyword") + "%')";
    logger.warn("function:" + this.getClass().getSimpleName() + "/Download;" +
            "teacher_id:" + ((Teacher) getSessionAttr("Teacher")).getId().toString() + ";" +
            "file_name:" + fileName + ";" +
            "sql:" + SQL + ";");
    ExportUtil.export(title, fileName, SQL, getResponse());
  }

}
