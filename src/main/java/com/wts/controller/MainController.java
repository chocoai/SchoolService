package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.jfinal.core.Controller;
import com.wts.entity.*;

public class MainController extends Controller {

  public void index()  throws WeixinException {
    renderText(WP.me.getUserByCode(getPara("code")).getName());

  }
}
