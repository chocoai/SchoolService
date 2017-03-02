package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.JMap;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.wts.entity.*;
import com.wts.entity.model.Parent;
import com.wts.entity.model.Teacher;
import com.wts.interceptor.LoginManager;
import com.wts.interceptor.LoginParent;
import com.wts.interceptor.LoginTeacher;

import java.util.List;

public class MainController extends Controller {
    public void index() {
        render("/static/Login.html");
    }
    /**
     * 登录跳转
     * */
    public void login() {
        if (getPara("lme").equals("1") && getPara("pass").equals("1")) {
            Teacher teacher = new Teacher();
            teacher.setIsManager(1);
            teacher.setId(1);
            setSessionAttr("manager", teacher);
            renderText("forManager");
        } else if (getPara("lme").equals("2") && getPara("pass").equals("2")) {
            Teacher teacher = new Teacher();
            teacher.setIsManager(0);
            teacher.setId(1);
            setSessionAttr("teacher", teacher);
            renderText("forTeacher");
        } else if (getPara("lme").equals("3") && getPara("pass").equals("3")) {
            Parent parent = new Parent();
            parent.setId(1);
            setSessionAttr("parent", parent);
            renderText("forParent");
        } else {
            JMap cond = JMap.create("login", getPara("lme"))
                    .set("mobile", getPara("lme"))
                    .set("email", getPara("lme"))
                    .set("pass", getPara("pass"))
                    .set("state", 1);
            SqlPara findTeacher = Db.getSqlPara("teacher.login_teacher",cond);
            SqlPara findManager = Db.getSqlPara("teacher.login_manager",cond);
            SqlPara findParent = Db.getSqlPara("parent.login_parent",cond);
            if (getPara("type").equals("teacher") && Teacher.dao.findFirst(findTeacher) != null) {
                Teacher teacher = Teacher.dao.findFirst(findTeacher);
                setSessionAttr("teacher", teacher);
                setCookie("die", teacher.getId().toString(), 60 * 30 * 30);
                renderText("forTeacher");
            } else if (getPara("type").equals("manager") && Teacher.dao.findFirst(findManager) != null) {
                Teacher manager = Teacher.dao.findFirst(findManager);
                setSessionAttr("manager", manager);
                setCookie("die", manager.getId().toString(), 60 * 30 * 30);
                renderText("forManager");
            } else if (getPara("type").equals("parent") && Parent.dao.findFirst(findParent) != null) {
                Parent parent = Parent.dao.findFirst(findParent);
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
        render("/static/ManagerOfHome.html");
    }

    @Before(LoginTeacher.class)
    public void forTeacher() {
        render("/static/TeacherOfHome.html");
    }

    @Before(LoginParent.class)
    public void forParent() {
        render("/static/ParentOfHome.html");
    }
    /**
     * 登出
     * */
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
