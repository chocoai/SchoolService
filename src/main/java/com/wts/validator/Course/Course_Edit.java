package com.wts.validator.Course;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.wts.entity.model.Course;

public class Course_Edit implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("name"))
            && !StrKit.isBlank(inv.getController().getPara("detail"))
            && !StrKit.isBlank(inv.getController().getPara("amount"))
            && !StrKit.isBlank(inv.getController().getPara("type"))
            && !StrKit.isBlank(inv.getController().getPara("id"))
            ) {
      String name = inv.getController().getPara("name");
      String detail = inv.getController().getPara("detail");
      String amount = inv.getController().getPara("amount");
      String type = inv.getController().getPara("type");
      String id = inv.getController().getPara("id");
      Course object = Course.dao.findById(id);
      if (!object.getStr("name").equals(name)
              && Db.find("SELECT * FROM course WHERE name = ?", name).size() != 0) {
        inv.getController().renderText("该名称已存在!");
      } else if(object.getStr("name").equals(name)
              && object.getStr("detail").equals(detail)
              && object.getAmount().toString().equals(amount)
              && object.getType().toString().equals(type)
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
