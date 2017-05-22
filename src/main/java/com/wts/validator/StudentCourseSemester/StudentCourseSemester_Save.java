package com.wts.validator.StudentCourseSemester;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.wts.entity.model.Studentcoursesemester;

public class StudentCourseSemester_Save implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("sid"))
            && !StrKit.isBlank(inv.getController().getPara("cid"))
            && !StrKit.isBlank(inv.getController().getPara("tid"))
            ) {
      if (Studentcoursesemester.dao.findById(inv.getController().getPara("cid"), inv.getController().getPara("sid"), inv.getController().getPara("tid")) != null) {
        inv.getController().renderText("该关系已存在!");
      } else{
        inv.invoke();
      }
    } else {
      inv.getController().renderText("缺少参数!");
    }
  }
}
