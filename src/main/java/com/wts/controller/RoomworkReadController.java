package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.WP;
import com.wts.entity.model.Enterprise;
import com.wts.entity.model.Roomworkread;
import com.wts.interceptor.AjaxParent;

import java.util.Date;

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
}
