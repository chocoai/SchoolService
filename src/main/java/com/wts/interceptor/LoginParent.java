package com.wts.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

import javax.servlet.http.HttpSession;

public class LoginParent implements Interceptor {

    public void intercept(Invocation inv) {

        HttpSession session = inv.getController().getSession();
        if (session.getAttribute("Parent") == null) {
            inv.getController().redirect("/");
        } else {
            inv.invoke();
        }
    }
}