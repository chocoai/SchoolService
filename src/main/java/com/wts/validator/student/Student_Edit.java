package com.wts.validator.student;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
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
      Student student = Student.dao.findById(id);
      if (!student.getStr("number").equals(name)
              && Student.dao.find("SELECT * FROM student WHERE number = ?", number).size() != 0) {
        inv.getController().renderText("该学生身份证号码已存在!");
      } else if (!student.getStr("code").equals(code)
              && Student.dao.find("SELECT * FROM student WHERE code = ?", code).size() != 0) {
        inv.getController().renderText("该学生学籍号码已存在!");
      } else if(student.getStr("name").equals(name)
              && student.getStr("number").equals(number)
              && student.getStr("code").equals(code)
              && student.getStr("address").equals(address)
              && student.getStr("state").equals(state)
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
