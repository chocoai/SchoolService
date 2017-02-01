package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.message.NotifyMessage;
import com.foxinmy.weixin4j.qy.model.IdParameter;
import com.foxinmy.weixin4j.qy.model.User;
import com.foxinmy.weixin4j.tuple.Text;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.WP;
import com.wts.entity.model.*;
import com.wts.interceptor.Ajax;
import com.wts.interceptor.AjaxManager;
import com.wts.interceptor.AjaxTeacher;
import com.wts.interceptor.Login;
import com.wts.util.ParamesAPI;
import com.wts.util.Util;

import java.util.List;

import static com.jfinal.plugin.activerecord.Db.find;

public class RoomController extends Controller {

  public void forManager() throws WeixinException {
// 检测session中是否存在teacher
    if (getSessionAttr("manager") == null || ((Enterprise)getSessionAttr("manager")).getIsManager()!=1) {
      // 检测cookie中是否存在EnterpriseId
      if (getCookie("die") == null || getCookie("die").equals("")) {
        // 检测是否来自微信请求
        if (!(getPara("code") == null || getPara("code").equals(""))) {
          User user = WP.me.getUserByCode(getPara("code"));
          Enterprise enterprise = Enterprise.dao.findFirst("select * from enterprise where state=1 and isManager=1 and userId=?", user.getUserId());
          // 检测是否有权限
          if (enterprise != null) {
            setSessionAttr("manager", enterprise);
            setCookie("die", enterprise.getId().toString(), 60 * 30);
            render("/static/RoomForManager.html");
          } else {
            redirect("/");
          }
        } else {
          redirect("/");
        }
      } else {
        Enterprise enterprise = Enterprise.dao.findById(getCookie("die"));
        setSessionAttr("manager", enterprise);
        render("/static/RoomForManager.html");
      }
    } else {
      render("/static/RoomForManager.html");
    }
  }
  @Before(AjaxManager.class)
  public void queryByName() {
    Page<Room> rooms= Room.dao.queryByName(getParaToInt("pageCurrent"),getParaToInt("pageSize"),getPara("queryString"));
    renderJson(rooms.getList());
  }
  @Before(AjaxManager.class)
  public void totalByName() {
    Long count = Db.queryLong("select count(*) from room where name like '%"+ getPara("queryString") +"%'");
    if (count%getParaToInt("pageSize")==0) {
      renderText((count/getParaToInt("pageSize"))+"");
    } else {
      renderText((count/getParaToInt("pageSize")+1)+"");
    }
  }
  @Before(AjaxManager.class)
  public void checkNameForNew() {
//    if (!getPara("name").matches("\\d{4}[\\u7ea7]\\d{1,2}[\\u73ed]")) {
//      renderText("班级名称格式应为：XXXX级XX班");
    if (Room.dao.find("select * from room where name=?", getPara("name")).size()!=0) {
      renderText("该班级已存在!");
    } else {
      renderText("OK");
    }
  }
  @Before(AjaxManager.class)
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
  @Before(AjaxManager.class)
  public void getById() {
    Room room = Room.dao.findById(getPara("id"));
    renderJson(room);
  }
  @Before(AjaxManager.class)
  public void inactiveById() {
    Room room = Room.dao.findById(getPara("id"));
    if (room == null) {
      renderText("未找到指定id的班级");
    } else if (room.get("state").toString().equals("2")) {
      renderText("该班级已注销!");
    } else {
      room.set("state",2).update();
      renderText("OK");
    }
  }
  @Before({Tx.class,AjaxManager.class})
  public void activeById() {
    Room room = Room.dao.findById(getPara("id"));
    if (room == null) {
      renderText("要重新激活的班级不存在!");
    } else if (room.get("state").toString().equals("1")) {
      renderText("该班级已激活!");
    } else {
      room.set("state",1).update();
      renderText("OK");
    }
  }
  @Before(AjaxManager.class)
  public void roomList() {
    List<Room> rooms = Room.dao.find("select * from room where state=1 order by name desc");
    renderJson(rooms);
  }
  @Before(AjaxTeacher.class)
  public void teacherRoomList() {
    List<Record> rooms = Db.find("select DISTINCT room_id as id,room.name,room.state from roomplan left join room on room.id=roomplan.room_id where teacher_id=?",((Enterprise) getSessionAttr("teacher")).getId());
    renderJson(rooms);
  }
  @Before(AjaxTeacher.class)
  public void teacherRoomFirst() {
    Roomplan roomplan = Roomplan.dao.findFirst("select DISTINCT room_id as id from roomplan where teacher_id=?",((Enterprise) getSessionAttr("teacher")).get("id").toString());
    if (roomplan!=null){
        renderText(roomplan.get("id").toString());
    }else{
        renderText("0");
    }
  }
  @Before(AjaxTeacher.class)
  public void roomCourseList() {
    List<Record> courses = Db.find("select DISTINCT course.name,roomplan.course_id as id from roomplan left join course on course.id=roomplan.course_id where roomplan.teacher_id=? and roomplan.room_id=?",((Enterprise) getSessionAttr("teacher")).getId(),getPara("id"));
    renderJson(courses);
  }
  @Before(AjaxTeacher.class)
  public void roomCourseFirst() {
    Roomplan roomplan = Roomplan.dao.findFirst("select DISTINCT course_id as id from roomplan where teacher_id=? and roomplan.room_id=?",((Enterprise) getSessionAttr("teacher")).get("id").toString(),getPara("id"));
    if (roomplan!=null){
      renderText(roomplan.get("id").toString());
    }else{
      renderText("0");
    }
  }
  @Before({Login.class, Ajax.class})
  public void getNameById() {
    Room room = Room.dao.findById(getPara("id"));
    renderText(room.get("name").toString());
  }
  @Before(AjaxManager.class)
  public  void courseList() {
    List<Course> courses = Course.dao.find("select * from course");
    renderJson(courses);
  }
  public void getRoomTeachers() {
    List<Roomplan> roomplan = Roomplan.dao.find("select * from roomplan where room_id=? and course_id=?",getPara("room"),getPara("course"));
    if (roomplan.size()!=0) {
      String sp1 = "";
      for (int i = 0; i < roomplan.size(); i++) {
        sp1 = sp1 + "'" + roomplan.get(i).get("teacher_id") + "',";
      }
      renderText("{course: [" + sp1.substring(0,sp1.length()-1) + "]}");
    } else {
      renderText("{}");
    }
  }
  @Before(AjaxManager.class)
  public void getRoomTeacher() {
    List<Roomplan> roomplan1 = Roomplan.dao.find("select * from roomplan where room_id=? and course_id=1",getPara("roomId"));
    List<Roomplan> roomplan2 = Roomplan.dao.find("select * from roomplan where room_id=? and course_id=2",getPara("roomId"));
    List<Roomplan> roomplan3 = Roomplan.dao.find("select * from roomplan where room_id=? and course_id=3",getPara("roomId"));
    List<Roomplan> roomplan4 = Roomplan.dao.find("select * from roomplan where room_id=? and course_id=4",getPara("roomId"));
    List<Roomplan> roomplan5 = Roomplan.dao.find("select * from roomplan where room_id=? and course_id=5",getPara("roomId"));
    List<Roomplan> roomplan6 = Roomplan.dao.find("select * from roomplan where room_id=? and course_id=6",getPara("roomId"));
    List<Roomplan> roomplan7 = Roomplan.dao.find("select * from roomplan where room_id=? and course_id=7",getPara("roomId"));
    List<Roomplan> roomplan8 = Roomplan.dao.find("select * from roomplan where room_id=? and course_id=8",getPara("roomId"));
    List<Roomplan> roomplan9 = Roomplan.dao.find("select * from roomplan where room_id=? and course_id=9",getPara("roomId"));
    List<Roomplan> roomplan10 = Roomplan.dao.find("select * from roomplan where room_id=? and course_id=10",getPara("roomId"));
    List<Roomplan> roomplan11 = Roomplan.dao.find("select * from roomplan where room_id=? and course_id=11",getPara("roomId"));
    List<Roomplan> roomplan12 = Roomplan.dao.find("select * from roomplan where room_id=? and course_id=12",getPara("roomId"));

    String rp1 = "";
    String rp2 = "";
    String rp3 = "";
    String rp4 = "";
    String rp5 = "";
    String rp6 = "";
    String rp7 = "";
    String rp8 = "";
    String rp9 = "";
    String rp10 = "";
    String rp11 = "";
    String rp12 = "";
    if (roomplan1.size()!=0) {
      for (int i = 0; i < roomplan1.size(); i++) {
        rp1 = rp1 + "'" + roomplan1.get(i).get("teacher_id") + "',";
      }
      rp1 = "course1: [" + rp1.substring(0,rp1.length()-1) + "]";
    } else {
      rp1 = "course1: []";
    }
    if (roomplan2.size()!=0) {
      for (int i = 0; i < roomplan2.size(); i++) {
        rp2 = rp2 + "'" + roomplan2.get(i).get("teacher_id") + "',";
      }
      rp2 = "course2: [" + rp2.substring(0,rp2.length()-1) + "]";
    } else {
      rp2 = "course2: []";
    }
    if (roomplan3.size()!=0) {
      for (int i = 0; i < roomplan3.size(); i++) {
        rp3 = rp3 + "'" + roomplan3.get(i).get("teacher_id") + "',";
      }
      rp3 = "course3: [" + rp3.substring(0,rp3.length()-1) + "]";
    } else {
      rp3 = "course3: []";
    }
    if (roomplan4.size()!=0) {
      for (int i = 0; i < roomplan4.size(); i++) {
        rp4 = rp4 + "'" + roomplan4.get(i).get("teacher_id") + "',";
      }
      rp4 = "course4: [" + rp4.substring(0,rp4.length()-1) + "]";
    } else {
      rp4 = "course4: []";
    }
    if (roomplan5.size()!=0) {
      for (int i = 0; i < roomplan5.size(); i++) {
        rp5 = rp5 + "'" + roomplan5.get(i).get("teacher_id") + "',";
      }
      rp5 = "course5: [" + rp5.substring(0,rp5.length()-1) + "]";
    } else {
      rp5 = "course5: []";
    }
    if (roomplan6.size()!=0) {
      for (int i = 0; i < roomplan6.size(); i++) {
        rp6 = rp6 + "'" + roomplan6.get(i).get("teacher_id") + "',";
      }
      rp6 = "course6: [" + rp6.substring(0,rp6.length()-1) + "]";
    } else {
      rp6 = "course6: []";
    }
    if (roomplan7.size()!=0) {
      for (int i = 0; i < roomplan7.size(); i++) {
        rp7 = rp7 + "'" + roomplan7.get(i).get("teacher_id") + "',";
      }
      rp7 = "course7: [" + rp7.substring(0,rp7.length()-1) + "]";
    } else {
      rp7 = "course7: []";
    }
    if (roomplan8.size()!=0) {
      for (int i = 0; i < roomplan8.size(); i++) {
        rp8 = rp8 + "'" + roomplan8.get(i).get("teacher_id") + "',";
      }
      rp8 = "course8: [" + rp8.substring(0,rp8.length()-1) + "]";
    } else {
      rp8 = "course8: []";
    }
    if (roomplan9.size()!=0) {
      for (int i = 0; i < roomplan9.size(); i++) {
        rp9 = rp9 + "'" + roomplan9.get(i).get("teacher_id") + "',";
      }
      rp9 = "course9: [" + rp9.substring(0,rp9.length()-1) + "]";
    } else {
      rp9 = "course9: []";
    }
    if (roomplan10.size()!=0) {
      for (int i = 0; i < roomplan10.size(); i++) {
        rp10 = rp10 + "'" + roomplan10.get(i).get("teacher_id") + "',";
      }
      rp10 = "course10: [" + rp10.substring(0,rp10.length()-1) + "]";
    } else {
      rp10 = "course10: []";
    }
    if (roomplan11.size()!=0) {
      for (int i = 0; i < roomplan11.size(); i++) {
        rp11 = rp11 + "'" + roomplan11.get(i).get("teacher_id") + "',";
      }
      rp11 = "course11: [" + rp11.substring(0,rp11.length()-1) + "]";
    } else {
      rp11 = "course11: []";
    }
    if (roomplan12.size()!=0) {
      for (int i = 0; i < roomplan12.size(); i++) {
        rp12 = rp12 + "'" + roomplan12.get(i).get("teacher_id") + "',";
      }
      rp12 = "course12: [" + rp12.substring(0,rp12.length()-1) + "]";
    } else {
      rp12 = "course12: []";
    }
    renderText("{"+rp1+","+rp2+","+rp3+","+rp4+","+rp5+","+rp6+","+rp7+","+rp8+","+rp9+","+rp10+","+rp11+","+rp12+"}");
  }
  @Before({Tx.class,AjaxManager.class})
  public void save()  {
//    if (!getPara("name").matches("\\d{4}[\\u7ea7]\\d{1,2}[\\u73ed]")) {
//      renderText("班级名称格式应为：XXXX级XX班");
    if (Room.dao.find("select * from room where name=?", getPara("name")).size()!=0) {
      renderText("该班级已存在!");
    } else {
      Room room = new Room();
      room.set("name",getPara("name")).set("state",1).save();
      String[] course1 = getParaValues("course1[]");
      String[] course2 = getParaValues("course2[]");
      String[] course3 = getParaValues("course3[]");
      String[] course4 = getParaValues("course4[]");
      String[] course5 = getParaValues("course5[]");
      String[] course6 = getParaValues("course6[]");
      String[] course7 = getParaValues("course7[]");
      String[] course8 = getParaValues("course8[]");
      String[] course9 = getParaValues("course9[]");
      String[] course10 = getParaValues("course10[]");
      String[] course11 = getParaValues("course11[]");
      String[] course12 = getParaValues("course12[]");
      if (course1!=null){
        for (String i : course1){
          Roomplan roomplan = new Roomplan();
          roomplan.set("course_id",1).set("room_id",room.get("id")).set("teacher_id",i).save();
          Enterprise teacher = Enterprise.dao.findById(i);
          if (teacher.getState()==1){
            try {
              WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的班主任"), new IdParameter().putUserIds(teacher.getUserId()), false));
            } catch (Exception e) {
              renderText(e.getMessage());
            }
          }
        }
      }
      if (course2!=null){
        for (String i : course2){
          Roomplan roomplan = new Roomplan();
          roomplan.set("course_id",2).set("room_id",room.get("id")).set("teacher_id",i).save();
          Enterprise teacher = Enterprise.dao.findById(i);
          if (teacher.getState()==1){
            try {
              WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的语文老师"), new IdParameter().putUserIds(teacher.getUserId()), false));
            } catch (Exception e) {
              renderText(e.getMessage());
            }
          }
        }
      }
      if (course3!=null){
        for (String i : course3){
          Roomplan roomplan = new Roomplan();
          roomplan.set("course_id",3).set("room_id",room.get("id")).set("teacher_id",i).save();
          Enterprise teacher = Enterprise.dao.findById(i);
          if (teacher.getState()==1){
            try {
              WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的数学老师"), new IdParameter().putUserIds(teacher.getUserId()), false));
            } catch (Exception e) {
              renderText(e.getMessage());
            }
          }
        }
      }
      if (course4!=null){
        for (String i : course4){
          Roomplan roomplan = new Roomplan();
          roomplan.set("course_id",4).set("room_id",room.get("id")).set("teacher_id",i).save();
          Enterprise teacher = Enterprise.dao.findById(i);
          if (teacher.getState()==1){
            try {
              WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的英语老师"), new IdParameter().putUserIds(teacher.getUserId()), false));
            } catch (Exception e) {
              renderText(e.getMessage());
            }
          }
        }
      }
      if (course5!=null){
        for (String i : course5){
          Roomplan roomplan = new Roomplan();
          roomplan.set("course_id",5).set("room_id",room.get("id")).set("teacher_id",i).save();
          Enterprise teacher = Enterprise.dao.findById(i);
          if (teacher.getState()==1){
            try {
              WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的音乐老师"), new IdParameter().putUserIds(teacher.getUserId()), false));
            } catch (Exception e) {
              renderText(e.getMessage());
            }
          }
        }
      }
      if (course6!=null){
        for (String i : course6){
          Roomplan roomplan = new Roomplan();
          roomplan.set("course_id",6).set("room_id",room.get("id")).set("teacher_id",i).save();
          Enterprise teacher = Enterprise.dao.findById(i);
          if (teacher.getState()==1){
            try {
              WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的体育老师"), new IdParameter().putUserIds(teacher.getUserId()), false));
            } catch (Exception e) {
              renderText(e.getMessage());
            }
          }
        }
      }
      if (course7!=null){
        for (String i : course7){
          Roomplan roomplan = new Roomplan();
          roomplan.set("course_id",7).set("room_id",room.get("id")).set("teacher_id",i).save();
          Enterprise teacher = Enterprise.dao.findById(i);
          if (teacher.getState()==1){
            try {
              WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的美术老师"), new IdParameter().putUserIds(teacher.getUserId()), false));
            } catch (Exception e) {
              renderText(e.getMessage());
            }
          }
        }
      }
      if (course8!=null){
        for (String i : course8){
          Roomplan roomplan = new Roomplan();
          roomplan.set("course_id",8).set("room_id",room.get("id")).set("teacher_id",i).save();
          Enterprise teacher = Enterprise.dao.findById(i);
          if (teacher.getState()==1){
            try {
              WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的科学老师"), new IdParameter().putUserIds(teacher.getUserId()), false));
            } catch (Exception e) {
              renderText(e.getMessage());
            }
          }
        }
      }
      if (course9!=null){
        for (String i : course9){
          Roomplan roomplan = new Roomplan();
          roomplan.set("course_id",9).set("room_id",room.get("id")).set("teacher_id",i).save();
          Enterprise teacher = Enterprise.dao.findById(i);
          if (teacher.getState()==1){
            try {
              WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的计算机老师"), new IdParameter().putUserIds(teacher.getUserId()), false));
            } catch (Exception e) {
              renderText(e.getMessage());
            }
          }
        }
      }
      if (course10!=null){
        for (String i : course10){
          Roomplan roomplan = new Roomplan();
          roomplan.set("course_id",10).set("room_id",room.get("id")).set("teacher_id",i).save();
          Enterprise teacher = Enterprise.dao.findById(i);
          if (teacher.getState()==1){
            try {
              WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的品生老师"), new IdParameter().putUserIds(teacher.getUserId()), false));
            } catch (Exception e) {
              renderText(e.getMessage());
            }
          }
        }
      }
      if (course11!=null){
        for (String i : course11){
          Roomplan roomplan = new Roomplan();
          roomplan.set("course_id",11).set("room_id",room.get("id")).set("teacher_id",i).save();
          Enterprise teacher = Enterprise.dao.findById(i);
          if (teacher.getState()==1){
            try {
              WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的品社老师"), new IdParameter().putUserIds(teacher.getUserId()), false));
            } catch (Exception e) {
              renderText(e.getMessage());
            }
          }
        }
      }
      if (course12!=null){
        for (String i : course12){
          Roomplan roomplan = new Roomplan();
          roomplan.set("course_id",12).set("room_id",room.get("id")).set("teacher_id",i).save();
          Enterprise teacher = Enterprise.dao.findById(i);
          if (teacher.getState()==1){
            try {
              WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的传统文化老师"), new IdParameter().putUserIds(teacher.getUserId()), false));
            } catch (Exception e) {
              renderText(e.getMessage());
            }
          }
        }
      }
      renderText("OK");
    }
  }
  @Before({Tx.class,AjaxManager.class})
  public void edit()  {
    Room room = Room.dao.findById(getPara("id"));
    if (room == null) {
      renderText("要修改的班级不存在!");
    } else {
//      if (Util.getString(room.getStr("name")).equals(getPara("name").trim())) {
//        renderText("未找到修改内容!");
//      } else if (!getPara("name").matches("\\d{4}[\\u7ea7]\\d{1,2}[\\u73ed]")) {
//        renderText("班级名称格式应为：XXXX级XX班");
      if (!Util.getString(room.getStr("name")).equals(getPara("name"))
              &&  Room.dao.find("select * from room where name=?", getPara("name")).size()!=0) {
        renderText("该班级已存在!");
      } else {
        room.set("name",getPara("name")).update();
        Db.update("delete from roomplan where room_id = ?", getPara("id"));
        String[] course1 = getParaValues("course1[]");
        String[] course2 = getParaValues("course2[]");
        String[] course3 = getParaValues("course3[]");
        String[] course4 = getParaValues("course4[]");
        String[] course5 = getParaValues("course5[]");
        String[] course6 = getParaValues("course6[]");
        String[] course7 = getParaValues("course7[]");
        String[] course8 = getParaValues("course8[]");
        String[] course9 = getParaValues("course9[]");
        String[] course10 = getParaValues("course10[]");
        String[] course11 = getParaValues("course11[]");
        String[] course12 = getParaValues("course12[]");
        if (course1!=null){
          for (String i : course1){
            Roomplan roomplan = new Roomplan();
            roomplan.set("course_id",1).set("room_id",room.get("id")).set("teacher_id",i).save();
            Enterprise teacher = Enterprise.dao.findById(i);
            if (teacher.getState()==1){
              try {
                WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的班主任"), new IdParameter().putUserIds(teacher.getUserId()), false));
              } catch (Exception e) {
                renderText(e.getMessage());
              }
            }
          }
        }
        if (course2!=null){
          for (String i : course2){
            Roomplan roomplan = new Roomplan();
            roomplan.set("course_id",2).set("room_id",room.get("id")).set("teacher_id",i).save();
            Enterprise teacher = Enterprise.dao.findById(i);
            if (teacher.getState()==1){
              try {
                WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的语文老师"), new IdParameter().putUserIds(teacher.getUserId()), false));
              } catch (Exception e) {
                renderText(e.getMessage());
              }
            }
          }
        }
        if (course3!=null){
          for (String i : course3){
            Roomplan roomplan = new Roomplan();
            roomplan.set("course_id",3).set("room_id",room.get("id")).set("teacher_id",i).save();
            Enterprise teacher = Enterprise.dao.findById(i);
            if (teacher.getState()==1){
              try {
                WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的数学老师"), new IdParameter().putUserIds(teacher.getUserId()), false));
              } catch (Exception e) {
                renderText(e.getMessage());
              }
            }
          }
        }
        if (course4!=null){
          for (String i : course4){
            Roomplan roomplan = new Roomplan();
            roomplan.set("course_id",4).set("room_id",room.get("id")).set("teacher_id",i).save();
            Enterprise teacher = Enterprise.dao.findById(i);
            if (teacher.getState()==1){
              try {
                WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的英语老师"), new IdParameter().putUserIds(teacher.getUserId()), false));
              } catch (Exception e) {
                renderText(e.getMessage());
              }
            }
          }
        }
        if (course5!=null){
          for (String i : course5){
            Roomplan roomplan = new Roomplan();
            roomplan.set("course_id",5).set("room_id",room.get("id")).set("teacher_id",i).save();
            Enterprise teacher = Enterprise.dao.findById(i);
            if (teacher.getState()==1){
              try {
                WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的音乐老师"), new IdParameter().putUserIds(teacher.getUserId()), false));
              } catch (Exception e) {
                renderText(e.getMessage());
              }
            }
          }
        }
        if (course6!=null){
          for (String i : course6){
            Roomplan roomplan = new Roomplan();
            roomplan.set("course_id",6).set("room_id",room.get("id")).set("teacher_id",i).save();
            Enterprise teacher = Enterprise.dao.findById(i);
            if (teacher.getState()==1){
              try {
                WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的体育老师"), new IdParameter().putUserIds(teacher.getUserId()), false));
              } catch (Exception e) {
                renderText(e.getMessage());
              }
            }
          }
        }
        if (course7!=null){
          for (String i : course7){
            Roomplan roomplan = new Roomplan();
            roomplan.set("course_id",7).set("room_id",room.get("id")).set("teacher_id",i).save();
            Enterprise teacher = Enterprise.dao.findById(i);
            if (teacher.getState()==1){
              try {
                WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的美术老师"), new IdParameter().putUserIds(teacher.getUserId()), false));
              } catch (Exception e) {
                renderText(e.getMessage());
              }
            }
          }
        }
        if (course8!=null){
          for (String i : course8){
            Roomplan roomplan = new Roomplan();
            roomplan.set("course_id",8).set("room_id",room.get("id")).set("teacher_id",i).save();
            Enterprise teacher = Enterprise.dao.findById(i);
            if (teacher.getState()==1){
              try {
                WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的科学老师"), new IdParameter().putUserIds(teacher.getUserId()), false));
              } catch (Exception e) {
                renderText(e.getMessage());
              }
            }
          }
        }
        if (course9!=null){
          for (String i : course9){
            Roomplan roomplan = new Roomplan();
            roomplan.set("course_id",9).set("room_id",room.get("id")).set("teacher_id",i).save();
            Enterprise teacher = Enterprise.dao.findById(i);
            if (teacher.getState()==1){
              try {
                WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的计算机老师"), new IdParameter().putUserIds(teacher.getUserId()), false));
              } catch (Exception e) {
                renderText(e.getMessage());
              }
            }
          }
        }
        if (course10!=null){
          for (String i : course10){
            Roomplan roomplan = new Roomplan();
            roomplan.set("course_id",10).set("room_id",room.get("id")).set("teacher_id",i).save();
            Enterprise teacher = Enterprise.dao.findById(i);
            if (teacher.getState()==1){
              try {
                WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的品生老师"), new IdParameter().putUserIds(teacher.getUserId()), false));
              } catch (Exception e) {
                renderText(e.getMessage());
              }
            }
          }
        }
        if (course11!=null){
          for (String i : course11){
            Roomplan roomplan = new Roomplan();
            roomplan.set("course_id",11).set("room_id",room.get("id")).set("teacher_id",i).save();
            Enterprise teacher = Enterprise.dao.findById(i);
            if (teacher.getState()==1){
              try {
                WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的品社老师"), new IdParameter().putUserIds(teacher.getUserId()), false));
              } catch (Exception e) {
                renderText(e.getMessage());
              }
            }
          }
        }
        if (course12!=null){
          for (String i : course12){
            Roomplan roomplan = new Roomplan();
            roomplan.set("course_id",12).set("room_id",room.get("id")).set("teacher_id",i).save();
            Enterprise teacher = Enterprise.dao.findById(i);
            if (teacher.getState()==1){
              try {
                WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+room.getName()+"的传统文化老师"), new IdParameter().putUserIds(teacher.getUserId()), false));
              } catch (Exception e) {
                renderText(e.getMessage());
              }
            }
          }
        }
        renderText("OK");
      }
    }
  }
}
