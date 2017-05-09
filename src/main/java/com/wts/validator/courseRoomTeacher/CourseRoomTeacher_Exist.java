package com.wts.validator.courseRoomTeacher;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.wts.entity.model.Courseroomteacher;

public class CourseRoomTeacher_Exist implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("cid"))
            && !StrKit.isBlank(inv.getController().getPara("rid"))
            && !StrKit.isBlank(inv.getController().getPara("sid"))
            && !StrKit.isBlank(inv.getController().getPara("tid"))
            ) {
      if (Courseroomteacher.dao.findById(inv.getController().getPara("cid"), inv.getController().getPara("rid"), inv.getController().getPara("sid"), inv.getController().getPara("tid")) == null) {
        inv.getController().renderText("指定关系不存在!");
      } else {
        inv.invoke();
      }
    } else {
      inv.getController().renderText("缺少参数!");
    }
  }
}
