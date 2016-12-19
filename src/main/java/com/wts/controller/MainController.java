package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.message.ChatMessage;
import com.foxinmy.weixin4j.qy.message.CustomeMessage;
import com.foxinmy.weixin4j.qy.message.NotifyMessage;
import com.jfinal.core.Controller;
import com.wts.entity.*;

public class MainController extends Controller {

  public void index()  throws WeixinException {
    //renderText("aaa");
    NotifyMessage CM = new NotifyMessage();
    WP.me.;
    WP.me.sendNotifyMessage();

    renderText(WP.me.getUserByCode(getPara("code")).getName());
  }
}
