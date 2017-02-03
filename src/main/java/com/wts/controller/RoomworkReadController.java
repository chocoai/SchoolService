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
    List<Record> students = Db.find("select student.`name` as sname,student.sex,student.number,identity.`name` as dname,roomworkread.time" +
            " from (((roomworkread " +
            " left join student on roomworkread.student_id=student.id)" +
            " left join enterprise on roomworkread.parent_id=enterprise.id)" +
            " left join identity on roomworkread.identity_id=identity.id)" +
            " where roomworkread.roomwork_id=? and roomworkread.state=1",getPara("id"));
    if (students.size()!=0){
      renderJson(students);
    }else{
      renderText("null");
    }
  }
  public void getUnreadRoomwork(){
    List<Record> students = Db.find("select DISTINCT student.`name` as sname,student.sex,student.number,student.id as id" +
            " from (((roomworkread " +
            " left join student on roomworkread.student_id=student.id)" +
            " left join enterprise on roomworkread.parent_id=enterprise.id)" +
            " left join identity on roomworkread.identity_id=identity.id)" +
            " where roomworkread.roomwork_id=? and roomworkread.state=0 and " +
            " student.number not in (" +
            " select DISTINCT student.number" +
            " from (((roomworkread " +
            " left join student on roomworkread.student_id=student.id)" +
            " left join enterprise on roomworkread.parent_id=enterprise.id)" +
            " left join identity on roomworkread.identity_id=identity.id)" +
            " where roomworkread.roomwork_id=? and roomworkread.state=1)",getPara("id"),getPara("id"));
    if (students.size()!=0){
      renderJson(students);
    }else{
      renderText("null");
    }
  }
  public void send() {
    List<Record> roomworkread = Db.find("select DISTINCT student.`name` as sname,student.sex,student.number,student.id as id" +
            " from (((roomworkread " +
            " left join student on roomworkread.student_id=student.id)" +
            " left join enterprise on roomworkread.parent_id=enterprise.id)" +
            " left join identity on roomworkread.identity_id=identity.id)" +
            " where roomworkread.roomwork_id=? and roomworkread.state=0 and " +
            " student.number not in (" +
            " select DISTINCT student.number" +
            " from (((roomworkread " +
            " left join student on roomworkread.student_id=student.id)" +
            " left join enterprise on roomworkread.parent_id=enterprise.id)" +
            " left join identity on roomworkread.identity_id=identity.id)" +
            " where roomworkread.roomwork_id=? and roomworkread.state=1)",getPara("roomwork_id"),getPara("roomwork_id"));
    Roomwork roomwork = Roomwork.dao.findById(getPara("roomwork_id"));
    if (roomworkread.size() == 0) {
      renderText("全部学生的家长都已阅读!");
    } else if(roomwork.getState()==2) {
      renderText("该消息已注销，无法追加发送!");
    } else {
      List<Relation> relations = Relation.dao.find("select * from relation where identity_id=999");
      for (Record i : roomworkread) {
        if (Relation.dao.find("select * from relation where student_id=?", i.get("id")).size() != 0) {
          relations.addAll(Relation.dao.find("select * from relation where student_id=?", i.get("id")));
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
      buffer.append("教师："+Enterprise.dao.findById(roomwork.getTeacherId()).getName()).append("\n");
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
