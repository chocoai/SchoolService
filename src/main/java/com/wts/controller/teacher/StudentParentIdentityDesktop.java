package com.wts.controller.teacher;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.wts.entity.model.Studentparentidentity;
import com.wts.entity.model.Teacher;
import com.wts.interceptor.OverdueCheck;
import com.wts.interceptor.PageCheck;
import com.wts.interceptor.PermissionCheck;
import com.wts.util.ExportUtil;
import com.wts.validator.Query;
import com.wts.validator.studentParentIdentity.StudentParentIdentity_Exist;
import com.wts.validator.studentParentIdentity.StudentParentIdentity_Save;

import java.io.IOException;

import static com.wts.util.Util.PermissionString;


public class StudentParentIdentityDesktop extends Controller {
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
        render("/static/html/desktop/teacher/Desktop_Teacher_StudentParentIdentity.html");
      }
    } else {
      String teacherId = ((Teacher) getSessionAttr("teacher")).getId().toString();
      setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(), teacherId), 60 * 6 * 10);
      render("/static/html/desktop/teacher/Desktop_Teacher_StudentParentIdentity.html");
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
                    "parent.id AS pid," +
                    "student.id AS sid," +
                    "identity.id AS iid," +
                    "parent.name AS pname," +
                    "student.name AS sname," +
                    "identity.name AS iname," +
                    "parent.mobile AS pmobile," +
                    "student.number AS snumber," +
                    "student.code AS scode",
            "FROM (((studentparentidentity " +
                    "LEFT JOIN parent ON studentparentidentity.parent_id = parent.id) " +
                    "LEFT JOIN student ON studentparentidentity.student_id = student.id) " +
                    "LEFT JOIN identity ON studentparentidentity.identity_id = identity.id) " +
                    "WHERE parent.del = 0 " +
                    "AND student.del = 0 " +
                    "AND (parent.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR student.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR identity.name LIKE '%" + getPara("keyword") + "%' " +
                    "OR parent.mobile LIKE '%" + getPara("keyword") + "%' " +
                    "OR student.number LIKE '%" + getPara("keyword") + "%' " +
                    "OR student.code LIKE '%" + getPara("keyword") + "%')").getList());
  }

  /**
   * 计数
   */
  @Before({OverdueCheck.class, PermissionCheck.class})
  public void Total() {
    Long count = Db.queryLong("SELECT COUNT(*) FROM (((studentparentidentity " +
            "LEFT JOIN parent ON studentparentidentity.parent_id = parent.id) " +
            "LEFT JOIN student ON studentparentidentity.student_id = student.id) " +
            "LEFT JOIN identity ON studentparentidentity.identity_id = identity.id) " +
            "WHERE parent.del = 0 " +
            "AND student.del = 0 " +
            "AND (parent.name LIKE '%" + getPara("keyword") + "%' " +
            "OR student.name LIKE '%" + getPara("keyword") + "%' " +
            "OR identity.name LIKE '%" + getPara("keyword") + "%' " +
            "OR parent.mobile LIKE '%" + getPara("keyword") + "%' " +
            "OR student.number LIKE '%" + getPara("keyword") + "%' " +
            "OR student.code LIKE '%" + getPara("keyword") + "%')");
    renderText(count.toString());
  }


  /**
   * 删除
   */
  @Before({OverdueCheck.class, PermissionCheck.class, StudentParentIdentity_Exist.class})
  public void Delete() {
    Studentparentidentity.dao.deleteById(getPara("iid"), getPara("pid"), getPara("sid"));
    renderText("OK");
  }

  /**
   * 保存
   */
  @Before({OverdueCheck.class, PermissionCheck.class, StudentParentIdentity_Save.class})
  public void Save() {
    if (Studentparentidentity.dao.findById(getPara("iid"), getPara("pid"), getPara("sid")) != null) {
      renderText("该身份信息已存在");
    } else {
      Studentparentidentity object = new Studentparentidentity();
      object.set("student_id", getPara("sid"))
              .set("parent_id", getPara("pid"))
              .set("identity_id", getPara("iid"))
              .save();
      renderText("OK");
    }
  }


  /**
   * 导出
   */
  @Before({OverdueCheck.class, PermissionCheck.class})
  public void Download() throws IOException {
    String[] title = {"家长姓名", "学生姓名", "身份", "家长电话", "学生身份证号码", "学生学籍号码"};
    String fileName = "studentparentidentity";
    String SQL = "SELECT parent.name AS pname," +
            "student.name AS sname," +
            "identity.name AS iname," +
            "parent.mobile AS pmobile," +
            "student.number AS snumber," +
            "student.code AS scode" +
            "FROM (((studentparentidentity" +
            "LEFT JOIN parent ON studentparentidentity.parent_id = parent.id)" +
            "LEFT JOIN student ON studentparentidentity.student_id = student.id)" +
            "LEFT JOIN identity ON studentparentidentity.identity_id = identity.id)" +
            "WHERE parent.del = 0 " +
            "AND student.del = 1 " +
            "AND (parent.name LIKE '%" + getPara("keyword") + "%' " +
            "OR student.name LIKE '%" + getPara("keyword") + "%' " +
            "OR identity.name LIKE '%" + getPara("keyword") + "%' " +
            "OR  parent.mobile LIKE '%" + getPara("keyword") + "%' " +
            "OR student.number LIKE '%" + getPara("keyword") + "%' " +
            "OR student.code LIKE '%" + getPara("keyword") + "%')";
    ExportUtil.export(title, fileName, SQL, getResponse());
  }

}
