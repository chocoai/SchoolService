package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.jfinal.core.Controller;

public class TagController extends Controller {

  public void index() throws WeixinException {
    render("/static/Tag.html");
  }
  public void getName() throws WeixinException {
    renderText("济南市育明小学");
  }


}
