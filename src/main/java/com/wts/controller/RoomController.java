package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.wts.entity.WP;
import com.wts.entity.model.Enterprise;
import com.wts.entity.model.Room;
import com.wts.entity.model.Teacher;

public class RoomController extends Controller {

  public void index() throws WeixinException {
    if (getSessionAttr("teacher") == null) {
      if (!(getPara("code") == null || getPara("code").equals(""))) {
        User user = WP.me.getUserByCode(getPara("code"));
        Teacher teacher = Teacher.dao.findById(Enterprise.dao.findFirst("select * from enterprise where state=1 and userId=?", user.getUserId()).getTeacherId());
        setSessionAttr("teacher", teacher);
        render("/static/RoomManage.html");
      } else {
        redirect("/");
      }
    } else {
      render("/static/RoomManage.html");
    }
  }
  public void queryByName() {
    Page<Room> rooms= Room.dao.queryByName(getParaToInt("pageCurrent"),getParaToInt("pageSize"),getPara("queryString"));
    renderJson(rooms.getList());
  }
  public void totalByName() {
    Long count = Db.queryLong("select count(*) from room where name like '%"+ getPara("queryString") +"%'");
    if (count%getParaToInt("pageSize")==0) {
      renderText((count/getParaToInt("pageSize"))+"");
    } else {
      renderText((count/getParaToInt("pageSize")+1)+"");
    }
  }

}
