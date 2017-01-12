package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

public class TagController extends Controller {

  public void index() throws WeixinException {
    render("/static/TagManage.html");
  }
  public void getName() throws WeixinException {
    renderText("济南市育明小学");
  }
  public void queryByName() {
    Page<Tag> tags=Tag.dao.queryByName(getParaToInt("pageCurrent"),getParaToInt("pageSize"),getPara("queryString"));
    renderJson(tags.getList());
  }
  public void totalByName() {
    Long count = Db.queryLong("select count(*) from tag where name like '%"+ getPara("queryString") +"%'");
    if (count%getParaToInt("pageSize")==0) {
      renderText((count/getParaToInt("pageSize"))+"");
    } else {
      renderText((count/getParaToInt("pageSize")+1)+"");
    }
  }

}
