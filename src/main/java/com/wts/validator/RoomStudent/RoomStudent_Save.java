package com.wts.validator.RoomStudent;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;

public class RoomStudent_Save implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("rid"))
            && !StrKit.isBlank(inv.getController().getPara("sid"))
            ) {
      String rid = inv.getController().getPara("rid");
      String sid = inv.getController().getPara("sid");
      if (Db.find("SELECT * FROM roomstudent WHERE room_id = ? AND student_id = ?", rid,sid).size() != 0) {
        inv.getController().renderText("该关系已存在!");
      } else {
        inv.invoke();
      }
    } else {
      inv.getController().renderText("缺少参数!");
    }
  }
}
