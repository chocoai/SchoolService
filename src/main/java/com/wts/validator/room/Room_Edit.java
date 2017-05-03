package com.wts.validator.room;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.wts.entity.model.Course;
import com.wts.entity.model.Room;

public class Room_Edit implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("year"))
            && !StrKit.isBlank(inv.getController().getPara("order"))
            && !StrKit.isBlank(inv.getController().getPara("state"))
            && !StrKit.isBlank(inv.getController().getPara("id"))
            ) {
      String year = inv.getController().getPara("year");
      String order = inv.getController().getPara("order");
      String state = inv.getController().getPara("state");
      String id = inv.getController().getPara("id");
      Room room = Room.dao.findById(id);
      if (!(room.getStr("year").equals(year) && room.getStr("order").equals(order))
              && Room.dao.find("SELECT * FROM room WHERE year = ? AND order = ?", year, order).size() != 0) {
        inv.getController().renderText("该名称已存在!");
      } else if(room.getStr("name").equals(year)
              && room.getStr("order").equals(order)
              && room.getStr("state").equals(state)
              ){
        inv.getController().renderText("未发现修改内容!");
      }else {
        inv.invoke();
      }
    } else {
      inv.getController().renderText("缺少参数!");
    }

  }
}
