package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.jssdk.JSSDKConfigurator;
import com.foxinmy.weixin4j.qy.WeixinProxy;
import com.foxinmy.weixin4j.qy.model.User;
import com.foxinmy.weixin4j.type.TicketType;
import com.jfinal.core.Controller;
import com.wts.entity.WP;

public class UserController extends Controller {

  public void addUserPage()  throws WeixinException {

     if (getRequest().getHeader("user-agent").contains("MicroMessenger")){
       User u= WP.me.getUserByCode(getPara("code"));
       render("/src/html/addUser.html");
     }else{
       render("/src/html/addUser.html");
       //renderText("false");
     }
      String j= new JSSDKConfigurator(new WeixinProxy().getTicketManager(TicketType.jsapi)).toJSONConfig("fff");

  }
}
