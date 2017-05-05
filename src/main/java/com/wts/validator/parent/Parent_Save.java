package com.wts.validator.parent;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;

public class Parent_Save implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("name"))
            && !StrKit.isBlank(inv.getController().getPara("mobile"))
            ) {
      inv.invoke();
    } else {
      inv.getController().renderText("缺少参数!");
    }
  }
}
