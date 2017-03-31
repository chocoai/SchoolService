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
import com.wts.util.PinyinTool;

import java.util.List;

public class MainController extends Controller {
    public void index() {
        render("/static/html/mobile/Mobile_Login.html");
    }
    public void mobile() {
        render("/static/html/mobile/Mobile_Login.html");
    }
    /**
     * 登录跳转
     * */
    public void login() {
        if (getPara("lmewuq").equals("1") && getPara("pass").equals("1")) {
            Teacher teacher = new Teacher();
            teacher.setIsManager(1);
            teacher.setId(1);
            setSessionAttr("manager", teacher);
            renderText("Mobile_Manager_Home");
        } else if (getPara("lmewuq").equals("2") && getPara("pass").equals("2")) {
            Teacher teacher = new Teacher();
            teacher.setIsManager(0);
            teacher.setId(1);
            setSessionAttr("teacher", teacher);
            renderText("Mobile_Teacher_Home");
        } else if (getPara("lmewuq").equals("3") && getPara("pass").equals("3")) {
            Parent parent = new Parent();
            parent.setId(1);
            setSessionAttr("parent", parent);
            renderText("Mobile_Parent_Home");
        } else {
            JMap cond = JMap.create("lmewuq", getPara("lme"))
                    .set("pass", getPara("pass"))
                    .set("state", 1);
            SqlPara findTeacher = Db.getSqlPara("teacher.login_teacher",cond);
            SqlPara findManager = Db.getSqlPara("teacher.login_manager",cond);
            SqlPara findParent = Db.getSqlPara("parent.login_parent",cond);
            if (getPara("type").equals("teacher") && Teacher.dao.findFirst(findTeacher) != null) {
                Teacher teacher = Teacher.dao.findFirst(findTeacher);
                setSessionAttr("teacher", teacher);
                setCookie("die", teacher.getId().toString(), 60 * 30 * 30);
                renderText("Mobile_Teacher_Home");
            } else if (getPara("type").equals("manager") && Teacher.dao.findFirst(findManager) != null) {
                Teacher manager = Teacher.dao.findFirst(findManager);
                setSessionAttr("manager", manager);
                setCookie("die", manager.getId().toString(), 60 * 30 * 30);
                renderText("Mobile_Manager_Home");
            } else if (getPara("type").equals("parent") && Parent.dao.findFirst(findParent) != null) {
                Parent parent = Parent.dao.findFirst(findParent);
                setSessionAttr("parent", parent);
                setCookie("die", parent.getId().toString(), 60 * 30 * 30);
                renderText("Mobile_Parent_Home");
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
    public void Mobile_Manager_Home() {
        render("/static/html/mobile/manager/Mobile_Manager_Home.html");
    }

    @Before(LoginTeacher.class)
    public void Mobile_Teacher_Home() {
        render("/static/html/mobile/teacher/Mobile_Teacher_Home.html");
    }

    @Before(LoginParent.class)
    public void Mobile_Parent_Home() {
        render("/static/html/mobile/parent/Mobile_Parent_Home.html");
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
    public static String getUserId(String personName) {
        try {
            String UserId = new PinyinTool().toPinYin(personName, "", PinyinTool.Type.FIRSTUPPER);
            String UserIds;
            if (Teacher.dao.findFirst("SELECT * FROM teacher WHERE userId = ?", UserId) == null
                    && Parent.dao.findFirst("SELECT * FROM parent WHERE userId = ?", UserId) == null) {
                return UserId.replaceAll("u:","v");
            } else {
                int i = 1;
                do {
                    UserIds = UserId + i;
                    i++;
                }
                while (Teacher.dao.findFirst("SELECT * FROM teacher WHERE userId = ?", UserId) != null && Parent.dao.findFirst("SELECT * FROM parent WHERE userId = ?", UserId) != null);
                return UserIds.replaceAll("u:","v");
            }
        } catch (Exception e) {
            return "";
        }
    }
}
