package com.wts.controller;

import com.foxinmy.weixin4j.qy.WeixinProxy;
import com.jfinal.core.Controller;

public class MainController extends Controller {

  public void index() {

    WeixinProxy wp =new WeixinProxy();
    renderText(wp.getUserIdByCode(getPara("code")));

  }
}
