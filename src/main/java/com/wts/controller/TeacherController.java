package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.WP;
import com.wts.entity.model.Parent;
import com.wts.entity.model.Teacher;
import com.wts.interceptor.AjaxManager;
import com.wts.interceptor.AjaxTeacher;
import com.wts.util.ParamesAPI;
import com.wts.util.Util;

import java.util.ArrayList;
import java.util.List;

import static com.wts.util.Util.getUserId;

public class TeacherController extends Controller {
    /**
     * 登录到教师页面
     */
    public void forManager() throws WeixinException {
        // 检测session中是否存在teacher
        if (getSessionAttr("manager") == null || ((Teacher) getSessionAttr("manager")).getIsManager() != 1) {
            // 检测cookie中是否存在EnterpriseId
            if (getCookie("die") == null || getCookie("die").equals("")) {
                // 检测是否来自微信请求
                if (!(getPara("code") == null || getPara("code").equals(""))) {
                    User user = WP.me.getUserByCode(getPara("code"));
                    Teacher teacher = Teacher.dao.findFirst(Teacher.dao.getSql("teacher.weixin_manager"), user.getUserId(), 1);
                    // 检测是否有权限
                    if (teacher != null) {
                        setSessionAttr("manager", teacher);
                        setCookie("die", teacher.getId().toString(), 60 * 30);
                        render("/static/ManagerOfTeacher.html");
                    } else {
                        redirect("/");
                    }
                } else {
                    redirect("/");
                }
            } else {
                Teacher teacher = Teacher.dao.findById(getCookie("die"));
                setSessionAttr("manager", teacher);
                render("/static/ManagerOfTeacher.html");
            }
        } else {
            render("/static/ManagerOfTeacher.html");
        }
    }

    /**
     * 登录到教师个人页面
     */
    public void forTeacherPersonal() throws WeixinException {
        // 检测session中是否存在teacher
        if (getSessionAttr("teacher") == null) {
            // 检测cookie中是否存在EnterpriseId
            if (getCookie("die") == null || getCookie("die").equals("")) {
                // 检测是否来自微信请求
                if (!(getPara("code") == null || getPara("code").equals(""))) {
                    User user = WP.me.getUserByCode(getPara("code"));
                    Teacher teacher = Teacher.dao.findFirst(Teacher.dao.getSql("teacher.weixin_teacher"), user.getUserId(), 1);
                    // 检测是否有权限
                    if (teacher != null) {
                        setSessionAttr("teacher", teacher);
                        setCookie("die", teacher.getId().toString(), 60 * 30);
                        render("/static/TeacherOfPersonal.html");
                    } else {
                        redirect("/");
                    }
                } else {
                    redirect("/");
                }
            } else {
                Teacher teacher = Teacher.dao.findById(getCookie("die"));
                setSessionAttr("teacher", teacher);
                render("/static/TeacherOfPersonal.html");
            }
        } else {
            render("/static/TeacherOfPersonal.html");
        }
    }

