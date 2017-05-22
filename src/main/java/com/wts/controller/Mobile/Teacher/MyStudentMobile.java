package com.wts.controller.Mobile.Teacher;

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
import com.wts.validator.Total;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.wts.util.Util.PermissionString;


public class MyStudentMobile extends Controller {
  private static Logger logger = Logger.getLogger(MyStudentMobile.class);

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
        render("/static/html/mobile/Teacher/Mobile_Teacher_MyStudent.html");
      }
    } else {
      String teacherId = ((Teacher) getSessionAttr("Teacher")).getId().toString();
      setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(),teacherId), 60 * 6 * 10);
      render("/static/html/mobile/Teacher/Mobile_Teacher_MyStudent.html");
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
  @Before({OverdueCheck.class, PermissionCheck.class, Total.class})
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
    if (count % getParaToInt("pageSize") == 0) {
      renderText((count / getParaToInt("pageSize")) + "");
    } else {
      renderText((count / getParaToInt("pageSize") + 1) + "");
    }
  }

  /**
   * 读取学生
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Student_Exist.class})
  public void GetStudent() {
    renderJson(Student.dao.findById(getPara("id")));
  }

  /**
   * 读取亲属
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Student_Exist.class})
  public void GetParent() {
    renderJson(Db.find(
            "SELECT identity.`name` AS iname,parent.`name`,parent.mobile,parent.state" +
                    " FROM (((studentparentidentity" +
                    " LEFT JOIN parent ON studentparentidentity.parent_id = parent.id)" +
                    " LEFT JOIN identity ON studentparentidentity.identity_id = identity.id)" +
                    " LEFT JOIN student ON studentparentidentity.student_id = student.id)" +
                    " WHERE student_id = "+getPara("id") +
                    " AND parent.del = 0 AND student.del = 0 AND student.state = 1"));
  }

}
