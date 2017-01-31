package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.wts.entity.*;
import com.wts.entity.model.Enterprise;
import com.wts.interceptor.LoginTeacher;

public class MainController extends Controller {
  public void index() {
    render("/static/Login.html");
  }
  public void login() {
    if (getPara("userId").equals("1") && getPara("pass").equals("1")) {
      setSessionAttr("teacher", "");
      renderText("forManager");
    } else {
      Enterprise enterprise = Enterprise.dao.findFirst("select * from enterprise where userId=? and pass=? and state='1'", getPara("userId"), getPara("pass"));
      if (enterprise != null) {
        if (getPara("type").equals("teacher") && enterprise.getIsTeacher() == 1) {
          setSessionAttr("teacher", enterprise);
          setCookie("die", enterprise.getId().toString(), 60 * 30);
          renderText("forTeacher");
        } else if (getPara("type").equals("manager") && enterprise.getIsManager() == 1) {
          setSessionAttr("teacher", enterprise);
          setCookie("die", enterprise.getId().toString(), 60 * 30);
          renderText("forManager");
        } else if (getPara("type").equals("parent") && enterprise.getIsParent() == 1) {
          setSessionAttr("parent", enterprise);
          setCookie("die", enterprise.getId().toString(), 60 * 30);
          renderText("forParent");
        } else {
          renderText("noPower");
        }
      } else {
        renderText("error");
      }
    }
  }
//    render("/static/Home.html");


//    User u= WP.me.getUserByCode(getPara("code"));
//    System.out.println(getPara("code"));
//    setSessionAttr("user",u);

//    renderText("aaa");
//    try {
//      //NotifyMessage CM = new NotifyMessage(25,new Text("hello world!!!"),new IdParameter().putUserIds("wts"),false);
//      //WP.me.sendNotifyMessage(CM);
////上传指定文件
//      //System.out.println(WP.me.uploadMedia(25,new FileInputStream(new File("c:/777.jpg")),"aa"));
//      //System.out.println(WP.me.listMaterialMedia(25, MediaType.image,new Pageable(1,50)).toString());
//      // 创建文件输出流对象
//      FileOutputStream os = new FileOutputStream("c:/a.jpg");
//      os.write(WP.me.downloadMedia(25,"2DYEgQokxhwD197hQGE5L8yMXdkAWaY3YQrpcR5uc1crKnOVs4Kt-Ope4P9xx5jxFvXcYm3MeKJiiUIufeEKqnQ").getContent());
//// 关闭输出流
//      os.close();
//
//    } catch (Exception e){
//      System.out.println(e.getMessage());
//    }
////    renderText(WP.me.getUserByCode(getPara("code")).getName());

  @Before(LoginTeacher.class)
  public void forManager() {
    render("/static/HomeForManager.html");
  }
  @Before(LoginTeacher.class)
  public void forTeacher() {
    render("/static/HomeForTeacher.html");
  }
  @Before(LoginTeacher.class)
  public void forParent() {
    render("/static/HomeForParent.html");
  }

  public void bind()  throws WeixinException {
    User u= WP.me.getUserByCode(getPara("code"));
    System.out.println(u.getUserId());
    renderText(u.getName());
  }
}
