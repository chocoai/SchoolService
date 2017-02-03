package com.wts.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.wts.entity.model.Enterprise;

import javax.servlet.http.HttpSession;

public class LoginTeacher implements Interceptor {

    public void intercept(Invocation inv) {
        HttpSession session = inv.getController().getSession();
        if (session.getAttribute("teacher") == null || ((Enterprise) session.getAttribute("teacher")).getIsTeacher() != 1) {
            inv.getController().redirect("/");
        } else {
            inv.invoke();
        }
    }
}