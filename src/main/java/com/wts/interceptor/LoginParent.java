package com.wts.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.wts.entity.model.Enterprise;

import javax.servlet.http.HttpSession;

public class LoginParent implements Interceptor {

    public void intercept(Invocation inv) {

        HttpSession session = inv.getController().getSession();
        if (session.getAttribute("parent") == null || ((Enterprise) session.getAttribute("parent")).getIsParent() != 1) {
            inv.getController().redirect("/");
        } else {
            inv.invoke();
        }
    }
}