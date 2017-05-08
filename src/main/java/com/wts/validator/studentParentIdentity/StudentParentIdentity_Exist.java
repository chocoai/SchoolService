package com.wts.validator.studentParentIdentity;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.wts.entity.model.Course;
import com.wts.entity.model.Studentparentidentity;

public class StudentParentIdentity_Exist implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("iid"))
            && !StrKit.isBlank(inv.getController().getPara("pid"))
            && !StrKit.isBlank(inv.getController().getPara("sid"))
            ) {
      if (Studentparentidentity.dao.findById(inv.getController().getPara("iid"), inv.getController().getPara("pid"), inv.getController().getPara("sid")) == null) {
        inv.getController().renderText("指定关系不存在!");
      } else {
        inv.invoke();
      }
    } else {
      inv.getController().renderText("缺少参数!");
    }
  }
}
