//package com.wts.interceptor;
//
//import com.jfinal.aop.Interceptor;
//import com.jfinal.aop.Invocation;
//import com.wts.entity.model.Teacher;
//
//public class AjaxManager implements Interceptor {
//
//    public void intercept(Invocation inv) {
//        if (inv.getController().getSession().getAttribute("manager") == null
//                || ((Teacher) inv.getController().getSession().getAttribute("manager")).getIsManager() != 1
//                || inv.getController().getRequest().getHeader("X-Requested-With") == null
//                ) {
//            inv.getController().renderText("error");
//        } else {
//            inv.invoke();
//        }
//    }
//}