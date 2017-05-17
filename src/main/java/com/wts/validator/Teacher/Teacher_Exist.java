package com.wts.validator.Teacher;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.wts.entity.model.Teacher;

public class Teacher_Exist implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("id"))) {
      if (Teacher.dao.findById(inv.getController().getPara("id")) == null) {
        inv.getController().renderText("指定教师不存在!");
      } else {
        if (Teacher.dao.findById(inv.getController().getPara("id")).getDel()==0){
          inv.invoke();
        } else {
          inv.getController().renderText("指定教师已删除!");
        }
      }
    } else {
      inv.getController().renderText("缺少参数!");
    }
  }
}
