package com.wts.validator.roomStudent;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.wts.entity.model.Roomstudent;

public class RoomStudent_Exist implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("rid"))
            && !StrKit.isBlank(inv.getController().getPara("sid"))
            ) {
      if (Roomstudent.dao.findById(inv.getController().getPara("rid"), inv.getController().getPara("sid")) == null) {
        inv.getController().renderText("指定关系不存在!");
      } else {
        inv.invoke();
      }
    } else {
      inv.getController().renderText("缺少参数!");
    }
  }
}
