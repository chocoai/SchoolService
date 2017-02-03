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
import com.wts.util.ParamesAPI;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RoomworkReadController extends Controller {
  private static String BASIC = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+ ParamesAPI.corpId+"&redirect_uri=http%3a%2f%2f"+ParamesAPI.URL+"%2f"+"XXXXX"+"&response_type=code&scope=snsapi_base&state=1#wechat_redirect";

  @Before(Tx.class)
  public void readRoomwork()  throws WeixinException {
    User user = WP.me.getUserByCode(getPara("code"));
    Enterprise enterprise = Enterprise.dao.findFirst("select * from enterprise where state=1 and isTeacher=1 and userId=?", user.getUserId());
    Roomworkread roomworkread = Roomworkread.dao.findFirst("select * from roomworkread where roomwork_id=? and parent_id=?",getPara("roomworkId"),enterprise.getId());
    if (roomworkread.getState()==0){
      roomworkread.set("state",1)
              .set("time",new Date())
              .update();
      setAttr("readMessage","确认读取!");
      render("/html/read.html");
    } else {
      setAttr("readMessage","本消息您已经确认读取!");
      render("/html/read.html");
    }
  }
  public void getReadRoomwork(){
    Roomworkread roomworkread = Roomworkread.dao.findById(getPara("id"));
    List<Record> students = Db.find("select distinct student.`name` as sname,student.sex,identity.`name` as dname,enterprise.`name` as pname,roomworkread.time" +
            " from (((((student" +
            " left join relation on student.id = relation.student_id)" +
            " left join identity on identity.id = relation.parent_id)" +
            " left join enterprise on enterprise.id = relation.parent_id)" +
            " left join roomworkread on roomworkread.parent_id=relation.parent_id)" +
            " left join roomwork on roomworkread.roomwork_id = roomwork.id)" +
            " where roomwork.id=? and roomworkread.state=1",roomworkread.getRoomworkId());
    if (students.size()!=0){
      renderJson(students);
    }else{
      renderText("null");
    }
  }
  public void getUnreadRoomwork(){
    Roomworkread roomworkread = Roomworkread.dao.findById(getPara("id"));
    List<Record> students = Db.find("select student.`name` as sname,student.sex,count(distinct student.number)" +
            " from (((((student" +
            " left join relation on student.id = relation.student_id)" +
            " left join identity on identity.id = relation.parent_id)" +
            " left join enterprise on enterprise.id = relation.parent_id)" +
            " left join roomworkread on roomworkread.parent_id=relation.parent_id)" +
            " left join roomwork on roomworkread.roomwork_id = roomwork.id)" +
            " where roomwork.id=? and roomworkread.state=0",roomworkread.getRoomworkId());
    if (!students.get(0).get("count(distinct student.number)").toString().equals("0")){
      renderJson(students);
    }else{
      renderText("null");
    }
  }
  public void send() {
    Roomworkread roomworkread = Roomworkread.dao.findById(getPara("id"));
    Roomwork roomwork = Roomwork.dao.findById(roomworkread.getId());
    List<Student> students = Student.dao.find("select distinct student.id" +
            " from (((((student" +
            " left join relation on student.id = relation.student_id)" +
            " left join identity on identity.id = relation.parent_id)" +
            " left join enterprise on enterprise.id = relation.parent_id)" +
            " left join roomworkread on roomworkread.parent_id=relation.parent_id)" +
            " left join roomwork on roomworkread.roomwork_id = roomwork.id)" +
            " where roomwork.id=? and roomworkread.state=0", roomworkread.getRoomworkId());
    if (students.size() == 1 && students.get(0).get("count(distinct student.number)").toString().equals("0")) {
      renderText("无学生");
    } else if(roomwork.getState()==2) {
      renderText("该消息已注销，无法追加发送!");
    } else {
      List<Relation> relations = Relation.dao.find("select parent_id from relation where identity_id=999");
      for (Student i : students) {
        if (Relation.dao.find("select parent_id from relation where student_id=?", i.get("id")).size() != 0) {
          relations.addAll(Relation.dao.find("select parent_id from relation where student_id=?", i.get("id")));
        }
      }
      List<Relation> relationNew = new LinkedList<Relation>();
      for(Relation s: relations){
        if(Collections.frequency(relationNew, s) < 1) relationNew.add(s);
      }
      IdParameter idParameter = new IdParameter();
      for (Relation relation :relationNew){
        if (Enterprise.dao.findById(relation.getParentId()).getState()==1){
          idParameter.putUserIds(Enterprise.dao.findById(relation.getParentId()).getUserId());
        }
      }
      SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //格式化当前系统日期
      String roomworkRead = BASIC.replaceAll("XXXXX","roomworkread%2freadRoomwork%3froomworkId%3d"+roomwork.getId().toString());
      StringBuffer buffer = new StringBuffer();
      buffer.append("班级："+Room.dao.findById(roomwork.getRoomId()).getName()).append("\n");
      buffer.append("类型："+Course.dao.findById(roomwork.getCourseId()).getName()).append("\n");
      buffer.append("教师："+Enterprise.dao.findById(roomwork.getTeacherId())).append("\n");
      buffer.append("时间："+dateFm.format(roomwork.get("time"))).append("\n");
      buffer.append("内容："+roomwork.getContent()).append("\n");
      buffer.append("<a href=\""+roomworkRead+"\">确认已读点击这里</a>");
      try {
        WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.parentId, new Text(buffer.toString()), idParameter, false));
      } catch (Exception e) {
        renderText(e.getMessage());
      }
      renderText("OK");
    }
  }
}
