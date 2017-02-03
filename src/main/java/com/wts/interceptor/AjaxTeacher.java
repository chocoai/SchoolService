package com.wts.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.wts.entity.model.Enterprise;

public class AjaxTeacher implements Interceptor {

    public void intercept(Invocation inv) {
        if (inv.getController().getSession().getAttribute("teacher") == null
                || ((Enterprise) inv.getController().getSession().getAttribute("teacher")).getIsTeacher() != 1
                || inv.getController().getRequest().getHeader("X-Requested-With") == null
                ) {
            inv.getController().renderText("error");
        } else {
            inv.invoke();
        }
    }
}