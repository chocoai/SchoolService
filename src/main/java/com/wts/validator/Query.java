package com.wts.validator;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.wts.entity.model.Course;

public class Query implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("pageCurrent")) && !StrKit.isBlank(inv.getController().getPara("pageSize"))) {
      inv.invoke();
    } else {
      inv.getController().renderText("缺少参数!");
    }
  }
}
