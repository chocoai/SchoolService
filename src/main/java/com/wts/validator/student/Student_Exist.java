package com.wts.validator.student;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.wts.entity.model.Student;

public class Student_Exist implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("id"))) {
      if (Student.dao.findById(inv.getController().getPara("id")) == null) {
        inv.getController().renderText("指定学生不存在!");
      } else {
        if (Student.dao.findById(inv.getController().getPara("id")).getDel()==0){
          inv.invoke();
        } else {
          inv.getController().renderText("指定学生已删除!");
        }
      }
    } else {
      inv.getController().renderText("缺少参数!");
    }
  }
}
