package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.WP;
import com.wts.entity.model.Teacher;
import com.wts.entity.model.Teachermessage;
import com.wts.entity.model.Teachermessageread;
import com.wts.interceptor.AjaxTeacher;

import java.util.Date;

public class TeacherMessageReadController extends Controller {
    @Before(Tx.class)
    public void read() throws WeixinException {
        User user = WP.me.getUserByCode(getPara("code"));
        Teacher teacher = Teacher.dao.findFirst("SELECT * FROM teacher WHERE state=1 AND userId=?", user.getUserId());
        Teachermessageread teacherMessageRead = Teachermessageread.dao.findFirst("select * from teachermessageread where message_id=? and teacher_id=?", getPara("messageId"), teacher.getId());
        if (teacherMessageRead != null) {
            if (Teachermessage.dao.findById(teacherMessageRead.getMessageId()).getState() == 1) {
                if (teacherMessageRead.getState() == 0) {
                    teacherMessageRead.set("state", 1)
                            .set("time", new Date())
                            .update();
                    renderText("OK");
                } else {
                    renderText("该消息已确认读取!");
                }
            } else {
                renderText("该消息已经失效，无需确认读取!");
            }
        } else {
            renderText("您无权阅读此消息!");
        }
    }

}
