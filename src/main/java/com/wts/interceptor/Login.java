package com.wts.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.wts.entity.model.Enterprise;

import javax.servlet.http.HttpSession;

public class Login implements Interceptor {

    public void intercept(Invocation inv) {
        HttpSession session = inv.getController().getSession();
        if (session.getAttribute("teacher") == null && session.getAttribute("manager") == null && session.getAttribute("parent") == null) {
            inv.getController().redirect("/");
        } else {
            inv.invoke();
        }
    }
}