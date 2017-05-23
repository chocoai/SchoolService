package com.wts.controller.Desktop.Teacher;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.wts.entity.model.Semester;
import com.wts.entity.model.Student;
import com.wts.entity.model.Teacher;
import com.wts.interceptor.OverdueCheck;
import com.wts.interceptor.PageCheck;
import com.wts.interceptor.PermissionCheck;
import com.wts.util.ExportUtil;
import com.wts.validator.Query;
import com.wts.validator.Student.Student_Exist;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.wts.util.Util.PermissionString;


public class TeacherMessageSendDesktop extends Controller {
  private static Logger logger = Logger.getLogger(TeacherMessageSendDesktop.class);

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
        setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(),teacher.getId().toString()), 60 * 6 * 10);
        render("/static/html/desktop/Teacher/Desktop_Teacher_TeacherMessageSend.html");
      }
    } else {
      String teacherId = ((Teacher) getSessionAttr("Teacher")).getId().toString();
      setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(),teacherId), 60 * 6 * 10);
      render("/static/html/desktop/Teacher/Desktop_Teacher_TeacherMessageSend.html");
    }
  }

  /**
   * 权限
   * */
  public void Permission() {
    String teacherId = ((Teacher) getSessionAttr("Teacher")).getId().toString();
    setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(),teacherId), 60 * 6 * 10);
    setCookie("name",((Teacher) getSessionAttr("Teacher")).getName(),60 * 6 * 10);
  }

  /**
   * 查找
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Query.class})
  public void Query() {
    int sid = Semester.dao.findFirst("SELECT * FROM semester WHERE state = 1").getId();
    int tid = ((Teacher) getSessionAttr("Teacher")).getId();
    renderJson(Db.paginate(
            getParaToInt("pageCurrent"),
            getParaToInt("pageSize"),
            "SELECT student.id," +
                    "student.`name`," +
                    "student.number," +
                    "student.`code`," +
                    "student.sex," +
                    "room.`name` AS rname",
                    "FROM ((roomstudent" +
                    " LEFT JOIN student ON student.id = roomstudent.student_id)" +
                    " LEFT JOIN room ON room.id = roomstudent.room_id)" +
                    " WHERE student_id IN (SELECT DISTINCT student_id FROM studentcoursesemester WHERE studentcoursesemester.course_id IN (SELECT DISTINCT course_id FROM courseroomteachersemester WHERE semester_id = " + sid + " AND teacher_id = " + tid + "))" +
                    " AND room_id IN ( SELECT DISTINCT room_id FROM courseroomteachersemester WHERE semester_id = " + sid + " AND teacher_id = " + tid + ") " +
                    " AND student.del = 0 " +
                    " AND student.state = 1 " +
                    " AND room.del = 0 " +
                    " AND room.state = 1 " +
                    " AND (student.`name` LIKE '%" + getPara("keyword") + "%' " +
                    " OR student.`number` LIKE '%" + getPara("keyword") + "%' " +
                    " OR student.`code` LIKE '%" + getPara("keyword") + "%' " +
                    " OR room.`name` LIKE '%" + getPara("keyword") + "%') ORDER BY student.id DESC").getList());
  }

  /**
   * 计数
   */
  @Before({OverdueCheck.class, PermissionCheck.class})
  public void Total() {
    String sid = Semester.dao.findFirst("SELECT * FROM semester WHERE state = 1").getId().toString();
    String tid = ((Teacher) getSessionAttr("Teacher")).getId().toString();

    Long count = Db.queryLong("SELECT COUNT(*) FROM ((roomstudent" +
            " LEFT JOIN student ON student.id = roomstudent.student_id)" +
                    " LEFT JOIN room ON room.id = roomstudent.room_id)" +
                    " WHERE student_id IN (SELECT DISTINCT student_id FROM studentcoursesemester WHERE studentcoursesemester.course_id IN (SELECT DISTINCT course_id FROM courseroomteachersemester WHERE semester_id = " + sid + " AND teacher_id = " + tid + " ))" +
                    " AND room_id IN ( SELECT DISTINCT room_id FROM courseroomteachersemester WHERE semester_id = " + sid + " AND teacher_id = " + tid + ") " +
                    " AND student.del = 0 " +
                    " AND student.state = 1 " +
                    " AND room.del = 0 " +
                    " AND room.state = 1 " +
                    " AND (student.`name` LIKE '%" + getPara("keyword") + "%' " +
                    " OR student.`number` LIKE '%" + getPara("keyword") + "%' " +
                    " OR student.`code` LIKE '%" + getPara("keyword") + "%' " +
                    " OR room.`name` LIKE '%" + getPara("keyword") + "%')");
    renderText(count.toString());
  }

  /**
   * 读取
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Student_Exist.class})
  public void GetTeacher() {
    renderJson(Student.dao.find("SELECT * FROM teacher WHERE teacher.del=0 AND teacher.state = 1"));
  }


  /**
   * 导出
   */
  @Before({OverdueCheck.class, PermissionCheck.class})
  public void Download() throws IOException {
    int sid = Semester.dao.findFirst("SELECT * FROM semester WHERE state = 1").getId();
    String[] title={"序号","学生姓名","身份证号码","学籍号码","家庭地址","性别","班级名称"};
    String fileName = "MyStudent";
    String SQL = "SELECT student.id AS 序号," +
            "student.`name` AS 学生姓名," +
            "student.number AS 身份证号码," +
            "student.`code` AS 学籍号码," +
            "student.`address` AS 家庭地址," +
            "(case student.sex when 1 then '男' when 2 then '女' else '未知' end ) AS 性别, " +
            "room.`name` AS 班级名称 " +
            "FROM ((roomstudent" +
                    " LEFT JOIN student ON student.id = roomstudent.student_id)" +
                    " LEFT JOIN room ON room.id = roomstudent.room_id)" +
                    " WHERE student_id IN (SELECT DISTINCT student_id FROM studentcoursesemester WHERE studentcoursesemester.course_id IN (SELECT DISTINCT course_id FROM courseroomteachersemester WHERE semester_id = " + sid + " AND teacher_id = " + ((Teacher) getSessionAttr("Teacher")).getId() + "))" +
                    " AND room_id IN ( SELECT DISTINCT room_id FROM courseroomteachersemester WHERE semester_id = " + sid + " AND teacher_id = " + ((Teacher) getSessionAttr("Teacher")).getId() + ") " +
                    " AND student.del = 0 " +
                    " AND student.state = 1 " +
                    " AND room.del = 0 " +
                    " AND room.state = 1 " +
                    " AND (student.`name` LIKE '%" + getPara("keyword") + "%' " +
                    " OR student.`number` LIKE '%" + getPara("keyword") + "%' " +
                    " OR student.`code` LIKE '%" + getPara("keyword") + "%' " +
                    " OR room.`name` LIKE '%" + getPara("keyword") + "%') ORDER BY student.id DESC";
    logger.warn("function:" + this.getClass().getSimpleName() + "/Download;" +
            "teacher_id:" + ((Teacher) getSessionAttr("Teacher")).getId().toString() + ";" +
            "file_name:" + fileName + ";" +
            "sql:" + SQL + ";");
    ExportUtil.export(title,fileName,SQL,getResponse());
  }


}