    /**
     * 保存
     */
    @Before({Tx.class, AjaxManager.class})
    public void save() {
        if (!getPara("name").matches("^[\\u4e00-\\u9fa5]{2,}$")) {
            renderText("教师姓名应为两个以上汉字!");
        } else if (!getPara("mobile").matches("^1(3|4|5|7|8)\\d{9}$")) {
            renderText("手机号码格式错误!");
        } else if (Teacher.dao.find(Teacher.dao.getSql("teacher.mobile"), getPara("mobile")).size() != 0) {
            renderText("已有教师使用该手机号码!");
        } else if (Parent.dao.find(Parent.dao.getSql("parent.mobile"), getPara("mobile")).size() != 0) {
            renderText("已有家长使用该手机号码!");
        } else {
            User user = new User(getUserId(getPara("name")), getPara("name").trim());
            user.setMobile(getPara("mobile").trim());
            user.setPartyIds(1);
            try {
                WP.me.createUser(user);
                List<String> userIds = new ArrayList<String>();
                userIds.add(getUserId(getPara("name")));
                WP.me.addTagUsers(ParamesAPI.teacherTagId, userIds, new ArrayList<Integer>());
                if (getPara("isManager").trim().equals("1")) {
                    WP.me.addTagUsers(ParamesAPI.managerTagId, userIds, new ArrayList<Integer>());
                }
                if (getPara("isParent").trim().equals("1")) {
                    WP.me.addTagUsers(ParamesAPI.parentTagId, userIds, new ArrayList<Integer>());
                    Parent parent = new Parent();
                    parent.set("name", getPara("name").trim())
                            .set("mobile", getPara("mobile").trim())
                            .set("userId", getUserId(getPara("name")))
                            .set("pass", "wts")
                            .set("state", 4)
                            .save();
                }
                Teacher teacher = new Teacher();
                teacher.set("name", getPara("name").trim())
                        .set("mobile", getPara("mobile").trim())
                        .set("userId", getUserId(getPara("name")))
                        .set("pass", "wts")
                        .set("state", 4)
                        .set("isManager", getPara("isManager").trim())
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
        Teacher teacher = Teacher.dao.findById(getPara("id"));
        if (teacher == null) {
            renderText("要修改的教师不存在!");
        } else {
            if (Util.getString(teacher.getStr("name")).equals(getPara("name").trim())
                    && Util.getString(teacher.getStr("mobile")).equals(getPara("mobile").trim())
                    && Util.getString(teacher.get("isManager").toString()).equals(getPara("isManager").trim())
                    ) {
                renderText("未找到修改内容!");
            } else if (!getPara("name").matches("^[\\u4e00-\\u9fa5]{2,}$")) {
                renderText("教师姓名应为两个以上汉字!");
            } else if (!getPara("mobile").matches("^1(3|4|5|7|8)\\d{9}$")) {
                renderText("手机号码格式错误!");
            } else if (!Util.getString(teacher.getStr("mobile")).equals(getPara("mobile"))
                    && Teacher.dao.find(Teacher.dao.getSql("teacher.mobile"), getPara("mobile")).size() != 0
                    && Parent.dao.find(Parent.dao.getSql("parent.mobile"), getPara("mobile")).size() != 0) {
                renderText("该手机号码已存在!");
            } else {
                try {
                    if (!Util.getString(teacher.getStr("name")).equals(getPara("name").trim())
                            || !Util.getString(teacher.getStr("mobile")).equals(getPara("mobile").trim())) {
                        User user = new User(teacher.get("userId").toString(), teacher.get("name").toString());
                        user.setMobile(getPara("mobile").trim());
                        WP.me.updateUser(user);
                    }
                    List<String> userIds = new ArrayList<String>();
                    userIds.add(teacher.get("userId").toString().trim());
                    if (!Util.getString(teacher.get("isManager").toString()).equals(getPara("isManager").trim())) {
                        if (getPara("isManager").trim().equals("1")) {
                            WP.me.addTagUsers(ParamesAPI.managerTagId, userIds, new ArrayList<Integer>());
                        } else {
                            WP.me.deleteTagUsers(ParamesAPI.managerTagId, userIds, new ArrayList<Integer>());
                        }
                    }
                    teacher.set("name", getPara("name").trim())
                            .set("mobile", getPara("mobile").trim())
                            .set("isManager", getPara("isManager").trim())
                            .update();
                    renderText("OK");
                } catch (WeixinException e) {
                    renderText(e.getErrorText());
                }
            }
        }
    }

    /**
     * 自主修改
     */
    @Before({Tx.class, AjaxTeacher.class})
    public void editSelf() {
        Teacher teacher = Teacher.dao.findById(((Teacher) getSessionAttr("teacher")).getId());
        if (!getPara("name").matches("^[\\u4e00-\\u9fa5]{2,}$")) {
            renderText("教师姓名应为两个以上汉字!");
        } else if (!getPara("mobile").matches("^1(3|4|5|7|8)\\d{9}$")) {
            renderText("手机号码格式错误!");
        } else if (!Util.getString(teacher.getStr("mobile")).equals(getPara("mobile"))
                && Teacher.dao.find(Teacher.dao.getSql("teacher.mobile"), getPara("mobile")).size() != 0
                && Parent.dao.find(Parent.dao.getSql("parent.mobile"), getPara("mobile")).size() != 0) {
            renderText("该手机号码已存在!");
        } else {
            if (!Util.getString(teacher.getStr("name")).equals(getPara("name").trim())
                    || !Util.getString(teacher.getStr("mobile")).equals(getPara("mobile").trim())) {
                User user = new User(teacher.get("userId").toString(), teacher.get("name").toString());
                user.setMobile(getPara("mobile").trim());
                try {
                    WP.me.updateUser(user);
                } catch (WeixinException e) {
                    renderText(e.getErrorText());
                }
            }
            teacher.set("name", getPara("name").trim())
                    .set("mobile", getPara("mobile").trim())
                    .update();
            renderText("OK");
        }
    }

    /**
     * 查询
     */
    @Before(AjaxManager.class)
    public void query() {
        renderJson(Teacher.dao.paginate(getParaToInt("pageCurrent"), getParaToInt("pageSize"), "SELECT *", "FROM teacher WHERE name LIKE '%?%' OR userId LIKE '%?%' OR mobile LIKE '%?%' ORDER BY name ASC", getPara("queryString"), getPara("queryString"), getPara("queryString")).getList());
    }

    /**
     * 计数
     */
    @Before(AjaxManager.class)
    public void total() {
        Long count = Db.queryLong("SELECT count(*) FROM teacher WHERE name LIKE '%?%' OR mobile LIKE '%?%' OR userId LIKE '%?%' ORDER BY name ASC", getPara("queryString"), getPara("queryString"), getPara("queryString"));
        if (count % getParaToInt("pageSize") == 0) {
            renderText((count / getParaToInt("pageSize")) + "");
        } else {
            renderText((count / getParaToInt("pageSize") + 1) + "");
        }
    }

    /**
     * 列表
     */
    @Before(AjaxManager.class)
    public void list() {
        renderJson(Teacher.dao.find(Teacher.dao.getSql("teacher.list")));
    }
}
