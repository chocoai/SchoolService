package com.wts.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class OverdueCheck implements Interceptor {

  public void intercept(Invocation inv) {
    if ((inv.getController().getSession().getAttribute("Teacher") == null
            && inv.getController().getSession().getAttribute("Parent") == null)
//            || inv.getController().getRequest().getHeader("X-Requested-With") == null
            ) {
      inv.getController().renderText("overdue");
      inv.getController().redirect("/");
    } else {
      inv.invoke();
    }
  }
}