package com.wts.validator.Parent;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.wts.entity.model.Parent;

public class Parent_Exist implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("id"))) {
      if (Parent.dao.findById(inv.getController().getPara("id")) == null) {
        inv.getController().renderText("指定家长不存在!");
      } else {
        if (Parent.dao.findById(inv.getController().getPara("id")).getDel()==0){
          inv.invoke();
        } else {
          inv.getController().renderText("指定家长已删除!");
        }
      }
    } else {
      inv.getController().renderText("缺少参数!");
    }
  }
}
