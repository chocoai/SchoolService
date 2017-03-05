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
import com.wts.interceptor.AjaxManager;
import com.wts.interceptor.AjaxParent;
import com.wts.interceptor.AjaxTeacher;
import com.wts.util.ParamesAPI;
import com.wts.util.Util;

import java.util.ArrayList;
import java.util.List;

import static com.wts.util.IDNumber.checkIDNumberDetail;
import static com.wts.util.IDNumber.checkIDNumberDetailB;
import static com.wts.util.Util.getString;

public class ParentController extends Controller {

    /**
     * 登录到家长页面
     */
    public void forManager() throws WeixinException {
        // 检测session中是否存在teacher
        if (getSessionAttr("manager") == null || ((Teacher)getSessionAttr("manager")).getIsManager()!=1) {
            // 检测cookie中是否存在EnterpriseId
            if (getCookie("die") == null || getCookie("die").equals("")) {
                // 检测是否来自微信请求
                if (!(getPara("code") == null || getPara("code").equals(""))) {
                    User user = WP.me.getUserByCode(getPara("code"));
                    Teacher teacher = Teacher.dao.findFirst(Teacher.dao.getSql("teacher.weixin_manager"),user.getUserId(),1);
                    // 检测是否有权限
                    if (teacher != null) {
                        setSessionAttr("manager", teacher);
                        setCookie("die", teacher.getId().toString(), 60 * 30);
                        render("/static/ManagerOfParent.html");
                    } else {
                        redirect("/");
                    }
                } else {
                    redirect("/");
                }
            } else {
                Teacher teacher = Teacher.dao.findById(getCookie("die"));
                setSessionAttr("manager", teacher);
                render("/static/ManagerOfParent.html");
            }
        } else {
            render("/static/ManagerOfParent.html");
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
    /**
     * 查询
     */
    @Before(AjaxManager.class)
    public void query() {
        renderJson(Parent.dao.paginate(getParaToInt("pageCurrent"),getParaToInt("pageSize"),"SELECT *","FROM parent WHERE name LIKE '%?%' OR userId LIKE '%?%' OR mobile LIKE '%?%' ORDER BY name ASC",getPara("queryString"),getPara("queryString"),getPara("queryString")).getList());
    }
    /**
     * 计数
     */
    @Before(AjaxManager.class)
    public void total() {
        Long count = Db.queryLong("SELECT count(*) FROM parent WHERE name LIKE '%?%' OR mobile LIKE '%?%' OR userId LIKE '%?%' ORDER BY name ASC", getPara("queryString"), getPara("queryString"), getPara("queryString"));
        if (count % getParaToInt("pageSize") == 0) {
            renderText((count / getParaToInt("pageSize")) + "");
        } else {
            renderText((count / getParaToInt("pageSize") + 1) + "");
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
    @Before({Tx.class,AjaxManager.class})
    public void save()  {
        if (!Util.getString(getPara("name")).matches("^[\\u4e00-\\u9fa5]{2,}$")) {
            renderText("家长姓名应为两个以上汉字!");
        } else if (!Util.getString(getPara("mobile")).matches("^1(3|4|5|7|8)\\d{9}$")) {
            renderText("手机号码格式不正确!");
        } else if (!Util.getString(getPara("userId")).matches("^[A-Za-z0-9]+$")) {
            renderText("账号名应为字母或数字的组合!");
        } else if (Teacher.dao.find(Teacher.dao.getSql("teacher.mobile"), getPara("mobile")).size() != 0) {
            renderText("已有教师使用该手机号码!");
        } else if (Parent.dao.find(Parent.dao.getSql("parent.mobile"), getPara("mobile")).size() != 0) {
            renderText("已有家长使用该手机号码!");
        } else if(Relation.dao.find(Relation.dao.getSql("relation.getParent") ,getPara("student_id1"),getPara("identity_id1")).size()!=0) {
            renderText(boundString(getPara("student_id1"),getPara("identity_id1")));
        } else if(Relation.dao.find(Relation.dao.getSql("relation.getParent") ,getPara("student_id2"),getPara("identity_id2")).size()!=0) {
            renderText(boundString(getPara("student_id2"),getPara("identity_id2")));
        } else if(Relation.dao.find(Relation.dao.getSql("relation.getParent") ,getPara("student_id3"),getPara("identity_id3")).size()!=0) {
            renderText(boundString(getPara("student_id3"),getPara("identity_id3")));
        } else if(Relation.dao.find(Relation.dao.getSql("relation.getParent") ,getPara("student_id4"),getPara("identity_id4")).size()!=0) {
            renderText(boundString(getPara("student_id4"),getPara("identity_id4")));
        } else {
            User user = new User(getPara("userId").trim(),getPara("name").trim());
            user.setMobile(getPara("mobile").trim());
            user.setPartyIds(1);
            try{
                WP.me.createUser(user);
                List<String> userIds = new ArrayList<String>();
                userIds.add(getPara("userId").trim());
                WP.me.addTagUsers(ParamesAPI.parentTagId,userIds,new ArrayList<Integer>());
                Parent parent = new Parent();
                parent.set("name", getPara("name").trim())
                        .set("mobile", getPara("mobile").trim())
                        .set("userId", getPara("userId").trim())
                        .set("state", 4)
                        .save();
                if (!getPara("student_id1").equals("0") && !getPara("identity_id1").equals("0")
                        && Relation.dao.find(Relation.dao.getSql("relation.getParent")
                        ,getPara("student_id1"),getPara("identity_id1")).size()==0) {
                    Relation relation = new Relation();
                    relation.set("parent_id", parent.get("id"))
                            .set("student_id", getPara("student_id1"))
                            .set("identity_id", getPara("identity_id1")).save();
                    if (parent.getState()==1){
                        try {
                            WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.parentId, new Text("您已被设为"+Room.dao.findById(Roomstudent.dao.findFirst(Roomstudent.dao.getSql("roomStudent.studentId"),getPara("student_id1")).getRoomId()).getName()+Student.dao.findFirst(Student.dao.getSql("student.id"),getPara("student_id1")).getName()+"的"+Identity.dao.findFirst(Identity.dao.getSql("identity.id"),getPara("identity_id1")).get("name")), new IdParameter().putUserIds(parent.getUserId()), false));
                        } catch (Exception e) {
                            renderText(e.getMessage());
                        }
                    }
                }
                if (!getPara("student_id2").equals("0") && !getPara("identity_id2").equals("0")
                        && Relation.dao.find(Relation.dao.getSql("relation.getParent")
                        ,getPara("student_id2"),getPara("identity_id2")).size()==0) {
                    Relation relation = new Relation();
                    relation.set("parent_id", parent.getId())
                            .set("student_id", getPara("student_id2"))
                            .set("identity_id", getPara("identity_id2")).save();
                    if (parent.getState()==1){
                        try {
                            WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.parentId, new Text("您已被设为"+Room.dao.findById(Student.dao.findFirst("select * from student where id=?",getPara("student_id2")).getRoomId()).getName()+Student.dao.findFirst("select * from student where id=?",getPara("student_id2")).get("name")+"的"+Identity.dao.findFirst("select * from identity where id=?",getPara("identity_id2")).get("name")), new IdParameter().putUserIds(parent.getUserId()), false));
                        } catch (Exception e) {
                            renderText(e.getMessage());
                        }
                    }
                }
                if (!getPara("student_id3").equals("0") && !getPara("identity_id3").equals("0")
                        && Relation.dao.find(Relation.dao.getSql("relation.getParent")
                        ,getPara("student_id3"),getPara("identity_id3")).size()==0) {
                    Relation relation = new Relation();
                    relation.set("parent_id", parent.getId())
                            .set("student_id", getPara("student_id3"))
                            .set("identity_id", getPara("identity_id3")).save();
                    if (parent.getState()==1){
                        try {
                            WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.parentId, new Text("您已被设为"+Room.dao.findById(Student.dao.findFirst("select * from student where id=?",getPara("student_id3")).getRoomId()).getName()+Student.dao.findFirst("select * from student where id=?",getPara("student_id3")).get("name")+"的"+Identity.dao.findFirst("select * from identity where id=?",getPara("identity_id3")).get("name")), new IdParameter().putUserIds(parent.getUserId()), false));
                        } catch (Exception e) {
                            renderText(e.getMessage());
                        }
                    }
                }
                if (!getPara("student_id4").equals("0") && !getPara("identity_id4").equals("0")
                        && Relation.dao.find(Relation.dao.getSql("relation.getParent")
                        ,getPara("student_id4"),getPara("identity_id4")).size()==0) {
                    Relation relation = new Relation();
                    relation.set("parent_id", parent.getId())
                            .set("student_id", getPara("student_id4"))
                            .set("identity_id", getPara("identity_id4")).save();
                    if (parent.getState()==1){
                        try {
                            WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.parentId, new Text("您已被设为"+Room.dao.findById(Student.dao.findFirst("select * from student where id=?",getPara("student_id4")).getRoomId()).getName()+Student.dao.findFirst("select * from student where id=?",getPara("student_id4")).get("name")+"的"+Identity.dao.findFirst("select * from identity where id=?",getPara("identity_id4")).get("name")), new IdParameter().putUserIds(parent.getUserId()), false));
                        } catch (Exception e) {
                            renderText(e.getMessage());
                        }
                    }
                }
                renderText("OK");
            }catch(WeixinException e){
                renderText(e.getErrorText());
            }
        }
    }
    @Before({Tx.class,AjaxManager.class})
    public void edit(){
        Enterprise parent = Enterprise.dao.findById(getPara("id"));
        if (parent == null) {
            renderText("要修改的家长不存在!");
        } else {
            if (!getPara("name").matches("^[\\u4e00-\\u9fa5]{2,}$")) {
                renderText("家长姓名应为两个以上汉字!");
            } else if (!getPara("mobile").matches("^1(3|4|5|7|8)\\d{9}$")) {
                renderText("手机号码格式错误!");
            } else if (!Util.getString(parent.getStr("mobile")).equals(getPara("mobile"))
                    &&  Enterprise.dao.find("select * from enterprise where mobile=?", getPara("mobile")).size()!=0) {
                renderText("该手机号码已存在!");
            } else {
                try {
                    Db.update("delete from relation where parent_id = ?", getPara("id"));
                    if (!getPara("student_id1").equals("0") && !getPara("identity_id1").equals("0")
                            && Relation.dao.find("select * from relation where student_id=? and identity_id=?"
                            ,getPara("student_id1"),getPara("identity_id1")).size()==0) {
                        Relation relation = new Relation();
                        relation.set("parent_id", parent.get("id"))
                                .set("student_id", getPara("student_id1"))
                                .set("identity_id", getPara("identity_id1")).save();
                        if (parent.getState()==1){
                            try {
                                WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.parentId, new Text("您已被设为"+Room.dao.findById(Student.dao.findFirst("select * from student where id=?",getPara("student_id1")).getRoomId()).getName()+Student.dao.findFirst("select * from student where id=?",getPara("student_id1")).get("name")+"的"+Identity.dao.findFirst("select * from identity where id=?",getPara("identity_id1")).get("name")), new IdParameter().putUserIds(parent.getUserId()), false));
                            } catch (Exception e) {
                                renderText(e.getMessage());
                            }
                        }
                    }
                    if (!getPara("student_id2").equals("0") && !getPara("identity_id2").equals("0")
                            && Relation.dao.find("select * from relation where student_id=? and identity_id=?"
                            ,getPara("student_id2"),getPara("identity_id2")).size()==0) {
                        Relation relation = new Relation();
                        relation.set("parent_id", parent.getId())
                                .set("student_id", getPara("student_id2"))
                                .set("identity_id", getPara("identity_id2")).save();
                        if (parent.getState()==1){
                            try {
                                WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.parentId, new Text("您已被设为"+Room.dao.findById(Student.dao.findFirst("select * from student where id=?",getPara("student_id2")).getRoomId()).getName()+Student.dao.findFirst("select * from student where id=?",getPara("student_id2")).get("name")+"的"+Identity.dao.findFirst("select * from identity where id=?",getPara("identity_id2")).get("name")), new IdParameter().putUserIds(parent.getUserId()), false));
                            } catch (Exception e) {
                                renderText(e.getMessage());
                            }
                        }
                    }
                    if (!getPara("student_id3").equals("0") && !getPara("identity_id3").equals("0")
                            && Relation.dao.find("select * from relation where student_id=? and identity_id=?"
                            ,getPara("student_id3"),getPara("identity_id3")).size()==0) {
                        Relation relation = new Relation();
                        relation.set("parent_id", parent.getId())
                                .set("student_id", getPara("student_id3"))
                                .set("identity_id", getPara("identity_id3")).save();
                        if (parent.getState()==1){
                            try {
                                WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.parentId, new Text("您已被设为"+Room.dao.findById(Student.dao.findFirst("select * from student where id=?",getPara("student_id3")).getRoomId()).getName()+Student.dao.findFirst("select * from student where id=?",getPara("student_id3")).get("name")+"的"+Identity.dao.findFirst("select * from identity where id=?",getPara("identity_id3")).get("name")), new IdParameter().putUserIds(parent.getUserId()), false));
                            } catch (Exception e) {
                                renderText(e.getMessage());
                            }
                        }
                    }
                    if (!getPara("student_id4").equals("0") && !getPara("identity_id4").equals("0")
                            && Relation.dao.find("select * from relation where student_id=? and identity_id=?"
                            ,getPara("student_id4"),getPara("identity_id4")).size()==0) {
                        Relation relation = new Relation();
                        relation.set("parent_id", parent.getId())
                                .set("student_id", getPara("student_id4"))
                                .set("identity_id", getPara("identity_id4")).save();
                        if (parent.getState()==1){
                            try {
                                WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.parentId, new Text("您已被设为"+Room.dao.findById(Student.dao.findFirst("select * from student where id=?",getPara("student_id4")).getRoomId()).getName()+Student.dao.findFirst("select * from student where id=?",getPara("student_id4")).get("name")+"的"+Identity.dao.findFirst("select * from identity where id=?",getPara("identity_id4")).get("name")), new IdParameter().putUserIds(parent.getUserId()), false));
                            } catch (Exception e) {
                                renderText(e.getMessage());
                            }
                        }
                    }
                    if (!Util.getString(parent.getStr("name")).equals(getPara("name").trim())
                            || !Util.getString(parent.getStr("mobile")).equals(getPara("mobile").trim())
                            ) {
                        parent.set("name", getPara("name").trim())
                                .set("mobile", getPara("mobile").trim())
                                .update();
                        User user = new User(parent.get("userId").toString(), parent.get("name").toString());
                        user.setMobile(getPara("mobile").trim());
                        WP.me.updateUser(user);
                    }
                    renderText("OK");
                } catch (WeixinException e) {
                    renderText(e.getErrorText());
                }
            }
        }
    }
    @Before({Tx.class,AjaxParent.class})
    public void editSelf() {
        Enterprise parent = Enterprise.dao.findById(((Enterprise) getSessionAttr("parent")).getId());
        if (!getPara("name").matches("^[\\u4e00-\\u9fa5]{2,}$")) {
            renderText("家长姓名应为两个以上汉字!");
        } else if (!getPara("mobile").matches("^1(3|4|5|7|8)\\d{9}$")) {
            renderText("手机号码格式错误!");
        } else if (!Util.getString(parent.getStr("mobile")).equals(getPara("mobile"))
                && Enterprise.dao.find("select * from enterprise where mobile=?", getPara("mobile")).size() != 0) {
            renderText("该手机号码已存在!");
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
            Db.update("delete from relation where parent_id = ? and student_id=?", ((Enterprise) getSessionAttr("parent")).getId(),getPara("id"));
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
                List<Relation> relations = Relation.dao.find("select * from relation where student_id=? and parent_id=?",student.getId(), ((Enterprise) getSessionAttr("parent")).getId());
                if (relations.size()!=0){
                    renderText("您已绑定该学生!");
                }else{
                    Relation relation = new Relation();
                    relation.set("student_id",student.getId())
                            .set("parent_id", ((Enterprise) getSessionAttr("parent")).getId())
                            .set("identity_id",getPara("identity_id"))
                            .save();
                    renderText("OK");
                }
            }
        }
    }
    public String boundString (String studentId,String identityId){
        return "学生："+Student.dao.findFirst(Student.dao.getSql("student.id"),studentId).get("name")
                +"的" + Identity.dao.findFirst(Identity.dao.getSql("identity.id"),identityId).get("name")
                + "已绑定!";
    }
}
