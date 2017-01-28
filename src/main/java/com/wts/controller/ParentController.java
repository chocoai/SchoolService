package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.WP;
import com.wts.entity.model.*;
import com.wts.interceptor.AjaxFunction;
import com.wts.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ParentController extends Controller {

    public void index() throws WeixinException {
        // 检测session中是否存在teacher
        if (getSessionAttr("teacher") == null) {
            // 检测cookie中是否存在EnterpriseId
            if (getCookie("die") == null || getCookie("die").equals("")) {
                // 检测是否来自微信请求
                if (!(getPara("code") == null || getPara("code").equals(""))) {
                    User user = WP.me.getUserByCode(getPara("code"));
                    Enterprise teacher = Enterprise.dao.findFirst("select * from enterprise where state=1 and userId=?", user.getUserId());
                    setSessionAttr("teacher", teacher);
                    render("/static/ParentManage.html");
                } else {
                    redirect("/");
                }
            } else {
                Enterprise teacher = Enterprise.dao.findById(getCookie("die"));
                setSessionAttr("teacher", teacher);
                render("/static/ParentManage.html");
            }
        } else {
            render("/static/ParentManage.html");
        }
    }
    @Before(AjaxFunction.class)
    public void queryParent() {
        Page<Enterprise> parents= Enterprise.dao.parentQuery(getParaToInt("pageCurrent"),getParaToInt("pageSize"),getPara("queryString"));
        renderJson(parents.getList());
    }
    @Before(AjaxFunction.class)
    public void totalByName() {
        Long count = Db.queryLong("select count(*) from enterprise where isParent=1 and (name like '%"+ getPara("queryString") +"%' or mobile LIKE '%"+getPara("queryString")+"%' or userId LIKE '%"+getPara("queryString")+"%') ORDER BY name ASC");
        if (count%getParaToInt("pageSize")==0) {
            renderText((count/getParaToInt("pageSize"))+"");
        } else {
            renderText((count/getParaToInt("pageSize")+1)+"");
        }
    }

    public void getRelation() {
        List<Relation> relations = Relation.dao.find("select * from relation where parent_id=? order by id asc",getPara("id"));
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
                room_id[0] = "";
                roomName[0] = "班级";
            } else {
                room_id[0] = Student.dao.findById(student_id[0]).get("room_id").toString();
                roomName[0] = Room.dao.findById(room_id[0]).get("name").toString();
            }
            roomAble[0] = "true";
            studentAble[0] = "true";
            identityAble[0] = "true";
            for (int k = 1; k < 4; k++) {
                student_id[k] = "";
                studentName[k] = "姓名";
                identity_id[k] = "";
                identityName[k] = "身份";
                room_id[k] = "";
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
                    room_id[i] = "";
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
                student_id[k] = "";
                studentName[k] = "姓名";
                identity_id[k] = "";
                identityName[k] = "身份";
                room_id[k] = "";
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
                    room_id[i] = "";
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
                student_id[k] = "";
                studentName[k] = "姓名";
                identity_id[k] = "";
                identityName[k] = "身份";
                room_id[k] = "";
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
                    room_id[i] = "";
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

        }
        for (int i=0; i<4; i++){
            relation[i] = "{'room_id':'"+room_id[i]
                    +"','student_id':'"+student_id[i]
                    +"','identity_id':'"+identity_id[i]
                    +"','roomName':'"+roomName[i]
                    +"','studentName':'"+studentName[i]
                    +"','identityName':'"+identityName[i]
                    +"','roomAble':"+roomAble[i]
                    +",'studentAble':"+studentAble[i]
                    +",'identityAble':"+identityAble[i]+"}";
        }
        renderText("'relation1':"+relation[0]+"},{'relation2':"+relation[1]+"},{'relation3':"+relation[2]+"},{'relation4':"+relation[3]+"");
    }
    @Before({Tx.class,AjaxFunction.class})
    public void save()  {
        if (!Util.getString(getPara("name")).matches("^[\\u4e00-\\u9fa5]{2,}$")) {
            renderText("教师姓名应为两个以上汉字!");
        } else if (!Util.getString(getPara("mobile")).matches("^1(3|4|5|7|8)\\d{9}$")) {
            renderText("手机号码格式不正确!");
        } else if (Enterprise.dao.find("select * from enterprise where mobile=?", getPara("mobile")).size()!=0) {
            renderText("该手机号码已存在!");
        } else if (!Util.getString(getPara("userId")).matches("^[A-Za-z0-9]+$")) {
            renderText("账号名应为字母或数字的组合!");
        } else if (Enterprise.dao.find("select * from enterprise where userId=?", getPara("userId")).size()!=0) {
            renderText("该账号名已存在!");
        } else if(Relation.dao.find("select * from relation where student_id=? and identity_id=?"
                ,getPara("student_id1"),getPara("identity_id1")).size()!=0) {
            renderText("学生1："+Student.dao.findFirst("select * from student where id=?",getPara("student_id1")).get("name")
                    +"的" + Identity.dao.findFirst("select * from identity where id=?",getPara("identity_id1")).get("name")
                    + "已绑定!");
        } else if(Relation.dao.find("select * from relation where student_id=? and identity_id=?"
                ,getPara("student_id2"),getPara("identity_id2")).size()!=0) {
            renderText("学生2："+Student.dao.findFirst("select * from student where id=?",getPara("student_id2")).get("name")
                    +"的" + Identity.dao.findFirst("select * from identity where id=?",getPara("identity_id2")).get("name")
                    + "已绑定!");
        } else if(Relation.dao.find("select * from relation where student_id=? and identity_id=?"
                ,getPara("student_id3"),getPara("identity_id3")).size()!=0) {
            renderText("学生3："+Student.dao.findFirst("select * from student where id=?",getPara("student_id3")).get("name")
                    +"的" + Identity.dao.findFirst("select * from identity where id=?",getPara("identity_id3")).get("name")
                    + "已绑定!");
        } else if(Relation.dao.find("select * from relation where student_id=? and identity_id=?"
                ,getPara("student_id4"),getPara("identity_id4")).size()!=0) {
            renderText("学生4："+Student.dao.findFirst("select * from student where id=?",getPara("student_id4")).get("name")
                    +"的" + Identity.dao.findFirst("select * from identity where id=?",getPara("identity_id4")).get("name")
                    + "已绑定!");
        } else {
            User user = new User(getPara("userId").trim(),getPara("name").trim());
            user.setMobile(getPara("mobile").trim());
            user.setPartyIds(1);
            try{
                WP.me.createUser(user);
                Enterprise parent = new Enterprise();
                parent.set("name", getPara("name").trim())
                        .set("mobile", getPara("mobile").trim())
                        .set("userId", getPara("userId").trim())
                        .set("state", 2)
                        .set("isTeacher", 0)
                        .set("isManager",0)
                        .set("isParent",1)
                        .save();
                if (!getPara("student_id1").equals("") && !getPara("identity_id1").equals("")) {
                    Relation relation = new Relation();
                    relation.set("parent_id", parent.get("id"))
                            .set("student_id", getPara("student_id1"))
                            .set("identity_id", getPara("identity_id1")).save();
                }
                if (!getPara("student_id2").equals("") && !getPara("identity_id2").equals("")) {
                    Relation relation = new Relation();
                    relation.set("parent_id", parent.getId())
                            .set("student_id", getPara("student_id2"))
                            .set("identity_id", getPara("identity_id2")).save();
                }
                if (!getPara("student_id3").equals("") && !getPara("identity_id3").equals("")) {
                    Relation relation = new Relation();
                    relation.set("parent_id", parent.getId())
                            .set("student_id", getPara("student_id3"))
                            .set("identity_id", getPara("identity_id3")).save();
                }
                if (!getPara("student_id4").equals("") && !getPara("identity_id4").equals("")) {
                    Relation relation = new Relation();
                    relation.set("parent_id", parent.getId())
                            .set("student_id", getPara("student_id4"))
                            .set("identity_id", getPara("identity_id4")).save();
                }
                renderText("OK");
            }catch(WeixinException e){
                renderText(e.getErrorText());
            }
        }
    }
    @Before({Tx.class,AjaxFunction.class})
    public void edit(){
        Parent parent = Parent.dao.findById(getPara("id"));
        if (parent == null) {
            renderText("要修改的家长不存在!");
        } else {
            renderText("要修改的家长不存在!");
        }
    }
}
