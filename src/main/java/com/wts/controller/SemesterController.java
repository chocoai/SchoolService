package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.model.Semester;
import com.wts.interceptor.AjaxManager;

public class SemesterController extends Controller {

  public static Semester getNow() {
    return Semester.dao.findFirst("SELECT * FROM semester WHERE state = 1");

  }
  @Before({Tx.class, AjaxManager.class})
  public void active() {
    Semester semester = Semester.dao.findById(getPara("semesterId"));
    if (semester == null) {
      renderText("要重新激活的学期不存在!");
    } else if (semester.get("state").toString().equals("1")) {
      renderText("该学期已激活!");
    } else {
      semester.set("state",1).update();
      Db.update("UPDATE semester SET state = 0 WHERE id <> ?",getPara("semesterId"));
      renderText("OK");
    }
  }
}
