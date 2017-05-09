package com.wts.validator.courseRoomTeacher;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;

public class CourseRoomTeacher_Save implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("cid"))
            && !StrKit.isBlank(inv.getController().getPara("rid"))
            && !StrKit.isBlank(inv.getController().getPara("sid"))
            && !StrKit.isBlank(inv.getController().getPara("tid"))
            ) {
      String cid = inv.getController().getPara("cid");
      String rid = inv.getController().getPara("rid");
      String sid = inv.getController().getPara("sid");
      String tid = inv.getController().getPara("tid");
      if (Db.find("SELECT * FROM courseroomteacher WHERE course_id = ? AND room_id = ? AND semester_id = ? AND teacher_id = ?", cid,rid,sid,tid).size() != 0) {
        inv.getController().renderText("该关系已存在!");
      } else{
        inv.invoke();
      }
    } else {
      inv.getController().renderText("缺少参数!");
    }
  }
}
