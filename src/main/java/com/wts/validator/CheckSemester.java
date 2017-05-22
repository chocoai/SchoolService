package com.wts.validator;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.wts.entity.model.Semester;

public class CheckSemester implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("sid"))
            ) {
      String sid = Semester.dao.findFirst("SELECT * FROM semester WHERE state = 1").getId().toString();
      if (sid.equals(inv.getController().getPara("sid"))) {
        inv.invoke();
      }else{
        inv.getController().renderText("无法删除非当前学期的信息!");
      }
    } else {
      inv.getController().renderText("缺少参数!");
    }
  }
}
