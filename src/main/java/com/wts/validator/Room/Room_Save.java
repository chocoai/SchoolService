package com.wts.validator.Room;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;

public class Room_Save implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("year"))
            && !StrKit.isBlank(inv.getController().getPara("order"))
            && !StrKit.isBlank(inv.getController().getPara("state"))
            ) {
      String year = inv.getController().getPara("year");
      String order = inv.getController().getPara("order");
      if (Db.find("SELECT * FROM room WHERE `year` = ? AND `order` = ?", year, order).size() != 0) {
        inv.getController().renderText("该班级已存在!");
      } else if(Db.find("SELECT * FROM room WHERE `name` = ?", year+"级"+order+"班").size() != 0){
        inv.getController().renderText("该班级名称已存在!");
      } else {
        inv.invoke();
      }
    } else {
      inv.getController().renderText("缺少参数!");
    }
  }
}
