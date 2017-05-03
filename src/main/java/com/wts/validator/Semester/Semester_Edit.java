package com.wts.validator.Semester;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.wts.entity.model.Semester;

import java.util.Date;

public class Semester_Edit implements Interceptor {
  public void intercept(Invocation inv) {
    if (!StrKit.isBlank(inv.getController().getPara("name"))
            && !StrKit.isBlank(inv.getController().getPara("time_start"))
            && !StrKit.isBlank(inv.getController().getPara("time_end"))
            && !StrKit.isBlank(inv.getController().getPara("id"))
            ) {
      String name = inv.getController().getPara("name");
      Date time_start = new Date(inv.getController().getParaToLong("time_start"));
      Date time_end = new Date(inv.getController().getParaToLong("time_end"));
      String id = inv.getController().getPara("id");
      Semester semester = Semester.dao.findById(id);
      if (!semester.getStr("name").equals(name)
              && Semester.dao.find("SELECT * FROM semester WHERE name = ?", name).size() != 0) {
        inv.getController().renderText("该名称已存在!");
      } else if (semester.getStr("name").equals(name)
              && semester.getDate("time_start") == time_start
              && semester.getDate("time_end") == time_end
              ) {
        inv.getController().renderText("未发现修改内容!");
      } else if (time_start.after(time_end)) {
        inv.getController().renderText("开始日期晚于终止日期!");
      } else {
        inv.invoke();
      }
    } else {
      inv.getController().renderText("缺少参数!");
    }

  }
}
