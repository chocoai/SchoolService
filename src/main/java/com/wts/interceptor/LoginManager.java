package com.wts.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.wts.entity.model.Teacher;

import javax.servlet.http.HttpSession;

public class LoginManager implements Interceptor {

    public void intercept(Invocation inv) {

        HttpSession session = inv.getController().getSession();
        if (session.getAttribute("manager") == null || ((Teacher) session.getAttribute("manager")).getIsManager() != 1) {
            inv.getController().redirect("/");
        } else {
            inv.invoke();
        }
    }
}