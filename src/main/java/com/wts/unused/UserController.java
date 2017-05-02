package com.wts.unused;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.jssdk.JSSDKAPI;
import com.foxinmy.weixin4j.jssdk.JSSDKConfigurator;
import com.foxinmy.weixin4j.qy.WeixinProxy;
import com.foxinmy.weixin4j.qy.model.User;
import com.foxinmy.weixin4j.type.TicketType;
import com.jfinal.core.Controller;
import com.wts.entity.WP;
import com.wts.util.ParamesAPI;

public class UserController extends Controller {

  public void addUserPage()  throws WeixinException {

     if (getRequest().getHeader("user-agent").contains("MicroMessenger")){
       User u= WP.me.getUserByCode(getPara("code"));
       render("/src/html/addUser.html");
     }else{
       render("/src/html/addUser.html");
       //renderText("false");
     }
      new JSSDKConfigurator(WP.me.getTicketManager(TicketType.jsapi)).toJSONConfig(ParamesAPI.URL);
  }
  public void tokenSignature(){
    render("/static/Picture.html");
  }
  public void picture()  throws WeixinException {
    JSSDKConfigurator a =new JSSDKConfigurator(WP.me.getTicketManager(TicketType.jsapi));
    a.apis(JSSDKAPI.chooseImage);
    renderJson(a.toJSONConfig("http://wts.s1.natapp.cc/user/tokenSignature"));
  }
}
