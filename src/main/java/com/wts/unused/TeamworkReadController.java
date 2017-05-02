//package com.wts.controller;
//
//import com.foxinmy.weixin4j.exception.WeixinException;
//import com.foxinmy.weixin4j.qy.message.NotifyMessage;
//import com.foxinmy.weixin4j.qy.model.IdParameter;
//import com.foxinmy.weixin4j.qy.model.User;
//import com.foxinmy.weixin4j.tuple.Text;
//import com.jfinal.aop.Before;
//import com.jfinal.core.Controller;
//import com.jfinal.plugin.activerecord.Db;
//import com.jfinal.plugin.activerecord.Record;
//import com.jfinal.plugin.activerecord.tx.Tx;
//import com.wts.entity.WP;
//import com.wts.entity.model.*;
//import com.wts.interceptor.AjaxParent;
//import com.wts.util.ParamesAPI;
//
//import java.text.SimpleDateFormat;
//import java.util.Collections;
//import java.util.Date;
//import java.util.LinkedList;
//import java.util.List;
//
//public class TeamworkReadController extends Controller {
//  private static String BASIC = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+ ParamesAPI.corpId+"&redirect_uri=http%3a%2f%2f"+ParamesAPI.URL+"%2f"+"XXXXX"+"&response_type=code&scope=snsapi_base&state=1#wechat_redirect";
//  @Before(Tx.class)
//  public void readTeamwork()  throws WeixinException {
//    User user = WP.me.getUserByCode(getPara("code"));
//    Enterprise enterprise = Enterprise.dao.findFirst("select * from enterprise where state=1 and isTeacher=1 and userId=?", user.getUserId());
//    Teamworkread teamworkread = Teamworkread.dao.findFirst("select * from teamworkread where teamwork_id=? and parent_id=?",getPara("teamworkId"),enterprise.getId());
//    if (Teamwork.dao.findById(teamworkread.getTeamworkId()).getState() == 1) {
//      if (teamworkread.getState() == 0) {
//        teamworkread.set("state", 1)
//                .set("time", new Date())
//                .update();
//        setAttr("readMessage", "确认读取!");
//        render("/html/read.html");
//      } else {
//        setAttr("readMessage", "本消息您已经确认读取!");
//        render("/html/read.html");
//      }
//    } else {
//      setAttr("readMessage", "该消息已经失效，无需确认读取!");
//      render("/html/read.html");
//    }
//  }
//  @Before({Tx.class,AjaxParent.class})
//  public void readTeamworkById() {
//    Teamworkread teamworkread = Teamworkread.dao.findFirst("select * from teamworkread where teamwork_id=? and parent_id=?", getPara("id"), ((Enterprise) getSessionAttr("parent")).getId());
//    if (teamworkread != null) {
//      if (Teamwork.dao.findById(teamworkread.getTeamworkId()).getState() == 1) {
//        if (teamworkread.getState() == 0) {
//          teamworkread.set("state", 1)
//                  .set("time", new Date())
//                  .update();
//          renderText("OK");
//        } else {
//          renderText("该消息已确认读取!");
//        }
//      } else {
//        renderText("该消息已经失效，无需确认读取!");
//      }
//    } else {
//      renderText("您无权阅读此消息!");
//    }
//  }
//
//  @Before({Tx.class, AjaxParent.class})
//  public void readRoomworkStateById() {
//    Teamworkread teamworkread = Teamworkread.dao.findFirst("select * from teamworkread where teamwork_id=? and parent_id=?", getPara("id"), ((Enterprise) getSessionAttr("parent")).getId());
//    if (teamworkread != null) {
//      renderText(Teamwork.dao.findById(teamworkread.getTeamworkId()).getState().toString());
//    } else {
//      renderText("null");
//    }
//  }
//  public void getReadTeamwork(){
//    List<Record> students = Db.find("select student.`name` as sname,student.sex,student.number,identity.`name` as dname,teamworkread.time" +
//            " from (((teamworkread " +
//            " left join student on teamworkread.student_id=student.id)" +
//            " left join enterprise on teamworkread.parent_id=enterprise.id)" +
//            " left join identity on teamworkread.identity_id=identity.id)" +
//            " where teamworkread.teamwork_id=? and teamworkread.state=1",getPara("id"));
//    if (students.size()!=0){
//      renderJson(students);
//    }else{
//      renderText("null");
//    }
//  }
//  public void getUnreadTeamwork(){
//    List<Record> students = Db.find("select DISTINCT student.`name` as sname,student.sex,student.number,student.id as id" +
//            " from (((teamworkread " +
//            " left join student on teamworkread.student_id=student.id)" +
//            " left join enterprise on teamworkread.parent_id=enterprise.id)" +
//            " left join identity on teamworkread.identity_id=identity.id)" +
//            " where teamworkread.teamwork_id=? and teamworkread.state=0 and " +
//            " student.number not in (" +
//            " select DISTINCT student.number" +
//            " from (((teamworkread " +
//            " left join student on teamworkread.student_id=student.id)" +
//            " left join enterprise on teamworkread.parent_id=enterprise.id)" +
//            " left join identity on teamworkread.identity_id=identity.id)" +
//            " where teamworkread.teamwork_id=? and teamworkread.state=1)",getPara("id"),getPara("id"));
//    if (students.size()!=0){
//      renderJson(students);
//    }else{
//      renderText("null");
//    }
//  }
//  public void send() {
//    List<Record> teamworkread = Db.find("select DISTINCT student.`name` as sname,student.sex,student.number,student.id as id" +
//            " from (((teamworkread " +
//            " left join student on teamworkread.student_id=student.id)" +
//            " left join enterprise on teamworkread.parent_id=enterprise.id)" +
//            " left join identity on teamworkread.identity_id=identity.id)" +
//            " where teamworkread.teamwork_id=? and teamworkread.state=0 and " +
//            " student.number not in (" +
//            " select DISTINCT student.number" +
//            " from (((teamworkread " +
//            " left join student on teamworkread.student_id=student.id)" +
//            " left join enterprise on teamworkread.parent_id=enterprise.id)" +
//            " left join identity on teamworkread.identity_id=identity.id)" +
//            " where teamworkread.teamwork_id=? and teamworkread.state=1)",getPara("teamwork_id"),getPara("teamwork_id"));
//    Teamwork teamwork = Teamwork.dao.findById(getPara("teamwork_id"));
//    if (teamworkread.size() == 0) {
//      renderText("全部学生的家长都已阅读!");
//    } else if(teamwork.getState()==2) {
//      renderText("该消息已注销，无法追加发送!");
//    } else {
//      List<Relation> relations = Relation.dao.find("select * from relation where identity_id=999");
//      for (Record i : teamworkread) {
//        if (Relation.dao.find("select * from relation where student_id=?", i.get("id")).size() != 0) {
//          relations.addAll(Relation.dao.find("select * from relation where student_id=?", i.get("id")));
//        }
//      }
//      List<Relation> relationNew = new LinkedList<Relation>();
//      for(Relation s: relations){
//        if(Collections.frequency(relationNew, s) < 1) relationNew.add(s);
//      }
//      IdParameter idParameter = new IdParameter();
//      for (Relation relation :relationNew){
//        if (Enterprise.dao.findById(relation.getParentId()).getState()==1){
//          idParameter.putUserIds(Enterprise.dao.findById(relation.getParentId()).getUserId());
//        }
//      }
//      SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //格式化当前系统日期
//      String teamworkRead = BASIC.replaceAll("XXXXX","teamworkread%2freadTeamwork%3fteamworkId%3d"+teamwork.getId().toString());
//      StringBuffer buffer = new StringBuffer();
//      buffer.append("社团："+Team.dao.findById(teamwork.getTeamId()).getName()).append("\n");
//      buffer.append("教师："+Enterprise.dao.findById(teamwork.getTeacherId()).getName()).append("\n");
//      buffer.append("时间："+dateFm.format(teamwork.get("time"))).append("\n");
//      buffer.append("内容："+teamwork.getContent()).append("\n");
//      buffer.append("<a href=\""+teamworkRead+"\">确认已读点击这里</a>");
//      try {
//        WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.parentId, new Text(buffer.toString()), idParameter, false));
//      } catch (Exception e) {
//        renderText(e.getMessage());
//      }
//      renderText("OK");
//    }
//  }
//}
