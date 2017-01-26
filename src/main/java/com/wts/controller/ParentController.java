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
    public void identityList() {
        List<Identity> identities = Identity.dao.find("select * from identity order by id asc");
        renderJson(identities);
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
    @Before(AjaxFunction.class)
    public void getIdentityName(){
        Identity identity = Identity.dao.findById(getPara("id"));
        renderText(identity.get("name").toString());
    }
    @Before(AjaxFunction.class)
    public void getById() {
        Enterprise parent = Enterprise.dao.findById(getPara("id"));
        renderJson(parent);
    }
    @Before(AjaxFunction.class)
    public void deleteById() {
        Enterprise parent = Enterprise.dao.findById(getPara("id"));
        if (parent == null) {
            renderText("要取消关注的家长不存在!");
        } else if (parent.get("state").toString().equals("4")) {
            renderText("该家长已处于取消关注状态!");
        }  else if (parent.get("state").toString().equals("3")) {
            renderText("该家长已处于冻结状态!");
        } else {
            try {
                WP.me.deleteUser(parent.getUserId());
                parent.set("state",4).update();
                renderText("OK");
            } catch (WeixinException e) {
                renderText(e.getErrorText());
            }
        }
    }
    @Before(AjaxFunction.class)
    public void resaveById() {
        Enterprise parent = Enterprise.dao.findById(getPara("id"));
        if (parent == null) {
            renderText("要重新邀请关注的家长不存在!");
        } else if (parent.get("state").toString().equals("2")) {
            renderText("该家长已处于关注状态!");
        } else {
            User user = new User(parent.get("userId").toString(),parent.get("name").toString());
            user.setMobile(parent.get("mobile").toString());
            user.setPartyIds(1);
            try {
                WP.me.createUser(user);
                parent.set("state",2).update();
                renderText("OK");
            } catch (WeixinException e) {
                renderText(e.getErrorText());
            }
        }
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
                    relation.set("parent_id", parent.getId())
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
}
