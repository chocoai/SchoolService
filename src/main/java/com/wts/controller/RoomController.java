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
        Teacher teacher = Teacher.dao.findFirst("select * from teacher where state=1 and weixinId=?", user.getUserId());
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
  public void checkNameForNew() {
    if (getPara("name")!=null) {
      if (!getPara("name").matches("\\d{4}[\\u7ea7]\\d{1,2}[\\u73ed]")) {
        // 班级名称不合规格
        renderText("1");
      } else if (Teacher.dao.find("select * from room where name=?", getPara("name")).size()!=0) {
        // 班级名称冲突
        renderText("2");
      } else {
        renderText("0");
      }
    } else {
      renderText("Z");
    }
  }
  public void getById() {
    Room room  =Room.dao.findById(getPara("id"));
    renderJson(room);
  }

  public void deleteById() {
    if (getPara("id") != null) {
      if (Room.dao.findById(getPara("id")) == null) {
        // 未找到指定id的教师
        renderText("1");
      } else {
        Room.dao.deleteById(getPara("id"));
      }
      renderText("0");
    } else {
      renderText("Z");
    }
  }
  public void teacherList() {
    Teacher.dao.find("select * from teacher");
  }
}
