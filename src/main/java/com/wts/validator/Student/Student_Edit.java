package com.wts.validator.Student;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.wts.entity.model.Student;

public class Student_Edit implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("name"))
            && !StrKit.isBlank(inv.getController().getPara("number"))
            && !StrKit.isBlank(inv.getController().getPara("code"))
            && !StrKit.isBlank(inv.getController().getPara("address"))
            && !StrKit.isBlank(inv.getController().getPara("state"))
            && !StrKit.isBlank(inv.getController().getPara("id"))
            ) {
      String name = inv.getController().getPara("name");
      String number = inv.getController().getPara("number");
      String code = inv.getController().getPara("code");
      String address = inv.getController().getPara("address");
      String state = inv.getController().getPara("state");
      String id = inv.getController().getPara("id");
      Student object = Student.dao.findById(id);
      if (!object.getStr("number").equals(number)
              && Db.find("SELECT * FROM Student WHERE number = ?", number).size() != 0) {
        inv.getController().renderText("该学生身份证号码已存在!");
      } else if (!object.getStr("code").equals(code)
              && Db.find("SELECT * FROM Student WHERE code = ?", code).size() != 0) {
        inv.getController().renderText("该学生学籍号码已存在!");
      } else if(object.getStr("name").equals(name)
              && object.getStr("number").equals(number)
              && object.getStr("code").equals(code)
              && object.getStr("address").equals(address)
              && object.getState().toString().equals(state)
              ){
        inv.getController().renderText("未发现修改内容!");
      }else {
        inv.invoke();
      }
    } else {
      inv.getController().renderText("缺少参数!");
    }

  }
}
