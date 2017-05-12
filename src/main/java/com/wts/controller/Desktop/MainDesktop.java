package com.wts.controller.Desktop;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.wts.controller.Desktop.Teacher.SemesterDesktop;
import com.wts.entity.model.Teacher;
import com.wts.interceptor.OverdueCheck;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import static com.wts.util.Util.PermissionString;

public class MainDesktop extends Controller {
  private static Logger logger = Logger.getLogger(MainDesktop.class);

  public void index() throws Exception{
    render("/static/html/desktop/Desktop_Login.html");
  }

  @Before(OverdueCheck.class)
  public void Teacher_Home() {
    render("/static/html/desktop/Teacher/Desktop_Teacher_Home.html");
  }
  /**
   * 登录
   * */
  public void Login() throws IOException{
    if (getPara("mobile").equals("") && getPara("password").equals("")) {
      setCookie("mobile", "", 60 * 60 * 24 * 7);
      setCookie("password", "", 60 * 60 * 24 * 7);
      Teacher teacher = Teacher.dao.findById("1");
      setSessionAttr("Teacher", teacher);
      setCookie("dit", teacher.getId().toString(), 60 * 60 * 3);
      List<Record> lists = Db.find(
              "SELECT permission.url, teacherpermission.state FROM teacherpermission " +
                      " LEFT JOIN permission" +
                      " ON teacherpermission.permission_id = permission.id" +
                      " WHERE teacher_id=? AND permission.url LIKE '%Page%'"
              ,((Teacher) getSessionAttr("Teacher")).getId().toString());
      String permission = "";
      for (int i=0;i<lists.size();i++){
        if (lists.get(i).get("state").toString().equals("1")){
          permission = permission + "\""+lists.get(i).get("url")+"\": true, ";
        } else {
          permission = permission + "\""+lists.get(i).get("url")+"\": false, ";
        }
      }
      setCookie("menu", "{"+permission.substring(0,permission.length()-2).replace("/","_")+"}", 60 * 6 * 10);
      setCookie("name",URLEncoder.encode(teacher.getName(), "UTF-8"),60 * 6 * 10);
      setCookie("SemesterDesktop", PermissionString("SemesterDesktop",teacher.getId().toString()), 60 * 6 * 10);
      setCookie("CourseDesktop", PermissionString("SemesterDesktop",teacher.getId().toString()), 60 * 6 * 10);
      setCookie("RoomDesktop", PermissionString("SemesterDesktop",teacher.getId().toString()), 60 * 6 * 10);
      setCookie("StudentDesktop", PermissionString("StudentDesktop",teacher.getId().toString()), 60 * 6 * 10);
      setCookie("TeacherDesktop", PermissionString("TeacherDesktop",teacher.getId().toString()), 60 * 6 * 10);
      setCookie("ParentDesktop", PermissionString("ParentDesktop",teacher.getId().toString()), 60 * 6 * 10);
      setCookie("StudentParentIdentityDesktop", PermissionString("StudentParentIdentityDesktop",teacher.getId().toString()), 60 * 6 * 10);
      setCookie("RoomStudentDesktop", PermissionString("RoomStudentDesktop",teacher.getId().toString()), 60 * 6 * 10);
      setCookie("CourseRoomTeacherDesktop", PermissionString("CourseRoomTeacherDesktop",teacher.getId().toString()), 60 * 6 * 10);
      logger.warn("function:"+this.getClass().getSimpleName()+"/Login;"+"teacher_id:"+((Teacher) getSessionAttr("Teacher")).getId().toString()+";");
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
                    " WHERE teacher_id=? AND permission.url LIKE '%Desktop%'"
            ,((Teacher) getSessionAttr("Teacher")).getId().toString());
    String permission = "";
    for (int i=0;i<lists.size();i++){
      if (lists.get(i).get("state").toString().equals("1")){
        permission = permission + "\""+lists.get(i).get("url")+"\": true, ";
      } else {
        permission = permission + "\""+lists.get(i).get("url")+"\": false, ";
      }
    }
    setCookie("permission", "{"+permission.substring(0,permission.length()-2).replace("/","_")+"}", 60 * 6 * 10);
    setCookie("name",((Teacher) getSessionAttr("Teacher")).getName(),60 * 6 * 10);
  }

  /**
   * 菜单权限
   * */
  public void Menu() {
    List<Record> lists = Db.find(
            "SELECT permission.url, teacherpermission.state FROM teacherpermission " +
                    " LEFT JOIN permission" +
                    " ON teacherpermission.permission_id = permission.id" +
                    " WHERE teacher_id=? AND permission.url LIKE '%Page%'"
            ,((Teacher) getSessionAttr("Teacher")).getId().toString());
    String permission = "";
    for (int i=0;i<lists.size();i++){
      if (lists.get(i).get("state").toString().equals("1")){
        permission = permission + "\""+lists.get(i).get("url")+"\": true, ";
      } else {
        permission = permission + "\""+lists.get(i).get("url")+"\": false, ";
      }
    }
    setCookie("menu", "{"+permission.substring(0,permission.length()-2).replace("/","_")+"}", 60 * 6 * 10);
    setCookie("name",((Teacher) getSessionAttr("Teacher")).getName(),60 * 6 * 10);
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
    setSessionAttr("manager", "");
    setSessionAttr("Teacher", "");
    setSessionAttr("parent", "");
    setCookie("permission", "", -1);
    redirect("/");
  }
}
