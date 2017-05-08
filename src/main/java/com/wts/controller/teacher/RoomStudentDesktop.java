package com.wts.controller.teacher;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.wts.entity.model.Roomstudent;
import com.wts.entity.model.Teacher;
import com.wts.interceptor.OverdueCheck;
import com.wts.interceptor.PageCheck;
import com.wts.interceptor.PermissionCheck;
import com.wts.util.ExportUtil;
import com.wts.validator.Query;
import com.wts.validator.roomStudent.RoomStudent_Exist;
import com.wts.validator.roomStudent.RoomStudent_Save;

import java.io.IOException;

import static com.wts.util.Util.PermissionString;


public class RoomStudentDesktop extends Controller {
  /**
   * 页面
   */
  @Before({OverdueCheck.class, PageCheck.class})
  public void Page() {
    // 未登录
    if (getSessionAttr("teacher") == null) {
      // cookie过期
      if (getCookie("dit").equals("")) {
        redirect("/desktop");
      } else {
        Teacher teacher = Teacher.dao.findById(getCookie("dit"));
        setSessionAttr("teacher", teacher);
        setCookie("dit", teacher.getId().toString(), 60 * 60 * 3);
        setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(), teacher.getId().toString()), 60 * 6 * 10);
        render("/static/html/desktop/teacher/Desktop_Teacher_RoomStudent.html");
      }
    } else {
      String teacherId = ((Teacher) getSessionAttr("teacher")).getId().toString();
      setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(), teacherId), 60 * 6 * 10);
      render("/static/html/desktop/teacher/Desktop_Teacher_RoomStudent.html");
    }
  }

  /**
   * 权限
   */
  public void Permission() {
    String teacherId = ((Teacher) getSessionAttr("teacher")).getId().toString();
    setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(), teacherId), 60 * 6 * 10);
    setCookie("name", ((Teacher) getSessionAttr("teacher")).getName(), 60 * 6 * 10);
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
                    "room.id AS rid," +
                    "student.id AS sid," +
                    "room.name AS rname," +
                    "student.name AS sname," +
                    "student.number AS snumber," +
                    "student.code AS scode",
            "FROM ((roomstudent " +
                    "LEFT JOIN room ON roomstudent.room_id = room.id) " +
                    "LEFT JOIN student ON roomstudent.student_id = student.id) " +
                    "WHERE room.del = 0 " +
                    "AND student.del = 0 " +
                    "AND (room.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR student.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR student.number LIKE '%" + getPara("keyword") + "%' " +
                    "OR student.code LIKE '%" + getPara("keyword") + "%')").getList());
  }

  /**
   * 计数
   */
  @Before({OverdueCheck.class, PermissionCheck.class})
  public void Total() {
    Long count = Db.queryLong("SELECT COUNT(*) FROM ((roomstudent " +
            "LEFT JOIN room ON roomstudent.room_id = room.id) " +
            "LEFT JOIN student ON roomstudent.student_id = student.id) " +
            "WHERE room.del = 0 " +
            "AND student.del = 0 " +
            "AND (room.name LIKE '%" + getPara("keyword") + "%' " +
            "OR student.name LIKE '%" + getPara("keyword") + "%' " +
            "OR student.number LIKE '%" + getPara("keyword") + "%' " +
            "OR student.code LIKE '%" + getPara("keyword") + "%')");
    renderText(count.toString());
  }


  /**
   * 删除
   */
  @Before({OverdueCheck.class, PermissionCheck.class, RoomStudent_Exist.class})
  public void Delete() {
    Roomstudent.dao.deleteById(getPara("rid"),  getPara("sid"));
    renderText("OK");
  }

  /**
   * 保存
   */
  @Before({OverdueCheck.class, PermissionCheck.class, RoomStudent_Save.class})
  public void Save() {
    Roomstudent object = new Roomstudent();
    object.set("student_id", getPara("sid"))
            .set("room_id", getPara("rid"))
            .save();
    renderText("OK");
  }


  /**
   * 导出
   */
  @Before({OverdueCheck.class, PermissionCheck.class})
  public void Download() throws IOException {
    String[] title = {"班级名称", "学生姓名", "学生身份证号码", "学生学籍编号"};
    String fileName = "roomstudent";
    String SQL = "SELECT " +
            "room.name AS rname," +
            "student.name AS sname," +
            "student.number AS snumber," +
            "student.code AS scode" +
            "FROM ((roomstudent" +
            "LEFT JOIN room ON roomstudent.room_id = room.id)" +
            "LEFT JOIN student ON roomstudent.student_id = student.id)" +
            "WHERE room.del = 0 " +
            "AND student.del = 0 " +
            "AND (room.name LIKE '%" + getPara("keyword") + "%' " +
            "OR student.name LIKE '%" + getPara("keyword") + "%' " +
            "OR student.number LIKE '%" + getPara("keyword") + "%' " +
            "OR student.code LIKE '%" + getPara("keyword") + "%')";
    ExportUtil.export(title, fileName, SQL, getResponse());
  }

}
