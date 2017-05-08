package com.wts.validator.studentParentIdentity;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;

public class StudentParentIdentity_Save implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("iid"))
            && !StrKit.isBlank(inv.getController().getPara("pid"))
            && !StrKit.isBlank(inv.getController().getPara("sid"))
            ) {
      String iid = inv.getController().getPara("iid");
      String pid = inv.getController().getPara("pid");
      String sid = inv.getController().getPara("sid");
      if (Db.find("SELECT * FROM studentparentidentity WHERE identity_id = ? AND parent_id = ? AND student_id = ?", iid,pid,sid).size() != 0) {
        inv.getController().renderText("该关系已存在!");
      } else if (Db.find("SELECT * FROM studentparentidentity WHERE parent_id = ? AND student_id = ?", pid,sid).size() != 0) {
        inv.getController().renderText("该关系已存在!");
      } else{
        inv.invoke();
      }
    } else {
      inv.getController().renderText("缺少参数!");
    }
  }
}
