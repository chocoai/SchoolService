package com.wts.entity;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.wts.entity.*;
import com.wts.entity.model.Parent;
import com.wts.entity.model.Teacher;
import com.wts.interceptor.LoginManager;
import com.wts.interceptor.LoginParent;
import com.wts.interceptor.LoginTeacher;

public class MainController extends Controller {
  public void index() {
    render("/static/Login.html");
  }

  public void login() {
    if (getPara("login").equals("1") && getPara("pass").equals("1")) {
      Teacher teacher = new Teacher();
      teacher.setIsManager(1);
      teacher.setId(1);
      setSessionAttr("manager", teacher);
      renderText("forManager");
    } else if (getPara("login").equals("2") && getPara("pass").equals("2")) {
      Teacher teacher = new Teacher();
      teacher.setIsManager(0);
      teacher.setId(1);
      setSessionAttr("teacher", teacher);
      renderText("forTeacher");
    } else if (getPara("login").equals("3") && getPara("pass").equals("3")) {
      Parent parent = new Parent();
      parent.setId(1);
      setSessionAttr("parent", parent);
      renderText("forParent");
    } else {
      if (getPara("type").equals("teacher")
              && Teacher.dao.findFirst("select * from teacher where login=? and pass=? and state='1'", getPara("login"), getPara("pass")) != null) {
        Teacher teacher = Teacher.dao.findFirst("select * from teacher where login=? and pass=? and state='1'", getPara("login"), getPara("pass"));
        setSessionAttr("teacher", teacher);
        setCookie("die", teacher.getId().toString(), 60 * 30 * 30);
        renderText("forTeacher");
      } else if (getPara("type").equals("manager")
              && Teacher.dao.findFirst("select * from teacher where login=? and pass=? and state='1' and isManager='1'", getPara("login"), getPara("pass")) != null) {
        Teacher manager = Teacher.dao.findFirst("select * from teacher where login=? and pass=? and state='1' and isManager='1'", getPara("login"), getPara("pass"));
        setSessionAttr("manager", manager);
        setCookie("die", manager.getId().toString(), 60 * 30 * 30);
        renderText("forManager");
      } else if (getPara("type").equals("parent")
              && Parent.dao.findFirst("select * from parent where login=? and pass=? and state='1'", getPara("login"), getPara("pass")) != null) {
        Parent parent = Parent.dao.findFirst("select * from parent where login=? and pass=? and state='1'", getPara("login"), getPara("pass"));
        setSessionAttr("parent", parent);
        setCookie("die", parent.getId().toString(), 60 * 30 * 30);
        renderText("forParent");
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

  @Before(LoginManager.class)
  public void forManager() {
    render("/static/HomeForManager.html");
  }

  @Before(LoginTeacher.class)
  public void forTeacher() {
    render("/static/HomeForTeacher.html");
  }

  @Before(LoginParent.class)
  public void forParent() {
    render("/static/HomeForParent.html");
  }

  public void exit() {
    setSessionAttr("manager", "");
    setSessionAttr("teacher", "");
    setSessionAttr("parent", "");
    redirect("/");
  }

  public void bind() throws WeixinException {
    User u = WP.me.getUserByCode(getPara("code"));
    System.out.println(u.getUserId());
    renderText(u.getName());
  }
}
