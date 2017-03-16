package com.wts.controller;


import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.message.NotifyMessage;
import com.foxinmy.weixin4j.qy.model.IdParameter;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.WP;
import com.wts.entity.model.Teacher;
import com.wts.entity.model.Teachermessage;
import com.wts.entity.model.Teachermessageread;
import com.foxinmy.weixin4j.tuple.Text;
import com.wts.interceptor.Ajax;
import com.wts.interceptor.AjaxManager;
import com.wts.interceptor.Login;
import com.wts.util.ParamesAPI;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TeacherMessageController extends Controller {
    private String getSQL(String sql) {
        return "FROM teachermessage " +
                "LEFT JOIN teacher " +
                "ON teachermessage.teacher_id = teacher.id " +
                "WHERE teacher.name LIKE '%" + sql +
                "%' OR teachermessage.title LIKE '%" + sql +
                "%' OR teachermessage.content LIKE '%" + sql +
                "%' ORDER BY teachermessage.id DESC";
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
     * 初始化新建时的数据
     */
    @Before(AjaxManager.class)
    public void list() {
        String list,select;


        List<Teacher> teacherz=Teacher.dao.find("SELECT * FROM teacher WHERE state = 1 ORDER BY name ASC");
        String ss = "";
        if (teacherz.size() > 0) {
            for (Teacher t : teacherz) {
                ss = ss + "{\"id\": \""+t.getId() + "\","
                        + "\"name\": \""+t.getName() + "\","
                        + "\"state\": \""+t.getState() + "\","
                        + "\"type\": \""+t.getType() + "\","
                        + "\"picUrl\": \""+t.getPicUrl() + "\"},";
            }
            list="\"list\": [" + ss.substring(0, ss.length() - 1) + "]";
        } else {
            list="\"list\":[]";
        }
        List<Teacher> teachers = Teacher.dao.find("SELECT id FROM teacher WHERE state=1");
        String tt = "";
        if (teachers.size() > 0) {
            for (Teacher t : teachers) {
                tt = tt + t.getId() + ",";
            }
            select="\"select\": [" + tt.substring(0, tt.length() - 1) + "]";
        } else {
            select="\"select\":[]";
        }
        renderJson("{"+list+","+select+"}");
    }
    /**
     * 查询
     */
    @Before(AjaxManager.class)
    public void query() {
        renderJson(Teachermessage.dao.paginate(getParaToInt("pageCurrent"), getParaToInt("pageSize"), "SELECT teachermessage.*,teacher.name", getSQL(getPara("queryString"))).getList());
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
    @Before({Login.class, Ajax.class})
    public void get() {
        String teacheres,teacherez,messages;
        String sql= "SELECT teacher.*, teachermessageread.time "+
                " FROM teachermessageread " +
                " LEFT JOIN teacher " +
                " ON teachermessageread.teacher_id = teacher.id " +
                " WHERE teachermessageread.message_id = ? AND teachermessageread.state = ?";
        String sq= "SELECT teacher.name AS name, teachermessage.* "+
                " FROM teachermessage " +
                " LEFT JOIN teacher " +
                " ON teachermessage.teacher_id = teacher.id " +
                " WHERE teachermessage.id = ?";
        List<Record> teachers =Db.find(sql,getPara("id"),1);
        List<Record> teacherz =Db.find(sql,getPara("id"),0);
        List<Record> message =Db.find(sq,getPara("id"));

        String ss = "";
        if (teachers.size() > 0) {
            for (Record t : teachers) {
                ss = ss + "{\"id\": \""+t.get("id") + "\","
                        + "\"name\": \""+t.get("name") + "\","
                        + "\"time\": \""+t.get("time") + "\","
                        + "\"state\": \""+t.get("state")  + "\","
                        + "\"picUrl\": \""+t.get("picUrl") + "\"},";
            }
            teacheres="\"teachers\": [" + ss.substring(0, ss.length() - 1) + "]";
        } else {
            teacheres="\"teachers\":[]";
        }

        String zz = "";
        if (teacherz.size() > 0) {
            for (Record t : teacherz) {
                zz = zz + "{\"id\": \""+t.get("id") + "\","
                        + "\"name\": \""+t.get("name") + "\","
                        + "\"state\": \""+t.get("state")  + "\","
                        + "\"picUrl\": \""+t.get("picUrl") + "\"},";
            }
            teacherez="\"teacherz\": [" + zz.substring(0, zz.length() - 1) + "]";
        } else {
            teacherez="\"teacherz\":[]";
        }

        String rr = "";
        if (message.size() > 0) {
            for (Record t : message) {
                rr = rr + "{\"id\": \""+t.get("id") + "\","
                        + "\"name\": \""+t.get("name") + "\","
                        + "\"title\": \""+t.get("title")  + "\","
                        + "\"content\": \""+t.get("content")  + "\","
                        + "\"time\": \""+t.get("time") + "\","
                        + "\"state\": \""+t.get("state")  + "\","
                        + "\"send\": \""+t.get("send") + "\"},";
            }
            messages="\"message\": [" + rr.substring(0, rr.length() - 1) + "]";
        } else {
            messages="\"message\":[]";
        }
        renderJson("{"+teacheres+","+teacherez+","+messages+"}");
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
        } else if (!teacherMessage.get("teacher_id").equals(((Teacher) getSessionAttr("manager")).getId())){
            renderText("该消息只能用其发布者停用!");
        }else {
            teacherMessage.set("state", 0).update();
            renderText("OK");
        }
    }

    private static String BASIC = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + ParamesAPI.corpId + "&redirect_uri=http%3a%2f%2f" + ParamesAPI.URL + "%2f" + "XXXXX" + "&response_type=code&scope=snsapi_base&state=1#wechat_redirect";

    @Before({Tx.class, AjaxManager.class})
    public void save() {
        if (getPara("title").length() > 20) {
            renderText("输入标题超过20字符!");
        } else if (getPara("content").length() > 200) {
            renderText("输入内容超过200字符!");
        } else {
            Teachermessage teacherMessage = new Teachermessage();
            teacherMessage.set("title", getPara("title"))
                    .set("content", getPara("content"))
                    .set("reply", getPara("reply"))
                    .set("state", 1)
                    .set("time", new Date())
                    .set("teacher_id", ((Teacher) getSessionAttr("manager")).getId())
                    .save();
            String[] teacherId = getParaValues("teacher_id[]");
            IdParameter idParameter = new IdParameter();
            SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //格式化当前系统日期
            String read = BASIC.replaceAll("XXXXX", "teacherMessageRead%2fread%3fmessageId%3d" + teacherMessage.getId().toString());
            StringBuffer buffer = new StringBuffer();
            buffer.append("标题：" + getPara("title")).append("\n");
            buffer.append("教师：" + Teacher.dao.findById(((Teacher) getSessionAttr("manager")).getId()).getName()).append("\n");
            buffer.append("时间：" + dateFm.format(teacherMessage.get("time"))).append("\n");
            buffer.append("内容：" + getPara("content")).append("\n");
            if (getPara("reply").equals("1")) {
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

    @Before({Tx.class, AjaxManager.class})
    public void send(){
        String sql= "SELECT teacher.* "+
                " FROM teachermessageread " +
                " LEFT JOIN teacher " +
                " ON teachermessageread.teacher_id = teacher.id " +
                " WHERE teachermessageread.message_id = ? AND teachermessageread.state = 0";
        List<Record> teachers =Db.find(sql,getPara("id"));
        Teachermessage teacherMessage=Teachermessage.dao.findById(getPara("id"));
        if (teachers.size()==0) {
            renderText("指定教师均已阅读!");
        } else if(teacherMessage.getState()!=1){
            renderText("该消息已停用!");
        } else{
            IdParameter idParameter = new IdParameter();
            SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //格式化当前系统日期
            String read = BASIC.replaceAll("XXXXX", "teacherMessageRead%2fread%3fmessageId%3d" + teacherMessage.getId().toString());
            StringBuffer buffer = new StringBuffer();
            buffer.append("标题：" + teacherMessage.get("title")).append("\n");
            buffer.append("教师：" + Teacher.dao.findById(teacherMessage.get("teacher_id")).getName()).append("\n");
            buffer.append("时间：" + dateFm.format(teacherMessage.get("time"))).append("\n");
            buffer.append("内容：" + teacherMessage.get("content")).append("\n");
            buffer.append("<a href=\"" + read + "\">确认已读点击这里</a>");
            if (teacherMessage.get("reply").equals("1")) {
                for (Record i : teachers) {
                    if (Teacher.dao.findById(i.get("id")).getState() == 1) {
                        idParameter.putUserIds(Teacher.dao.findById(i).getUserId());
                    }
                }
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
