package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.model.Semester;
import com.wts.interceptor.AjaxManager;

import static com.wts.util.Util.getString;

public class SemesterController extends Controller {

  /**
   * 当前学期
   */
  public static Semester getNow() {
    return Semester.dao.findFirst("SELECT * FROM semester WHERE state = 1");
  }
  /**
   * 列表
   */
  @Before(AjaxManager.class)
  public void list() {
    renderJson(Semester.dao.find("SELECT * FROM semester ORDER BY id DESC"));
  }
  /**
   * 查询
   */
  @Before(AjaxManager.class)
  public void query() {
    renderJson(Semester.dao.paginate(getParaToInt("pageCurrent"), getParaToInt("pageSize"), "SELECT *", "FROM semester WHERE name LIKE '%?%' ORDER BY id DESC", getPara("queryString")).getList());
  }

  /**
   * 计数
   */
  @Before(AjaxManager.class)
  public void total() {
    Long count = Db.queryLong("SELECT COUNT(*) FROM semester WHERE name LIKE '%?%' ORDER BY id DESC", getPara("queryString"));
    if (count % getParaToInt("pageSize") == 0) {
      renderText((count / getParaToInt("pageSize")) + "");
    } else {
      renderText((count / getParaToInt("pageSize") + 1) + "");
    }
  }
  /**
   * 激活
   */
  @Before({Tx.class, AjaxManager.class})
  public void active() {
    Semester semester = Semester.dao.findById(getPara("semesterId"));
    if (semester == null) {
      renderText("要重新激活的学期不存在!");
    } else if (semester.get("state").toString().equals("1")) {
      renderText("该学期已激活!");
    } else {
      semester.set("state", 1).update();
      Db.update("UPDATE semester SET state = 0 WHERE id <> ?", getPara("semesterId"));
      renderText("OK");
    }
  }
  /**
   * 保存
   */
  @Before({Tx.class, AjaxManager.class})
  public void save() {
    if (Semester.dao.find("SELECT * FROM semester WHERE name = ?", getPara("name")).size() != 0) {
      renderText("该名称已存在!");
    } else {
      Semester semester = new Semester();
      semester.set("name", getPara("name"))
              .set("timeStart", getPara("timeStart"))
              .set("timeEnd", getPara("timeEnd"))
              .set("state", 0)
              .save();
      renderText("OK");
    }
  }
  /**
   * 修改
   */
  @Before({Tx.class, AjaxManager.class})
  public void edit() {
    Semester semester = Semester.dao.findById(getPara("semesterId"));
    if (semester == null) {
      renderText("要修改的学期不存在!");
    } else {
      if (!getString(semester.getStr("name")).equals(getPara("name"))
              && Semester.dao.find("SELECT * FROM semester WHERE name = ?", getPara("name")).size() != 0) {
        renderText("该名称已存在!");
      } else {
        semester.set("name", getPara("name"))
                .set("timeStart", getPara("timeStart"))
                .set("timeEnd", getPara("timeEnd"))
                .update();
        renderText("OK");
      }
    }
  }
}
