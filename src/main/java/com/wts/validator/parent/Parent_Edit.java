package com.wts.validator.parent;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.wts.entity.model.Parent;

public class Parent_Edit implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("name"))
            && !StrKit.isBlank(inv.getController().getPara("mobile"))
            ) {
      String name = inv.getController().getPara("name");
      String mobile = inv.getController().getPara("mobile");
      String id = inv.getController().getPara("id");
      Parent object = Parent.dao.findById(id);
      if(object.getStr("name").equals(name)
              && object.getStr("mobile").equals(mobile)
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
