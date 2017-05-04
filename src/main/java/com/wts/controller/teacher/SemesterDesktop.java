package com.wts.controller.teacher;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.wts.entity.model.Semester;
import com.wts.entity.model.Teacher;
import com.wts.interceptor.OverdueCheck;
import com.wts.interceptor.PageCheck;
import com.wts.interceptor.PermissionCheck;
import com.wts.util.ExportUtil;
import com.wts.validator.Query;
import com.wts.validator.semester.Semester_Edit;
import com.wts.validator.semester.Semester_Exist;
import com.wts.validator.semester.Semester_Save;

import java.io.IOException;
import java.util.Date;

import static com.wts.util.Util.PermissionString;


public class SemesterDesktop extends Controller {
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
        setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(),teacher.getId().toString()), 60 * 6 * 10);
        render("/static/html/desktop/teacher/Desktop_Teacher_Semester.html");
      }
    } else {
      String teacherId = ((Teacher) getSessionAttr("teacher")).getId().toString();
      setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(),teacherId), 60 * 6 * 10);
      render("/static/html/desktop/teacher/Desktop_Teacher_Semester.html");
    }
  }

  /**
   * 权限
   * */
  public void Permission() {
    String teacherId = ((Teacher) getSessionAttr("teacher")).getId().toString();
    setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(),teacherId), 60 * 6 * 10);
    setCookie("name",((Teacher) getSessionAttr("teacher")).getName(),60 * 6 * 10);
  }

  /**
   * 查找
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Query.class})
  public void Query() {
    renderJson(Db.paginate(
            getParaToInt("pageCurrent"),
            getParaToInt("pageSize"),
            "SELECT *",
            "FROM semester WHERE del = 0 AND name LIKE '%" + getPara("keyword") + "%' ORDER BY id DESC").getList());
  }

  /**
   * 计数
   */
  @Before({OverdueCheck.class, PermissionCheck.class})
  public void Total() {
    Long count = Db.queryLong("SELECT COUNT(*) FROM semester WHERE del = 0 AND name LIKE '%" + getPara("keyword") + "%'");
    renderText(count.toString());
  }

  /**
   * 读取
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Semester_Exist.class})
  public void Get() {
    renderJson(Semester.dao.findById(getPara("id")));
  }

  /**
   * 激活
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Semester_Exist.class})
  public void Active() {
    Semester object = Semester.dao.findById(getPara("id"));
    if (object.get("state").toString().equals("1")) {
      renderText("该学期已为当前学期!");
    } else {
      object.set("state", 1).update();
      Db.update("UPDATE semester SET state = 0 WHERE id <> ?", getPara("id"));
      renderText("OK");
    }
  }
  /**
   * 注销
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Semester_Exist.class})
  public void Inactive() {
    Semester object = Semester.dao.findById(getPara("id"));
    if (object.get("state").toString().equals("0")) {
      renderText("该学期已非当前学期!");
    } else {
      object.set("state", 0).update();
      renderText("OK");
    }
  }

  /**
   * 删除
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Semester_Exist.class})
  public void Delete() {
    Semester object = Semester.dao.findById(getPara("id"));
    if (object.get("del").toString().equals("1")) {
      renderText("该学期已删除!");
    } else {
      object.set("del", 1).update();
      renderText("OK");
    }
  }
  /**
   * 保存
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Semester_Save.class})
  public void Save() {
    Semester object = new Semester();
    object.set("name", getPara("name"))
            .set("time_start", new Date(getParaToLong("time_start")))
            .set("time_end", new Date(getParaToLong("time_end")))
            .set("state", 0)
            .set("del", 0)
            .save();
    renderText("OK");
  }

  /**
   * 修改
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Semester_Exist.class, Semester_Edit.class})
  public void Edit() {
    Semester object = Semester.dao.findById(getPara("id"));
    object.set("name", getPara("name"))
            .set("time_start", new Date(getParaToLong("time_start")))
            .set("time_end", new Date(getParaToLong("time_end")))
            .update();
    renderText("OK");
  }

  /**
   * 导出
   */
  @Before({OverdueCheck.class, PermissionCheck.class})
  public void Download() throws IOException {
    String[] title={"序号","学期名称","开始日期","终止日期","学期状态"};
    String fileName = "Semester";
    String SQL = "select id AS 序号,name AS 学期名称, " +
            "(case state when 1 then '当前学期' when 2 then '非当前学期' else '错误' end ) AS 学期状态 " +
            "from semester where del = 0 AND name like '%"+getPara("keyword")+"%'" +
            "ORDER BY id ASC";
    ExportUtil.export(title,fileName,SQL,getResponse());
  }

  /**
   * 检测名称_新增
   */
  @Before(OverdueCheck.class)
  public void checkNameForAdd() {
    if (Db.find("SELECT * FROM semester WHERE name = ?", getPara("name")).size() != 0) {
      renderText("该学期名称已存在!");
    } else {
      renderText("OK");
    }
  }

  /**
   * 检测名称_修改
   */
  @Before(OverdueCheck.class)
  public void checkNameForEdit() {
    if (!Semester.dao.findById(getPara("id")).get("name").equals(getPara("name"))
            && Db.find("SELECT * FROM semester WHERE name = ?", getPara("name")).size() != 0) {
      renderText("该学期名称已存在!");
    } else {
      renderText("OK");
    }
  }

  /**
   * 当前学期
   */
  public static Semester getNow() {
    return Semester.dao.findFirst("SELECT * FROM semester WHERE state = 1");
  }
}
