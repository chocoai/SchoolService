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

import java.util.Date;
import java.util.List;

public class RoomworkReadController extends Controller {
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
    List<Record> students = Db.find("select student.`name` as sname,student.sex,identity.`name` as dname,enterprise.`name` as pname,roomworkread.time,count(distinct student.number)" +
            " from (((((student" +
            " left join relation on student.id = relation.student_id)" +
            " left join identity on identity.id = relation.parent_id)" +
            " left join enterprise on enterprise.id = relation.parent_id)" +
            " left join roomworkread on roomworkread.parent_id=relation.parent_id)" +
            " left join roomwork on roomworkread.roomwork_id = roomwork.id)" +
            " where roomwork.id=? and roomworkread.state=?",roomworkread.getRoomworkId(),getPara("state"));
    if (!students.get(0).get("count(distinct student.number)").toString().equals("0")){
      renderJson(students);
    }else{
      renderText("null");
    }
  }
}
