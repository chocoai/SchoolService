package com.wts.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.wts.entity.model.Teacher;

import javax.servlet.http.HttpSession;

public class AjaxTeacher implements Interceptor {

    public void intercept(Invocation inv) {
        HttpSession session = inv.getController().getSession();
//        if (session.getAttribute("teacher") == null
//                || inv.getController().getRequest().getHeader("X-Requested-With") == null
//                ) {
//            inv.getController().renderText("error");
//        } else {
            String url = inv.getTarget().getClass().getSimpleName()+"/"+inv.getMethodName();
            Teacher teacher = (Teacher)session.getAttribute("teacher");
            teacher.getId();
            System.out.println(inv.getMethodName());
            System.out.println(inv.getTarget().getClass().getSimpleName());
            inv.invoke();
//        }
    }
}