package com.wts.validator.room;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.wts.entity.model.Course;
import com.wts.entity.model.Room;

public class Room_Edit implements Interceptor {
    public void intercept(Invocation inv) {
        if (!StrKit.isBlank(inv.getController().getPara("year"))
                && !StrKit.isBlank(inv.getController().getPara("order"))
                && !StrKit.isBlank(inv.getController().getPara("state"))
                && !StrKit.isBlank(inv.getController().getPara("id"))
                ) {
            String year = inv.getController().getPara("year");
            String order = inv.getController().getPara("order");
            String state = inv.getController().getPara("state");
            String id = inv.getController().getPara("id");
            Room room = Room.dao.findById(id);
            if (!(room.get("year").equals(year) && room.get("order").equals(order))
                    && (Room.dao.find("SELECT * FROM room WHERE `year` = ? AND `order` = ?", year, order).size() != 0
                    || Room.dao.find("SELECT * FROM room WHERE name = ?", year + "级" + order + "班").size() != 0)
                    ) {
                inv.getController().renderText("该班级已存在!");
            } else if (room.get("name").equals(year)
                    && room.get("order").equals(order)
                    && room.get("state").equals(state)
                    ) {
                inv.getController().renderText("未发现修改内容!");
            } else {
                inv.invoke();
            }
        } else {
            inv.getController().renderText("缺少参数!");
        }

    }
}
