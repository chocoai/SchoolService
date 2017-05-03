package com.wts.validator.room;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.wts.entity.model.Room;

public class Room_Save implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("year"))
            && !StrKit.isBlank(inv.getController().getPara("order"))
            && !StrKit.isBlank(inv.getController().getPara("state"))
            ) {
      String year = inv.getController().getPara("year");
      String order = inv.getController().getPara("order");
      if (Room.dao.find("SELECT * FROM room WHERE `year` = ? AND `order` = ?", year, order).size() != 0) {
        inv.getController().renderText("该班级已存在!");
      } else if(Room.dao.find("SELECT * FROM room WHERE `name` = ?", year+"级"+order+"班").size() != 0){
        inv.getController().renderText("该班级名称已存在!");
      } else {
        inv.invoke();
      }
    } else {
      inv.getController().renderText("缺少参数!");
    }
  }
}
