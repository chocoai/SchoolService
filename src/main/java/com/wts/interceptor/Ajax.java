package com.wts.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class Ajax implements Interceptor {

    public void intercept(Invocation inv) {
        if ((inv.getController().getSession().getAttribute("teacher") == null
                && inv.getController().getSession().getAttribute("manager") == null
                && inv.getController().getSession().getAttribute("parent") == null)
                || inv.getController().getRequest().getHeader("X-Requested-With") == null
                ) {
            inv.getController().renderText("error");
        } else {
            inv.invoke();
        }
    }
}