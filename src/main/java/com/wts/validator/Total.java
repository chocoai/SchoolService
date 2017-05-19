package com.wts.validator;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;

public class Total implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("pageSize"))
            && !StrKit.isBlank(inv.getController().getPara("keyword"))
            ) {
      inv.invoke();
    } else {
      inv.getController().renderText("缺少参数!");
    }
  }
}
