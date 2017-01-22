package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.WP;
import com.wts.entity.model.*;
import com.wts.interceptor.AjaxFunction;

import java.util.ArrayList;
import java.util.List;

import static com.jfinal.plugin.activerecord.Db.find;

public class RoomController extends Controller {

  public void index() throws WeixinException {
// 检测session中是否存在teacher
    if (getSessionAttr("teacher") == null) {
      // 检测cookie中是否存在EnterpriseId
      if (getCookie("die") == null || getCookie("die").equals("")) {
        // 检测是否来自微信请求
        if (!(getPara("code") == null || getPara("code").equals(""))) {
          User user = WP.me.getUserByCode(getPara("code"));
          Enterprise teacher = Enterprise.dao.findFirst("select * from enterprise where state=1 and userId=?", user.getUserId());
          setSessionAttr("teacher", teacher);
          render("/static/RoomManage.html");
        } else {
          redirect("/");
        }
      } else {
        Enterprise teacher = Enterprise.dao.findById(getCookie("die"));
        setSessionAttr("teacher", teacher);
        render("/static/RoomManage.html");
      }
    } else {
      render("/static/RoomManage.html");
    }
  }
  @Before(AjaxFunction.class)
  public void queryByName() {
    Page<Room> rooms= Room.dao.queryByName(getParaToInt("pageCurrent"),getParaToInt("pageSize"),getPara("queryString"));
    renderJson(rooms.getList());
  }
  @Before(AjaxFunction.class)
  public void totalByName() {
    Long count = Db.queryLong("select count(*) from room where name like '%"+ getPara("queryString") +"%'");
    if (count%getParaToInt("pageSize")==0) {
      renderText((count/getParaToInt("pageSize"))+"");
    } else {
      renderText((count/getParaToInt("pageSize")+1)+"");
    }
  }
  @Before(AjaxFunction.class)
  public void checkNameForNew() {
    if (!getPara("name").matches("\\d{4}[\\u7ea7]\\d{1,2}[\\u73ed]")) {
      renderText("班级名称格式应为：XXXX级XX班");
    } else if (Room.dao.find("select * from room where name=?", getPara("name")).size()!=0) {
      renderText("该班级已存在!");
    } else {
      renderText("OK");
    }
  }
  @Before(AjaxFunction.class)
  public void checkNameForEdit() {
    if (!getPara("name").matches("\\d{4}[\\u7ea7]\\d{1,2}[\\u73ed]")) {
      renderText("班级名称格式应为：XXXX级XX班");
    } else if (!Room.dao.findById(getPara("id")).getName().equals(getPara("name"))
            && Room.dao.find("select * from room where name=?", getPara("name")).size() != 0) {
      renderText("该班级已存在!");
    } else {
      renderText("OK");
    }
  }
  @Before(AjaxFunction.class)
  public void getById() {
    Room room = Room.dao.findById(getPara("id"));
    renderJson(room);
  }
  @Before(AjaxFunction.class)
  public void deleteById() {
    if (Room.dao.findById(getPara("id")) == null) {
      renderText("未找到指定id的班级");
    } else {
      Room room = Room.dao.findById(getPara("id"));
      room.set("state",2).update();
      renderText("OK");
    }
  }
  @Before(AjaxFunction.class)
  public void teacherList() {
    List<Enterprise> teachers = Enterprise.dao.find("select * from enterprise where (isTeacher=1 or isManager=1) and (state=1 or state=2)");
    renderJson(teachers);
  }
  @Before(AjaxFunction.class)
  public  void courseList() {
    List<Course> courses = Course.dao.find("select * from course");
    renderJson(courses);
  }

  public void getCourseTeacher() {
    List<Courseplan> courseplan = Courseplan.dao.find("select teacher_id from courseplan where room_id=? and course_id=?",getPara("room"),getPara("course"));
    if (courseplan.size()!=0) {
      String sp1 = "";
      for (int i = 0; i < courseplan.size(); i++) {
        sp1 = sp1 + "'" + courseplan.get(i).get("teacher_id") + "',";
      }
      renderText("{course: [" + sp1.substring(0,sp1.length()-1) + "]}");
    } else {
      renderText("");
    }

  }
  @Before({Tx.class,AjaxFunction.class})
  public void save()  {
    if (!getPara("name").matches("\\d{4}[\\u7ea7]\\d{1,2}[\\u73ed]")) {
      renderText("班级名称格式应为：XXXX级XX班");
    } else if (Room.dao.find("select * from room where name=?", getPara("name")).size()!=0) {
      renderText("该班级已存在!");
    } else {
      Room room = new Room();
      room.set("name",getPara("name")).set("state",1).save();
      String[] course1 = getParaValues("course1[]");
      String[] course2 = getParaValues("course2[]");
      String[] course3 = getParaValues("course3[]");
      String[] course4 = getParaValues("course4[]");
      if (course1!=null){
        for (String i : course1){
          Courseplan courseplan = new Courseplan();
          courseplan.set("course_id",1).set("room_id",room.get("id")).set("teacher_id",i).save();
        }
      }
      if (course2!=null){
        for (String i : course2){
          Courseplan courseplan = new Courseplan();
          courseplan.set("course_id",2).set("room_id",room.get("id")).set("teacher_id",i).save();
        }
      }
      if (course3!=null){
        for (String i : course3){
          Courseplan courseplan = new Courseplan();
          courseplan.set("course_id",3).set("room_id",room.get("id")).set("teacher_id",i).save();
        }
      }
      if (course4!=null){
        for (String i : course4){
          Courseplan courseplan = new Courseplan();
          courseplan.set("course_id",4).set("room_id",room.get("id")).set("teacher_id",i).save();
        }
      }
      renderText("OK");
    }
  }
  @Before({Tx.class,AjaxFunction.class})
  public void edit()  {

  }
}
