package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.handler.DebugMessageHandler;
import com.foxinmy.weixin4j.handler.MessageHandlerAdapter;
import com.foxinmy.weixin4j.handler.WeixinMessageHandler;
import com.foxinmy.weixin4j.message.TextMessage;
import com.foxinmy.weixin4j.model.media.MediaDownloadResult;
import com.foxinmy.weixin4j.model.paging.Pageable;
import com.foxinmy.weixin4j.qy.message.ChatMessage;
import com.foxinmy.weixin4j.qy.message.CustomeMessage;
import com.foxinmy.weixin4j.qy.message.NotifyMessage;
import com.foxinmy.weixin4j.qy.model.IdParameter;
import com.foxinmy.weixin4j.qy.model.User;
import com.foxinmy.weixin4j.request.WeixinRequest;
import com.foxinmy.weixin4j.response.TextResponse;
import com.foxinmy.weixin4j.response.WeixinResponse;
import com.foxinmy.weixin4j.startup.WeixinServerBootstrap;
import com.foxinmy.weixin4j.tuple.Text;
import com.foxinmy.weixin4j.type.MediaType;
import com.foxinmy.weixin4j.util.AesToken;
import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.*;
import com.wts.entity.model.Enterprise;
import com.wts.entity.model.Teacher;
import com.wts.interceptor.AjaxFunction;
import com.wts.interceptor.LoginTeacher;

import java.io.*;

import static com.wts.util.EncryptUtils.encodeMD5String;

public class MainController extends Controller {
  public void index() {
    render("/static/Login.html");
  }
  public void login() {
    if (getPara("userId").equals("1") && getPara("pass").equals("1")) {
      setSessionAttr("teacher", "");
      renderText("sys");
    } else {
      Enterprise teacher = Enterprise.dao.findFirst("select * from enterprise where userId=? and pass=? and state='1'", getPara("userId"), getPara("pass"));
      if (teacher != null) {
        if (getPara("type").equals("teacher") && teacher.getIsTeacher() == 1) {
          setSessionAttr("teacher", teacher);
          setCookie("die", teacher.getId().toString(), 60 * 30);
          renderText("com");
        } else if (getPara("type").equals("manager") && teacher.getIsManager() == 1) {
          setSessionAttr("teacher", teacher);
          setCookie("die", teacher.getId().toString(), 60 * 30);
          renderText("sys");
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
  public void sys() {
    render("/static/Sys.html");
  }
  @Before(LoginTeacher.class)
  public void home() {
    renderText("这里是普通教师");
  }
  public void bind()  throws WeixinException {
    User u= WP.me.getUserByCode(getPara("code"));
    System.out.println(u.getUserId());
    renderText(u.getName());
  }
}
