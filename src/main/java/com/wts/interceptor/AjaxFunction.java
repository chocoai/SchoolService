package com.wts.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

import javax.servlet.http.HttpSession;

public class AjaxFunction implements Interceptor {

	public void intercept(Invocation inv) {

		if(inv.getController().getSession().getAttribute("teacher") == null
						|| inv.getController().getRequest().getHeader("X-Requested-With") == null
						){
			inv.getController().renderText("error");
		}
		else{
			inv.invoke();
		}
	}
}