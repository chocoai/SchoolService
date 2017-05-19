package com.wts.controller.Mobile;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.wts.entity.model.Teacher;
import com.wts.interceptor.OverdueCheck;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import static com.wts.util.Util.PermissionString;


public class MainMobile extends Controller {
  public void index() throws Exception{
    render("/static/html/mobile/Mobile_Login.html");
  }

  @Before(OverdueCheck.class)
  public void Teacher_Home() {
    render("/static/html/mobile/Teacher/Mobile_Teacher_Home.html");
  }
  /**
   * 登录
   * */
  public void Login() throws IOException{
//    String userId = "WangTianShuo";
//    WxCpMessage message = WxCpMessage.TEXT().agentId(26).toUser(userId).content("Hello World").build();
//    System.out.println("000");
//    try{
//    WxService.getMe().messageSend(message);
//    }catch (Exception e){
//      System.out.println(e.toString());
//    }
//    System.out.println("111");
    if (getPara("mobile").equals("1") && getPara("pass").equals("1")) {
      setCookie("user", "", 60 * 60 * 24 * 7);
      setCookie("password", "", 60 * 60 * 24 * 7);
      Teacher teacher = Teacher.dao.findById("1");
      setSessionAttr("Teacher", teacher);
      setCookie("dit", teacher.getId().toString(), 60 * 60 * 3);
      List<Record> lists = Db.find(
              "SELECT permission.url, teacherpermission.state FROM teacherpermission " +
                      " LEFT JOIN permission" +
                      " ON teacherpermission.permission_id = permission.id" +
                      " WHERE teacher_id=? AND permission.url LIKE '%Mobile/Page%'"
              ,((Teacher) getSessionAttr("Teacher")).getId().toString());
      String permission = "";
      for (int i=0;i<lists.size();i++){
        if (lists.get(i).get("state").toString().equals("1")){
          permission = permission + "\""+lists.get(i).get("url")+"\": true, ";
        } else {
          permission = permission + "\""+lists.get(i).get("url")+"\": false, ";
        }
      }
      setCookie("MenuMobile", "{"+permission.substring(0,permission.length()-2).replace("/","_")+"}", 60 * 6 * 10);
      setCookie("SemesterMobile", PermissionString("SemesterMobile",teacher.getId().toString()), 60 * 6 * 10);
      setCookie("CourseMobile", PermissionString("CourseMobile",teacher.getId().toString()), 60 * 6 * 10);
      setCookie("RoomMobile", PermissionString("RoomMobile",teacher.getId().toString()), 60 * 6 * 10);
      setCookie("TeacherMobile", PermissionString("TeacherMobile",teacher.getId().toString()), 60 * 6 * 10);
      setCookie("ParentMobile", PermissionString("ParentMobile",teacher.getId().toString()), 60 * 6 * 10);
      setCookie("StudentMobile", PermissionString("StudentMobile",teacher.getId().toString()), 60 * 6 * 10);

      renderText("OK");
    } else {
      renderText("error");
    }
  }

  /**
   * 桌面权限
   * */
  public void Permission() {
    List<Record> lists = Db.find(
            "SELECT permission.url, teacherpermission.state FROM teacherpermission " +
                    " LEFT JOIN permission" +
                    " ON teacherpermission.permission_id = permission.id" +
                    " WHERE teacher_id=? AND permission.url LIKE '%Mobile%'"
            ,((Teacher) getSessionAttr("Teacher")).getId().toString());
    String permission = "";
    for (int i=0;i<lists.size();i++){
      if (lists.get(i).get("state").toString().equals("1")){
        permission = permission + "\""+lists.get(i).get("url")+"\": true, ";
      } else {
        permission = permission + "\""+lists.get(i).get("url")+"\": false, ";
      }
    }
    setCookie("PermissionMobile", "{"+permission.substring(0,permission.length()-2).replace("/","_")+"}", 60 * 6 * 10);
  }

  /**
   * 菜单权限
   * */
  public void Menu() {
    List<Record> lists = Db.find(
            "SELECT permission.url, teacherpermission.state FROM teacherpermission " +
                    " LEFT JOIN permission" +
                    " ON teacherpermission.permission_id = permission.id" +
                    " WHERE teacher_id=? AND permission.url LIKE '%Mobile/Page%'"
            ,((Teacher) getSessionAttr("Teacher")).getId().toString());
    String permission = "";
    for (int i=0;i<lists.size();i++){
      if (lists.get(i).get("state").toString().equals("1")){
        permission = permission + "\""+lists.get(i).get("url")+"\": true, ";
      } else {
        permission = permission + "\""+lists.get(i).get("url")+"\": false, ";
      }
    }
    setCookie("MenuMobile", "{"+permission.substring(0,permission.length()-2).replace("/","_")+"}", 60 * 6 * 10);
  }

  /**
   * 验证码
   * */
  public void Img() {
    renderCaptcha();
  }

  /**
   * 登出
   * */
  public void Exit() {
    setSessionAttr("Teacher", "");
    setSessionAttr("Parent", "");
    setCookie("PermissionMobile", "", -1);
    setCookie("MenuMobile", "", -1);
    redirect("/");
  }
}
