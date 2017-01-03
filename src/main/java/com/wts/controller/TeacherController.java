package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.jfinal.core.Controller;

public class TeacherController extends Controller {

  public void index() {
    render("/dist/TeacherManage.html");
  }
  public void getName() {
    renderText("王天硕");
  }
}
