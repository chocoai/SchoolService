package com.wts.validator.room;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.wts.entity.model.Room;

public class Room_Exist implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("id"))) {
      if (Room.dao.findById(inv.getController().getPara("id")) == null) {
        inv.getController().renderText("指定班级不存在!");
      } else {
        if (Room.dao.findById(inv.getController().getPara("id")).getDel()==0){
          inv.invoke();
        } else {
          inv.getController().renderText("指定班级已删除!");
        }
      }
    } else {
      inv.getController().renderText("缺少参数!");
    }
  }
}
