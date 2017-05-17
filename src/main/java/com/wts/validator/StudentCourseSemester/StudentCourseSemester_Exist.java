package com.wts.validator.StudentCourseSemester;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.wts.entity.model.Studentcoursesemester;

public class StudentCourseSemester_Exist implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("xid"))
            && !StrKit.isBlank(inv.getController().getPara("cid"))
            && !StrKit.isBlank(inv.getController().getPara("sid"))
            ) {
      if (Studentcoursesemester.dao.findById(inv.getController().getPara("cid"), inv.getController().getPara("xid"), inv.getController().getPara("sid")) == null) {
        inv.getController().renderText("指定关系不存在!");
      } else {
        inv.invoke();
      }
    } else {
      inv.getController().renderText("缺少参数!");
    }
  }
}
