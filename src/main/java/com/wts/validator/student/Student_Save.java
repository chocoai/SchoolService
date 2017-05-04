package com.wts.validator.student;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.wts.entity.model.Student;

public class Student_Save implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("name"))
            && !StrKit.isBlank(inv.getController().getPara("number"))
            && !StrKit.isBlank(inv.getController().getPara("code"))
            && !StrKit.isBlank(inv.getController().getPara("address"))
            && !StrKit.isBlank(inv.getController().getPara("state"))
            ) {
      String number = inv.getController().getPara("number");
      String code = inv.getController().getPara("code");
      if (Student.dao.find("SELECT * FROM student WHERE number = ?", number).size() != 0) {
        inv.getController().renderText("该学生身份证号码已存在!");
      } else if (Student.dao.find("SELECT * FROM student WHERE code = ?", code).size() != 0) {
        inv.getController().renderText("该学生学籍号码已存在!");
      } else {
        inv.invoke();
      }
    } else {
      inv.getController().renderText("缺少参数!");
    }
  }
}
