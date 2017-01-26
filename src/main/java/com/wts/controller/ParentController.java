package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.wts.entity.WP;
import com.wts.entity.model.Enterprise;
import com.wts.entity.model.Identity;
import com.wts.entity.model.Room;
import com.wts.interceptor.AjaxFunction;

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
        List<Identity> identities = Identity.dao.find("select * from identity order by name asc");
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
}
