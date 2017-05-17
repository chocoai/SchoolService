package com.wts.validator.Course;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.wts.entity.model.Course;

public class Course_Exist implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("id"))) {
      if (Course.dao.findById(inv.getController().getPara("id")) == null) {
        inv.getController().renderText("指定课程不存在!");
      } else {
        if (Course.dao.findById(inv.getController().getPara("id")).getDel()==0){
          inv.invoke();
        } else {
          inv.getController().renderText("指定课程已删除!");
        }
      }
    } else {
      inv.getController().renderText("缺少参数!");
    }
  }
}
