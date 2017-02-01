package com.wts.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.wts.entity.model.Enterprise;

public class AjaxParent implements Interceptor {

    public void intercept(Invocation inv) {
        if (inv.getController().getSession().getAttribute("parent") == null
                || ((Enterprise) inv.getController().getSession().getAttribute("parent")).getIsParent() != 1
                || inv.getController().getRequest().getHeader("X-Requested-With") == null
                ) {
            inv.getController().renderText("error");
        } else {
            inv.invoke();
        }
    }
}