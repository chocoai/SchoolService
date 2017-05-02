package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.WP;
import com.wts.entity.model.*;
import com.wts.interceptor.OverdueCheck;
import com.wts.interceptor.AjaxTeacher;
import com.wts.interceptor.Login;

import java.util.Date;

public class TeacherMessageReadController extends Controller {
    private String getSQL(String sql) {
        return " FROM ((teachermessageread " +
                " LEFT JOIN teachermessage ON teachermessageread.message_id = teachermessage.id) " +
                " LEFT JOIN teacher ON teachermessage.teacher_id = teacher.id) " +
                " WHERE (teachermessage.title LIKE '%" + sql +
                "%' OR teachermessage.content LIKE '%" + sql +
                "%') AND teachermessageread.teacher_id = " + ((Teacher) getSessionAttr("teacher")).getId().toString() +
                " ORDER BY teachermessageread.id DESC";
    }

    /**
     * 登录移动_教师_教师消息
     */
    public void Mobile_Teacher_TeacherMessage() throws WeixinException {
        if (getSessionAttr("teacher") == null) {
            if (getCookie("dit") == null || getCookie("dim").equals("")) {
                if (!(getPara("code") == null || getPara("code").equals(""))) {
                    User user = WP.me.getUserByCode(getPara("code"));
                    Teacher teacher = Teacher.dao.findFirst("SELECT * FROM teacher WHERE userId = ? AND state = ? ", user.getUserId(), 1);
                    if (teacher != null) {
                        setSessionAttr("teacher", teacher);
                        setCookie("dit", teacher.getId().toString(), 60 * 30);
                        if (user.getAvatar().equals(teacher.getPicUrl())) {
                            teacher.set("picUrl", user.getAvatar()).update();
                        }
                        render("/static/html/mobile/teacher/Mobile_Teacher_TeacherMessage.html");
                    } else {
                        redirect("/");
                    }
                } else {
                    redirect("/");
                }
            } else {
                Teacher teacher = Teacher.dao.findById(getCookie("dit"));
                setSessionAttr("teacher", teacher);
                render("/static/html/mobile/teacher/Mobile_Teacher_TeacherMessage.html");
            }
        } else {
            render("/static/html/mobile/teacher/Mobile_Teacher_TeacherMessage.html");
        }
    }

    /**
     * 查询
     */
    @Before(AjaxTeacher.class)
    public void query() {
        renderJson(Courseroomteacher.dao.paginate(getParaToInt("pageCurrent"), getParaToInt("pageSize"),
                "SELECT DISTINCT teachermessageread.*, teachermessage.title, teachermessage.content, teacher.name AS tname, teachermessage.time AS ttime, teachermessage.state AS tstate, teachermessage.reply AS treply, teachermessage.id AS tid",
                getSQL(getPara("queryString"))).getList());
    }

    /**
     * 计数
     */
    @Before(AjaxTeacher.class)
    public void total() {
        Long count = Db.queryLong("SELECT COUNT(*) " + getSQL(getPara("queryString")));
        if (count % getParaToInt("pageSize") == 0) {
            renderText((count / getParaToInt("pageSize")) + "");
        } else {
            renderText((count / getParaToInt("pageSize") + 1) + "");
        }
    }

    /**
     * 获取
     */
    @Before({Login.class, OverdueCheck.class})
    public void get() {
        Teachermessageread tmr = Teachermessageread.dao.findById(getPara("id"));
        Teachermessage tm = Teachermessage.dao.findById(tmr.getMessageId());
        String teacherMessage = "\"teacherMessage\": [{\"id\": \"" + tm.get("id") + "\","
                + "\"title\": \"" + tm.get("title") + "\","
                + "\"content\": \"" + tm.get("content") + "\","
                + "\"state\": \"" + tm.get("state") + "\","
                + "\"time\": \"" + tm.get("time") + "\","
                + "\"name\": \"" + Teacher.dao.findById(tm.get("teacher_id")).getName() + "\","
                + "\"reply\": \"" + tm.get("reply") + "\"}]";
        String teacherMessageRead = "\"teacherMessageRead\": [{\"id\": \"" + tmr.get("id") + "\","
                + "\"state\": \"" + tmr.get("state") + "\","
                + "\"name\": \"" + Teacher.dao.findById(tmr.get("teacher_id")).getName() + "\"}]";
        renderJson("{" + teacherMessage + "," + teacherMessageRead + "}");
    }
    @Before(Tx.class)
    public void reading() throws WeixinException {
        Teachermessageread tmr = Teachermessageread.dao.findById(getPara("id"));
        if (tmr == null) {
            renderText("要确认读取的内容不存在!");
        } else if (tmr.get("state").toString().equals("1")) {
            renderText("该消息已确认读取!");
        } else {
            tmr.set("state", 1).set("time",new Date()).update();
            renderText("OK");
        }
    }
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
                    setAttr("title", "确认完成!");
                    setAttr("content", "系统将汇总所有确认情况并反馈!");
                } else {
                    setAttr("title", "重复确认!");
                    setAttr("content", "该消息您已经确认读取，无需再次确认!");
                }
            } else {
                setAttr("title", "消息失效!");
                setAttr("content", "该消息已经失效，无需确认读取!");
            }
        } else {
            setAttr("title", "权限错误!");
            setAttr("content", "您无权阅读此消息!");
        }
        render("/html/read.html");
    }

}
