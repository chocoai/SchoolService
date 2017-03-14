package com.wts.controller;


import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.message.NotifyMessage;
import com.foxinmy.weixin4j.qy.model.IdParameter;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.WP;
import com.wts.entity.model.Teacher;
import com.wts.entity.model.Teachermessage;
import com.wts.entity.model.Teachermessageread;
import com.foxinmy.weixin4j.tuple.Text;
import com.wts.interceptor.AjaxManager;
import com.wts.util.ParamesAPI;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TeacherMessageController extends Controller {
    private String getSQL(String sql) {
        return "FROM teachermessage " +
                "LEFT JOIN teacher " +
                "ON teachermessage.teacher_id = teacher.id " +
                "WHERE teacher.name LIKE '%" + sql +
                "%' OR teachermessage.title LIKE '%" + sql +
                "%' OR teachermessage.content LIKE '%" + sql +
                "%' ORDER BY teachermessage.id ASC";
    }

    /**
     * 登录移动_管理_教师消息
     */
    public void Mobile_Manager_TeacherMessage() throws WeixinException {
        if (getSessionAttr("manager") == null || ((Teacher) getSessionAttr("manager")).getIsManager() != 1) {
            if (getCookie("dim") == null || getCookie("dim").equals("")) {
                if (!(getPara("code") == null || getPara("code").equals(""))) {
                    User user = WP.me.getUserByCode(getPara("code"));
                    Teacher manager = Teacher.dao.findFirst("SELECT * FROM teacher WHERE userId = ? AND state = ? AND isManager = 1", user.getUserId(), 1);
                    if (manager != null) {
                        setSessionAttr("manager", manager);
                        setCookie("dim", manager.getId().toString(), 60 * 30);
                        if (user.getAvatar().equals(manager.getPicUrl())) {
                            manager.set("picUrl", user.getAvatar()).update();
                        }
                        render("/static/html/mobile/manager/Mobile_Manager_TeacherMessage.html");
                    } else {
                        redirect("/");
                    }
                } else {
                    redirect("/");
                }
            } else {
                Teacher manager = Teacher.dao.findById(getCookie("dim"));
                setSessionAttr("manager", manager);
                render("/static/html/mobile/manager/Mobile_Manager_TeacherMessage.html");
            }
        } else {
            render("/static/html/mobile/manager/Mobile_Manager_TeacherMessage.html");
        }
    }

    /**
     * 查询
     */
    @Before(AjaxManager.class)
    public void query() {
        renderJson(Teacher.dao.paginate(getParaToInt("pageCurrent"), getParaToInt("pageSize"), "SELECT *", getSQL(getPara("queryString"))).getList());
    }

    /**
     * 计数
     */
    @Before(AjaxManager.class)
    public void total() {
        Long count = Db.queryLong("SELECT COUNT(*) " + getSQL(getPara("queryString")));
        if (count % getParaToInt("pageSize") == 0) {
            renderText((count / getParaToInt("pageSize")) + "");
        } else {
            renderText((count / getParaToInt("pageSize") + 1) + "");
        }
    }

    /**
     * 注销
     */
    @Before(AjaxManager.class)
    public void inactive() {
        Teachermessage teacherMessage = Teachermessage.dao.findById(getPara("id"));
        if (teacherMessage == null) {
            renderText("要停用的消息不存在!");
        } else if (teacherMessage.getState().toString().equals("0")) {
            renderText("该消息已停用!");
        } else {
            teacherMessage.set("state", 0).update();
            renderText("OK");
        }
    }

    private static String BASIC = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + ParamesAPI.corpId + "&redirect_uri=http%3a%2f%2f" + ParamesAPI.URL + "%2f" + "XXXXX" + "&response_type=code&scope=snsapi_base&state=1#wechat_redirect";

    @Before({Tx.class, AjaxManager.class})
    public void save() {
        if (getPara("title").length() > 20) {
            renderText("输入标题超过20字符!");
        } else if (getPara("content").length() > 300) {
            renderText("输入内容超过300字符!");
        } else {
            System.out.println(getPara("title"));
            System.out.println(getPara("content"));
            System.out.println(getPara("send"));
            System.out.println(((Teacher) getSessionAttr("manager")).getId());
            Teachermessage teacherMessage = new Teachermessage();
            teacherMessage.set("title", getPara("title"))
                    .set("content", getPara("content"))
                    .set("send", getPara("send"))
                    .set("state", 1)
                    .set("time", new Date())
                    .set("teacher_id", ((Teacher) getSessionAttr("manager")).getId())
                    .save();
            System.out.println("22222222");
            String[] teacherId = getParaValues("teacher_id[]");
            IdParameter idParameter = new IdParameter();
            SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //格式化当前系统日期
            String read = BASIC.replaceAll("XXXXX", "teacherMessageRead%2fread%3fmessageId%3d" + teacherMessage.getId().toString());
            StringBuffer buffer = new StringBuffer();
            buffer.append("标题：" + getPara("title")).append("\n");
            buffer.append("教师：" + Teacher.dao.findById(((Teacher) getSessionAttr("manager")).getId()).getName()).append("\n");
            buffer.append("时间：" + dateFm.format(teacherMessage.get("time"))).append("\n");
            buffer.append("内容：" + getPara("content")).append("\n");
            System.out.println(teacherId);
            if (getPara("send").equals("1")) {
                for (String i : teacherId) {
                    if (Teacher.dao.findById(i).getState() == 1) {
                        idParameter.putUserIds(Teacher.dao.findById(i).getUserId());
                        Teachermessageread teacherMessageRead = new Teachermessageread();
                        teacherMessageRead.set("message_id", teacherMessage.getId())
                                .set("teacher_id", i)
                                .set("state", 0)
                                .save();
                    }
                }
                buffer.append("<a href=\"" + read + "\">确认已读点击这里</a>");
            }
            try {
                WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.parentId, new Text(buffer.toString()), idParameter, false));
            } catch (Exception e) {
                renderText(e.getMessage());
            }
            renderText("OK");
        }
    }
}
