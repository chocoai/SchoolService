package com.wts.validator.Student;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;

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
      if (Db.find("SELECT * FROM Student WHERE number = ?", number).size() != 0) {
        inv.getController().renderText("该学生身份证号码已存在!");
      } else if (Db.find("SELECT * FROM Student WHERE code = ?", code).size() != 0) {
        inv.getController().renderText("该学生学籍号码已存在!");
      } else {
        inv.invoke();
      }
    } else {
      inv.getController().renderText("缺少参数!");
    }
  }
}
