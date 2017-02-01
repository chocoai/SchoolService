package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.wts.entity.model.Identity;
import com.wts.interceptor.AjaxManager;

import java.util.List;

public class IdentityController extends Controller {

    @Before(AjaxManager.class)
    public void identityList() {
        List<Identity> identities = Identity.dao.find("select * from identity order by id asc");
        renderJson(identities);
    }
    @Before(AjaxManager.class)
    public void getNameById(){
        Identity identity = Identity.dao.findById(getPara("id"));
        renderText(identity.get("name").toString());
    }
}
