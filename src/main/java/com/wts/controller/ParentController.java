package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.message.NotifyMessage;
import com.foxinmy.weixin4j.qy.model.IdParameter;
import com.foxinmy.weixin4j.qy.model.User;
import com.foxinmy.weixin4j.tuple.Text;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.WP;
import com.wts.entity.model.*;
import com.wts.interceptor.*;
import com.wts.util.ParamesAPI;
import com.wts.util.Util;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import static com.wts.controller.MainController.getUserId;
import static com.wts.util.IDNumber.checkIDNumberDetail;
import static com.wts.util.IDNumber.checkIDNumberDetailB;
import static com.wts.util.Util.getString;

public class ParentController extends Controller {
    private String getSQL(String sql) {
        return "FROM parent WHERE name LIKE '%" + sql +
                "%' OR userId LIKE '%" + sql +
                "%' OR mobile LIKE '%" + sql +
                "%' OR weixinId LIKE '%" + sql +
                "%' OR qq LIKE '%" + sql +
                "%' OR email LIKE '%" + sql +
                "%' OR address LIKE '%" + sql +
                "%' OR login LIKE '%" + sql +
                "%' ORDER BY id DESC";
    }
    /**
     * 登录移动_管理_家长
     */
    public void Mobile_Manager_Parent() throws WeixinException {
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
                        render("/static/html/mobile/manager/Mobile_Manager_Parent.html");
                    } else {
                        redirect("/");
                    }
                } else {
                    redirect("/");
                }
            } else {
                Teacher manager = Teacher.dao.findById(getCookie("dim"));
                setSessionAttr("manager", manager);
                render("/static/html/mobile/manager/Mobile_Manager_Parent.html");
            }
        } else {
            render("/static/html/mobile/manager/Mobile_Manager_Parent.html");
        }
    }

    /**
     * 列表
     */
    @Before(AjaxManager.class)
    public void list() {
        renderJson(Parent.dao.find("SELECT * FROM parent WHERE state = 1 ORDER BY id DESC"));
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
     * 获取
     */
    @Before({Login.class, Ajax.class})
    public void get() {
        renderJson(Parent.dao.findById(getPara("id")));
    }

    /**
     * 激活
     */
    @Before(AjaxManager.class)
    public void active() {
        Parent parent = Parent.dao.findById(getPara("id"));
        if (parent == null) {
            renderText("要激活的家长不存在!");
        } else if (parent.get("state").toString().equals("1")) {
            renderText("该家长已处于关注状态!");
        } else {
            User user = new User(parent.get("userId").toString(), parent.get("name").toString());
            user.setMobile(parent.get("mobile").toString());
            user.setEmail(parent.get("email").toString());
            user.setWeixinId(parent.get("weixinId").toString());
            user.setPartyIds(1);
            try {
                WP.me.createUser(user);
                List<String> userIds = new ArrayList<String>();
                userIds.add(parent.get("userId").toString());
                WP.me.addTagUsers(ParamesAPI.parentTagId, userIds, new ArrayList<Integer>());
                parent.set("state", 1).update();
                renderText("OK");
            } catch (WeixinException e) {
                renderText(e.getErrorText());
            }
        }
    }

    /**
     * 注销
     */
    @Before(AjaxManager.class)
    public void inactive() {
        Parent parent = Parent.dao.findById(getPara("id"));
        if (parent == null) {
            renderText("要注销的家长不存在!");
        } else if (parent.get("state").toString().equals("3")) {
            renderText("该家长已处于取消关注状态!");
        } else if (parent.get("state").toString().equals("2")) {
            renderText("该家长已处于冻结状态!");
        } else {
            try {
                WP.me.deleteUser(parent.getUserId());
                parent.set("state", 3).update();
                renderText("OK");
            } catch (WeixinException e) {
                renderText(e.getErrorText());
            }
        }
    }

    /**
     * 检测手机_修改
     */
    @Before(AjaxManager.class)
    public void checkMobileForEdit() {
        if (!Util.getString(getPara("mobile")).matches("^1(3|4|5|7|8)\\d{9}$")) {
            renderText("手机号码格式错误!");
        } else if (!Parent.dao.findById(getPara("id")).get("mobile").equals(getPara("mobile"))
                && Teacher.dao.find("select * from teacher where mobile=?", getPara("mobile")).size() != 0
                && Parent.dao.find("select * from parent where mobile=?", getPara("mobile")).size() != 0
                ) {
            renderText("该手机号码已存在!");
        } else {
            renderText("OK");
        }
    }

    /**
     * 检测邮箱_修改
     */
    @Before(AjaxManager.class)
    public void checkEmailForEdit() {
        if (!Util.getString(getPara("email")).matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
            renderText("电子邮箱格式错误!");
        } else if (!Parent.dao.findById(getPara("id")).get("email").equals(getPara("email"))
                && Teacher.dao.find("select * from teacher where email=?", getPara("email")).size() != 0
                && Parent.dao.find("select * from parent where email=?", getPara("email")).size() != 0
                ) {
            renderText("该电子邮箱已存在!");
        } else {
            renderText("OK");
        }
    }

    /**
     * 检测QQ_修改
     */
    @Before(AjaxManager.class)
    public void checkQQForEdit() {
        if (!Util.getString(getPara("qq")).matches("^\\d{5,12}$")) {
            renderText("QQ号格式错误!");
        } else if (!Parent.dao.findById(getPara("id")).get("qq").equals(getPara("qq"))
                && Teacher.dao.find("select * from teacher where qq=?", getPara("qq")).size() != 0
                && Parent.dao.find("select * from parent where qq=?", getPara("qq")).size() != 0
                ) {
            renderText("该QQ号已存在!");
        } else {
            renderText("OK");
        }
    }


    /**
     * 检测微信_修改
     */
    @Before(AjaxManager.class)
    public void checkWeixinIdForEdit() {
        if (!Util.getString(getPara("weixinId")).matches("^[a-zA-Z]{1}[-_a-zA-Z0-9]{5,19}+$")) {
            renderText("微信号格式错误!");
        } else if (!Parent.dao.findById(getPara("id")).get("weixinId").equals(getPara("weixinId"))
                && Teacher.dao.find("select * from teacher where weixinId=?", getPara("weixinId")).size() != 0
                && Parent.dao.find("select * from parent where weixinId=?", getPara("weixinId")).size() != 0
                ) {
            renderText("该微信号已存在!");
        } else {
            renderText("OK");
        }
    }

    /**
     * 保存
     */
    @Before({Tx.class, AjaxManager.class})
    public void save() {
        if (!getPara("name").matches("^[\\u4e00-\\u9fa5]{2,}$")) {
            renderText("家长姓名应为两个以上汉字!");
        } else if (!getPara("mobile").matches("^1(3|4|5|7|8)\\d{9}$")) {
            renderText("手机号码格式错误!");
        } else if (Teacher.dao.find("SELECT * FROM teacher WHERE mobile = ?", getPara("mobile")).size() != 0) {
            renderText("已有教师使用该手机号码!");
        } else if (Parent.dao.find("SELECT * FROM parent WHERE mobile = ?", getPara("mobile")).size() != 0) {
            renderText("已有家长使用该手机号码!");
        } else if (getPara("email") != null && !getPara("email").equals("") && Teacher.dao.find("SELECT * FROM teacher WHERE email = ?", getPara("email")).size() != 0) {
            renderText("已有教师使用该电子邮箱!");
        } else if (getPara("email") != null && !getPara("email").equals("") && Parent.dao.find("SELECT * FROM parent WHERE email = ?", getPara("email")).size() != 0) {
            renderText("已有家长使用该电子邮箱!");
        } else if (getPara("qq") != null && !getPara("qq").equals("") && Teacher.dao.find("SELECT * FROM teacher WHERE qq = ?", getPara("qq")).size() != 0) {
            renderText("已有教师使用该QQ号!");
        } else if (getPara("qq") != null && !getPara("qq").equals("") && Parent.dao.find("SELECT * FROM parent WHERE qq = ?", getPara("qq")).size() != 0) {
            renderText("已有家长使用该QQ号!");
        } else if (getPara("weixinId") != null && !getPara("weixinId").equals("") && Teacher.dao.find("SELECT * FROM teacher WHERE weixinId = ?", getPara("weixinId")).size() != 0) {
            renderText("已有教师使用该微信号!");
        } else if (getPara("weixinId") != null && !getPara("weixinId").equals("") && Parent.dao.find("SELECT * FROM parent WHERE weixinId = ?", getPara("weixinId")).size() != 0) {
            renderText("已有家长使用该微信号!");
        } else if (Teacher.dao.find("SELECT * FROM teacher WHERE userId = ?", getUserId(getPara("name"))).size() != 0) {
            renderText("已有教师使用该userID!");
        } else if (Parent.dao.find("SELECT * FROM parent WHERE userId = ?", getUserId(getPara("name"))).size() != 0) {
            renderText("已有家长使用该userID!");
        } else {
            User user = new User(getUserId(getPara("name")), getPara("name").trim());
            user.setMobile(getPara("mobile").trim());
            user.setWeixinId(getPara("weixinId").trim());
            user.setEmail(getPara("email").trim());
            user.setPartyIds(1);
            try {
                WP.me.createUser(user);
                List<String> userIds = new ArrayList<String>();
                userIds.add(getUserId(getPara("name")));
                WP.me.addTagUsers(ParamesAPI.parentTagId,userIds,new ArrayList<Integer>());
                Parent parent = new Parent();
                parent.set("name", getPara("name").trim())
                        .set("mobile", getPara("mobile").trim())
                        .set("qq", getPara("qq").trim())
                        .set("email", getPara("email").trim())
                        .set("weixinId", getPara("weixinId").trim())
                        .set("address", getPara("address").trim())
                        .set("userId", getUserId(getPara("name")))
                        .set("pass", "0011223344")
                        .set("state", 4)
                        .save();
                renderText("OK");
            } catch (WeixinException e) {
                renderText(e.getErrorText());
            }
        }
    }
    /**
     * 管理员修改
     */
    @Before({Tx.class, AjaxManager.class})
    public void edit() {
        Parent parent = Parent.dao.findById(getPara("id"));
        if (parent == null) {
            renderText("要修改的家长不存在!");
        } else {
            if (Util.getString(parent.getStr("name")).equals(getPara("name").trim())
                    && Util.getString(parent.getStr("mobile")).equals(getPara("mobile").trim())
                    && Util.getString(parent.getStr("email")).equals(getPara("email").trim())
                    && Util.getString(parent.getStr("qq")).equals(getPara("qq").trim())
                    && Util.getString(parent.getStr("weixinId")).equals(getPara("weixinId").trim())
                    && Util.getString(parent.get("address").toString()).equals(getPara("address").trim())
                    ) {
                renderText("未找到修改内容!");
            } else if (!getPara("name").matches("^[\\u4e00-\\u9fa5]{2,}$")) {
                renderText("教师姓名应为两个以上汉字!");
            } else if (!getPara("mobile").matches("^1(3|4|5|7|8)\\d{9}$")) {
                renderText("手机号码格式错误!");
            } else if (!Util.getString(parent.getStr("mobile")).equals(getPara("mobile"))
                    && Teacher.dao.find("SELECT * FROM teacher WHERE mobile = ?", getPara("mobile")).size() != 0) {
                renderText("已有教师使用该手机号码!");
            } else if (!Util.getString(parent.getStr("mobile")).equals(getPara("mobile"))
                    && Parent.dao.find("SELECT * FROM parent WHERE mobile = ?", getPara("mobile")).size() != 0) {
                renderText("已有家长使用该手机号码!");
            } else if (!Util.getString(parent.getStr("email")).equals(getPara("email"))
                    && !getPara("email").equals("")
                    && Teacher.dao.find("SELECT * FROM teacher WHERE email = ?", getPara("email")).size() != 0) {
                renderText("已有教师使用该电子邮箱!");
            } else if (!Util.getString(parent.getStr("email")).equals(getPara("email"))
                    && !getPara("email").equals("")
                    && Parent.dao.find("SELECT * FROM parent WHERE email = ?", getPara("email")).size() != 0) {
                renderText("已有家长使用该电子邮箱!");
            } else if (!Util.getString(parent.getStr("qq")).equals(getPara("qq"))
                    && !getPara("qq").equals("")
                    && Teacher.dao.find("SELECT * FROM teacher WHERE qq = ?", getPara("qq")).size() != 0) {
                renderText("已有教师使用该QQ号!");
            } else if (!Util.getString(parent.getStr("qq")).equals(getPara("qq"))
                    && !getPara("qq").equals("")
                    && Parent.dao.find("SELECT * FROM parent WHERE qq = ?", getPara("qq")).size() != 0) {
                renderText("已有家长使用该QQ号!");
            } else if (!Util.getString(parent.getStr("weixinId")).equals(getPara("weixinId"))
                    && !getPara("weixinId").equals("")
                    && Teacher.dao.find("SELECT * FROM teacher WHERE weixinId = ?", getPara("weixinId")).size() != 0) {
                renderText("已有教师使用该微信号!");
            } else if (!Util.getString(parent.getStr("weixinId")).equals(getPara("weixinId"))
                    && !getPara("weixinId").equals("")
                    && Parent.dao.find("SELECT * FROM parent WHERE weixinId = ?", getPara("weixinId")).size() != 0) {
                renderText("已有家长使用该微信号!");
            } else if (!Util.getString(parent.getStr("userId")).equals(getUserId(getPara("name")))
                    && Teacher.dao.find("SELECT * FROM teacher WHERE userId = ?", getUserId(getPara("name"))).size() != 0) {
                renderText("已有教师使用该userId!");
            } else if (!Util.getString(parent.getStr("userId")).equals(getUserId(getPara("name")))
                    && Parent.dao.find("SELECT * FROM parent WHERE userId = ?", getUserId(getPara("name"))).size() != 0) {
                renderText("已有家长使用该userId!");
            } else {
                try {
                    if (!Util.getString(parent.getStr("name")).equals(getPara("name").trim())
                            || !Util.getString(parent.getStr("mobile")).equals(getPara("mobile").trim())
                            || !Util.getString(parent.getStr("email")).equals(getPara("email").trim())
                            || !Util.getString(parent.getStr("weixinId")).equals(getPara("weixinId").trim())
                            ) {
                        User user = new User(parent.get("userId").toString(), parent.get("name").toString());
                        user.setMobile(getPara("mobile").trim());
                        user.setEmail(getPara("email").trim());
                        user.setWeixinId(getPara("weixinId").trim());
                        WP.me.updateUser(user);
                    }
                    parent.set("name", getPara("name").trim())
                            .set("mobile", getPara("mobile").trim())
                            .set("email", getPara("email").trim())
                            .set("weixinId", getPara("weixinId").trim())
                            .set("qq", getPara("qq").trim())
                            .set("address", getPara("address").trim())
                            .update();
                    renderText("OK");
                } catch (WeixinException e) {
                    renderText(e.getErrorText());
                }
            }
        }
    }



    public void forParentPersonal() throws WeixinException {
        // 检测session中是否存在teacher
        if (getSessionAttr("parent") == null) {
            // 检测cookie中是否存在EnterpriseId
            if (getCookie("die") == null || getCookie("die").equals("")) {
                // 检测是否来自微信请求
                if (!(getPara("code") == null || getPara("code").equals(""))) {
                    User user = WP.me.getUserByCode(getPara("code"));
                    Parent parent = Parent.dao.findFirst(Parent.dao.getSql("parent.weixin_parent"),user.getUserId(),1);
                    // 检测是否有权限
                    if (parent != null) {
                        setSessionAttr("parent", parent);
                        setCookie("die", parent.getId().toString(), 60 * 30);
                        render("/static/ParentOfPersonal.html");
                    } else {
                        redirect("/");
                    }
                } else {
                    redirect("/");
                }
            } else {
                Parent parent = Parent.dao.findById(getCookie("die"));
                setSessionAttr("parent", parent);
                render("/static/ParentOfPersonal.html");
            }
        } else {
            render("/static/ParentOfPersonal.html");
        }
    }

    public void getRelation() {
        List<Relation> relations = Relation.dao.find(Relation.dao.getSql("relation.parentId"),getPara("parentId"));
        String[] relation = new String[4];
        String[] room_id = new String[4];
        String[] student_id = new String[4];
        String[] identity_id = new String[4];
        String[] roomName = new String[4];
        String[] studentName = new String[4];
        String[] identityName = new String[4];
        String[] roomAble = new String[4];
        String[] studentAble = new String[4];
        String[] identityAble = new String[4];

        if (relations.size() == 1) {
            student_id[0] = relations.get(0).get("student_id").toString();
            studentName[0] = Student.dao.findById(student_id[0]).get("name").toString();
            identity_id[0] = relations.get(0).get("identity_id").toString();
            identityName[0] = Identity.dao.findById(identity_id[0]).get("name").toString();
            if (Student.dao.findById(student_id[0]).get("room_id") == null) {
                room_id[0] = "0";
                roomName[0] = "班级";
            } else {
                room_id[0] = Student.dao.findById(student_id[0]).get("room_id").toString();
                roomName[0] = Room.dao.findById(room_id[0]).get("name").toString();
            }
            roomAble[0] = "true";
            studentAble[0] = "true";
            identityAble[0] = "true";
            for (int k = 1; k < 4; k++) {
                student_id[k] = "0";
                studentName[k] = "学生";
                identity_id[k] = "0";
                identityName[k] = "身份";
                room_id[k] = "0";
                roomName[k] = "班级";
                roomAble[k] = "false";
                studentAble[k] = "false";
                identityAble[k] = "false";
            }
        } else if (relations.size() == 2) {
            for (int i=0 ;i<2;i++){
                student_id[i] = relations.get(i).get("student_id").toString();
                studentName[i] = Student.dao.findById(student_id[i]).get("name").toString();
                identity_id[i] = relations.get(i).get("identity_id").toString();
                identityName[i] = Identity.dao.findById(identity_id[i]).get("name").toString();
                if (Student.dao.findById(student_id[i]).get("room_id") == null) {
                    room_id[i] = "0";
                    roomName[i] = "班级";
                } else {
                    room_id[i] = Student.dao.findById(student_id[i]).get("room_id").toString();
                    roomName[i] = Room.dao.findById(room_id[i]).get("name").toString();
                }
                roomAble[i] = "true";
                studentAble[i] = "true";
                identityAble[i] = "true";
            }
            for (int k = 2; k < 4; k++) {
                student_id[k] = "0";
                studentName[k] = "学生";
                identity_id[k] = "0";
                identityName[k] = "身份";
                room_id[k] = "0";
                roomName[k] = "班级";
                roomAble[k] = "false";
                studentAble[k] = "false";
                identityAble[k] = "false";
            }
        } else if (relations.size() == 3) {
            for (int i=0 ;i<3;i++){
                student_id[i] = relations.get(i).get("student_id").toString();
                studentName[i] = Student.dao.findById(student_id[i]).get("name").toString();
                identity_id[i] = relations.get(i).get("identity_id").toString();
                identityName[i] = Identity.dao.findById(identity_id[i]).get("name").toString();
                if (Student.dao.findById(student_id[i]).get("room_id") == null) {
                    room_id[i] = "0";
                    roomName[i] = "班级";
                } else {
                    room_id[i] = Student.dao.findById(student_id[i]).get("room_id").toString();
                    roomName[i] = Room.dao.findById(room_id[i]).get("name").toString();
                }
                roomAble[i] = "true";
                studentAble[i] = "true";
                identityAble[i] = "true";
            }
            for (int k = 3; k < 4; k++) {
                student_id[k] = "0";
                studentName[k] = "学生";
                identity_id[k] = "0";
                identityName[k] = "身份";
                room_id[k] = "0";
                roomName[k] = "班级";
                roomAble[k] = "false";
                studentAble[k] = "false";
                identityAble[k] = "false";
            }
        } else if (relations.size() == 4) {
            for (int i=0 ;i<4;i++){
                student_id[i] = relations.get(i).get("student_id").toString();
                studentName[i] = Student.dao.findById(student_id[i]).get("name").toString();
                identity_id[i] = relations.get(i).get("identity_id").toString();
                identityName[i] = Identity.dao.findById(identity_id[i]).get("name").toString();
                if (Student.dao.findById(student_id[i]).get("room_id") == null) {
                    room_id[i] = "0";
                    roomName[i] = "班级";
                } else {
                    room_id[i] = Student.dao.findById(student_id[i]).get("room_id").toString();
                    roomName[i] = Room.dao.findById(room_id[i]).get("name").toString();
                }
                roomAble[i] = "true";
                studentAble[i] = "true";
                identityAble[i] = "true";
            }
        } else {
            for (int k = 0; k < 4; k++) {
                student_id[k] = "0";
                studentName[k] = "学生";
                identity_id[k] = "0";
                identityName[k] = "身份";
                room_id[k] = "0";
                roomName[k] = "班级";
                roomAble[k] = "false";
                studentAble[k] = "false";
                identityAble[k] = "false";
            }
        }
        for (int i=0; i<4; i++){
            int j=i+1;
            relation[i] = "'room_id"+j+"':'"+room_id[i]
                    +"','student_id"+j+"':'"+student_id[i]
                    +"','identity_id"+j+"':'"+identity_id[i]
                    +"','roomName"+j+"':'"+roomName[i]
                    +"','studentName"+j+"':'"+studentName[i]
                    +"','identityName"+j+"':'"+identityName[i]
                    +"','roomAble"+j+"':"+roomAble[i]
                    +",'studentAble"+j+"':"+studentAble[i]
                    +",'identityAble"+j+"':"+identityAble[i];
        }
        renderText("{"+relation[0]+","+relation[1]+","+relation[2]+","+relation[3]+"}");
    }

    @Before({Tx.class,AjaxParent.class})
    public void editSelf() {
        Parent parent = Parent.dao.findById(((Parent) getSessionAttr("parent")).getId());
        if (!getPara("name").matches("^[\\u4e00-\\u9fa5]{2,}$")) {
            renderText("家长姓名应为两个以上汉字!");
        } else if (!getPara("mobile").matches("^1(3|4|5|7|8)\\d{9}$")) {
            renderText("手机号码格式错误!");
        } else if (checkMobile(parent,getPara("mobile"))) {
            renderText("该手机号码已使用!");
        } else {
            if (!Util.getString(parent.getStr("name")).equals(getPara("name").trim())
                    || !Util.getString(parent.getStr("mobile")).equals(getPara("mobile").trim())) {
                User user = new User(parent.get("userId").toString(), parent.get("name").toString());
                user.setMobile(getPara("mobile").trim());
                try {
                    WP.me.updateUser(user);
                } catch (WeixinException e) {
                    renderText(e.getErrorText());
                }
            }
            parent.set("name", getPara("name").trim())
                    .set("mobile", getPara("mobile").trim())
                    .update();
            renderText("OK");
        }
    }
    @Before({Tx.class,AjaxParent.class})
    public void deleteForParent() {
        Student student = Student.dao.findById(getPara("id"));
        if (student == null) {
            renderText("要解绑的学生不存在!");
        } else {
            Db.update("delete from relation where parent_id = ? and student_id=?", ((Parent) getSessionAttr("parent")).getId(),getPara("id"));
            renderText("OK");
        }
    }
    @Before({Tx.class,AjaxParent.class})
    public void addForParent() {
        if (!getString(getPara("name")).matches("^[\\u4e00-\\u9fa5]{2,}$")) {
            renderText("学生姓名为两个以上汉字!");
        } else if (!checkIDNumberDetailB(getPara("number"))){
            renderText(checkIDNumberDetail(getPara("number")));
        } else {
            Student student = Student.dao.findFirst("select * from student where name=? and number=? and code=?",getPara("name"),getPara("number"),getPara("code"));
            if (student==null) {
                renderText("未找到指定学生!");
            } else {
                List<Relation> relations = Relation.dao.find(Relation.dao.getSql("relation.getIdentity"),student.getId(),((Parent) getSessionAttr("parent")).getId());
                if (relations.size()!=0){
                    renderText("您已绑定该学生!");
                }else{
                    Relation relation = new Relation();
                    relation.set("student_id",student.getId())
                            .set("parent_id", ((Parent) getSessionAttr("parent")).getId())
                            .set("identity_id",getPara("identity_id"))
                            .save();
                    renderText("OK");
                }
            }
        }
    }
    private String boundString (String studentId,String identityId){
        return "学生："+Student.dao.findFirst(Student.dao.getSql("student.id"),studentId).get("name")
                +"的" + Identity.dao.findFirst(Identity.dao.getSql("identity.id"),identityId).get("name")
                + "已绑定!";
    }
    private String setSuccess (String studentId,String identityId){
        Room room = Room.dao.findById(Roomstudent.dao.findFirst(Roomstudent.dao.getSql("roomStudent.studentId"),studentId).getRoomId());
        return "您已被设为"+room.getYear()+"级"+room.getOrder()+"班"+Student.dao.findFirst(Student.dao.getSql("student.id"),studentId).getName()+"的"+Identity.dao.findFirst(Identity.dao.getSql("identity.id"),identityId).get("name");
    }

    private Boolean checkMobile(Parent parent, String mobile) {
        return !Util.getString(parent.getStr("mobile")).equals(mobile)
                && Teacher.dao.find(Teacher.dao.getSql("teacher.mobile"), mobile).size() != 0
                && Parent.dao.find(Parent.dao.getSql("parent.mobile"), mobile).size() != 0;
    }
    private void sendMessage(Parent parent, String studentId, String identityId) throws WeixinException {
        if (!studentId.equals("0") && !identityId.equals("0")
                && Relation.dao.find(Relation.dao.getSql("relation.getParent")
                , studentId, identityId).size() == 0) {
            Relation relation = new Relation();
            relation.set("parent_id", parent.get("id"))
                    .set("student_id", studentId)
                    .set("identity_id", identityId).save();
            if (parent.getState() == 1) {
                WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.parentId, new Text(setSuccess(studentId, identityId)), new IdParameter().putUserIds(parent.getUserId()), false));
            }
        }
    }
}
