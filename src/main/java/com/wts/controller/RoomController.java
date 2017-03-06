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
import com.wts.interceptor.*;
import com.wts.util.ParamesAPI;
import com.wts.util.Util;

import java.util.List;

import static com.jfinal.plugin.activerecord.Db.find;

public class RoomController extends Controller {

  public void forManager() throws WeixinException {
    // 检测session中是否存在teacher
    if (getSessionAttr("manager") == null || ((Teacher) getSessionAttr("manager")).getIsManager() != 1) {
      // 检测cookie中是否存在EnterpriseId
      if (getCookie("die") == null || getCookie("die").equals("")) {
        // 检测是否来自微信请求
        if (!(getPara("code") == null || getPara("code").equals(""))) {
          User user = WP.me.getUserByCode(getPara("code"));
          Teacher teacher = Teacher.dao.findFirst(Teacher.dao.getSql("teacher.weixin_manager"), user.getUserId(), 1);
          // 检测是否有权限
          if (teacher != null) {
            setSessionAttr("manager", teacher);
            setCookie("die", teacher.getId().toString(), 60 * 30);
            render("/static/ManagerOfRoom.html");
          } else {
            redirect("/");
          }
        } else {
          redirect("/");
        }
      } else {
        Teacher teacher = Teacher.dao.findById(getCookie("die"));
        setSessionAttr("manager", teacher);
        render("/static/ManagerOfRoom.html");
      }
    } else {
      render("/static/ManagerOfRoom.html");
    }
  }
  @Before(AjaxManager.class)
  public void query() {
    renderJson(Room.dao.paginate(getParaToInt("pageCurrent"), getParaToInt("pageSize"), "SELECT *", "FROM room WHERE year LIKE '%?%' OR order LIKE '%?%' OR slogan LIKE '%?%' ORDER BY id ASC", getPara("queryString"), getPara("queryString"), getPara("queryString")).getList());

  }
  @Before(AjaxManager.class)
  public void total() {
    Long count = Db.queryLong("select count(*) from room where year like '%?%' OR order LIKE '%?%' OR slogan LIKE '%?%'", getPara("queryString"), getPara("queryString"), getPara("queryString"));
    if (count%getParaToInt("pageSize")==0) {
      renderText((count/getParaToInt("pageSize"))+"");
    } else {
      renderText((count/getParaToInt("pageSize")+1)+"");
    }
  }
  @Before(AjaxManager.class)
  public void checkSloganForNew() {
    if (Room.dao.find(Room.dao.getSql("room.slogan"),getPara("slogan")).size()!=0) {
      renderText("该班级标语已存在!");
    } else {
      renderText("OK");
    }
  }
  @Before(AjaxManager.class)
  public void checkSloganForEdit() {
    if (!Room.dao.findById(getPara("id")).getSlogan().equals(getPara("slogan"))
            && Room.dao.find(Room.dao.getSql("room.slogan"),getPara("slogan")).size()!=0) {
      renderText("该班级标语已存在!");
    } else {
      renderText("OK");
    }
  }
  @Before(AjaxManager.class)
  public void checkRoom() {
    if (Room.dao.find(Room.dao.getSql("room.year_order"),getPara("year"),getPara("order")).size()!=0) {
      renderText("该班级已存在!");
    } else {
      renderText("OK");
    }
  }
  @Before(AjaxManager.class)
  public void get() {
    renderJson(Room.dao.findById(getPara("id")));
  }
  @Before({Tx.class,AjaxManager.class})
  public void inactive() {
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
  public void active() {
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
  public void list() {
    renderJson(Room.dao.find(Room.dao.getSql("room.list")));
  }
  @Before(AjaxParent.class)
  public void listOfParent() {
    String SQL = "SELECT DISTINCT room.id, room.name, room.state" +
            " FROM ((roomstudent" +
            " LEFT JOIN room ON room.id = roomstudent.room_id)" +
            " LEFT JOIN student ON student.id = roomstudent.student_id)" +
            " WHERE room.state = 1 AND student.state = 1 AND student.id" +
            " IN (SELECT DISTINCT student_id FROM relation WHERE parent_id = ?)";
    renderJson(Db.find(SQL,((Parent) getSessionAttr("parent")).getId()));
  }
  @Before(AjaxParent.class)
  public void firstOfParent() {
    String SQL = "SELECT DISTINCT room.id, room.name, room.state" +
            " FROM ((roomstudent" +
            " LEFT JOIN room ON room.id = roomstudent.room_id)" +
            " LEFT JOIN student ON student.id = roomstudent.student_id)" +
            " WHERE room.state = 1 AND student.state = 1 AND student.id" +
            " IN (SELECT DISTINCT student_id FROM relation WHERE parent_id = ?)";
    Record record = Db.findFirst(SQL,((Parent) getSessionAttr("parent")).getId());
    if (record!=null){
      renderText(record.get("id").toString());
    }else{
      renderText("0");
    }
  }
  @Before(AjaxTeacher.class)
  public void listOfTeacher() {
    String SQL = "SELECT DISTINCT room.id, room.name, room.state" +
            " FROM (((courseroomteacher" +
            " LEFT JOIN room ON room.id = courseroomteacher.room_id)" +
            " LEFT JOIN teacher ON teacher.id = courseroomteacher.teacher_id)" +
            " LEFT JOIN semester ON semester.id = courseroomteacher.semester_id)" +
            " WHERE room.state = 1 AND teacher.id = ? AND semester.id = ?";
    renderJson(Db.find(SQL,((Teacher) getSessionAttr("teacher")).getId(),Semester.dao.findFirst(Semester.dao.getSql("semester.used")).getId()));
  }
  @Before(AjaxTeacher.class)
  public void firstOfTeacher() {
    String SQL = "SELECT DISTINCT room.id, room.name, room.state" +
            " FROM (((courseroomteacher" +
            " LEFT JOIN room ON room.id = courseroomteacher.room_id)" +
            " LEFT JOIN teacher ON teacher.id = courseroomteacher.teacher_id)" +
            " LEFT JOIN semester ON semester.id = courseroomteacher.semester_id)" +
            " WHERE room.state = 1 AND teacher.id = ? AND semester.id = ?";
    Record record = Db.findFirst(SQL,((Teacher) getSessionAttr("teacher")).getId(),Semester.dao.findFirst(Semester.dao.getSql("semester.used")).getId());
    if (record!=null){
        renderText(record.get("id").toString());
    }else{
        renderText("0");
    }
  }
  @Before(AjaxTeacher.class)
  public void roomCourseList() {
    String SQL = "SELECT DISTINCT course.name,courseroomteacher.course_id" +
            "FROM courseroomteacher" +
            "LEFT JOIN course ON courseroomteacher.course_id = course.id" +
            "WHERE courseroomteacher.teacher_id = ? AND courseroomteacher.semester_id = ? AND courseroomteacher.room_id = ?";
    renderJson(Db.find(SQL,((Teacher) getSessionAttr("teacher")).getId(),Semester.dao.findFirst(Semester.dao.getSql("semester.used")).getId(),getPara("id")));
  }
  @Before(AjaxTeacher.class)
  public void roomCourseFirst() {
    String SQL = "SELECT DISTINCT course.name,courseroomteacher.course_id" +
            "FROM courseroomteacher" +
            "LEFT JOIN course ON courseroomteacher.course_id = course.id" +
            "WHERE courseroomteacher.teacher_id = ? AND courseroomteacher.semester_id = ? AND courseroomteacher.room_id = ?";
    Record record = Db.findFirst(SQL,((Teacher) getSessionAttr("teacher")).getId(),Semester.dao.findFirst(Semester.dao.getSql("semester.used")).getId(),getPara("id"));
    if (record!=null){
      renderText(record.get("id").toString());
    }else{
      renderText("0");
    }
  }
  @Before({Login.class, Ajax.class})
  public void getName() {
    renderText(Room.dao.findById(getPara("id")).get("name").toString());
  }

  public void getCourseRoomTeachers() {
    List<Courseroomteacher> courseroomteacher = Courseroomteacher.dao.find("select * from courseroomteacher where room_id=? and course_id=? and semester_id=?",getPara("room"),getPara("course"),Semester.dao.findFirst(Semester.dao.getSql("semester.used")).getId());
    if (courseroomteacher.size()!=0) {
      String sp1 = "";
      for (int i = 0; i < courseroomteacher.size(); i++) {
        sp1 = sp1 + "'" + courseroomteacher.get(i).get("teacher_id") + "',";
      }
      renderText("{course: [" + sp1.substring(0,sp1.length()-1) + "]}");
    } else {
      renderText("{}");
    }
  }
  private String crt (String courseId,String roomId){
    List<Courseroomteacher> courseroomteacher = Courseroomteacher.dao.find("select * from courseroomteacher where room_id=? and course_id=? and semester_id=?",roomId,courseId,Semester.dao.findFirst(Semester.dao.getSql("semester.used")).getId());
    String crt = "";
    if (courseroomteacher.size()!=0) {
      for (int i = 0; i < courseroomteacher.size(); i++) {
        crt = crt + "'" + courseroomteacher.get(i).get("teacher_id") + "',";
      }
      crt = "course"+courseId+": [" + crt.substring(0,crt.length()-1) + "]";
    } else {
      crt = "course"+courseId+":: []";
    }
    return crt;
  }

  @Before(AjaxManager.class)
  public void getCourseRoomTeacher() {
    List<Course> courses = Course.dao.find("SELECT * FROM course WHERE state = 1");
    String crt = "";
    if (courses.size()!=0) {
      for (int i = 0; i < courses.size(); i++) {
        crt = crt + crt(courses.get(i).get("id").toString(),getPara("roomId")) + ",";
      }
    }
    renderText("{"+crt.substring(0,crt.length()-1) + "}");
  }

  @Before({Tx.class,AjaxManager.class})
  public void save()  {
    if (!getPara("year").matches("\\d{4}")) {
      renderText("入学年份应为4位数字");
    } else if (!getPara("order").matches("\\d{1,2}")) {
      renderText("班级序号应为1-2位数字");
    } else if (Room.dao.find("SELECT * FROM room WHERE year=? AND order=?", getPara("year"), getPara("order")).size()!=0) {
      renderText("该班级已存在!");
    } else if (Room.dao.find("SELECT * FROM room WHERE slogan=?", getPara("slogan")).size()!=0) {
      renderText("该班级标语已存在!");
    } else if (Room.dao.find("SELECT * FROM room WHERE name=?", getPara("year")+"级"+getPara("order")+"班").size()!=0) {
      renderText("该班级标签已存在!");
    } else {
      Room room = new Room();
      room.set("name",getPara("year")+"级"+getPara("order")+"班")
              .set("year",getPara("year"))
              .set("order",getPara("order"))
              .set("slogan",getPara("slogan"))
              .set("state",1).save();
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
      if (!getPara("name").matches("\\d{4}[\\u7ea7]\\d{1,2}[\\u73ed]")) {
        renderText("班级名称格式应为：XXXX级XX班");
      } else if (!Util.getString(room.getStr("name")).equals(getPara("name"))
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
