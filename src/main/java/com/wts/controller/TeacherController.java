package com.wts.controller;

import com.jfinal.core.Controller;

public class TeacherController extends Controller {

  public void index() {
    render("/dist/TeacherManage.html");
  }
  public void getName() {
    renderText("王天硕");
  }
  public void checkName() {
    if (!getPara("name").matches("[\u4e00-\u9fa5]+")) {
      renderText("请输入汉字!");
    } else if (getPara("name").length()<2) {
      renderText("请输入两个以上汉字！");
    } else {
      renderText("OK");
    }
  }
}
