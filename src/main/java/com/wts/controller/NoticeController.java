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

import java.util.Date;
import java.util.List;

public class NoticeController extends Controller {

  public void forManager() throws WeixinException {
    // 检测session中是否存在teacher
    if (getSessionAttr("manager") == null || ((Enterprise)getSessionAttr("manager")).getIsManager()!=1) {
      // 检测cookie中是否存在EnterpriseId
      if (getCookie("die") == null || getCookie("die").equals("")) {
        // 检测是否来自微信请求
        if (!(getPara("code") == null || getPara("code").equals(""))) {
          User user = WP.me.getUserByCode(getPara("code"));
          Enterprise enterprise = Enterprise.dao.findFirst("select * from enterprise where state=1 and isManager=1 and userId=?", user.getUserId());
          // 检测是否有权限
          if (enterprise != null) {
            setSessionAttr("manager", enterprise);
            setCookie("die", enterprise.getId().toString(), 60 * 30);
            render("/static/NoticeForManager.html");
          } else {
            redirect("/");
          }
        } else {
          redirect("/");
        }
      } else {
        Enterprise enterprise = Enterprise.dao.findById(getCookie("die"));
        setSessionAttr("manager", enterprise);
        render("/static/NoticeForManager.html");
      }
    } else {
      render("/static/NoticeForManager.html");
    }
  }
  public void forTeacher() throws WeixinException {
    // 检测session中是否存在teacher
    if (getSessionAttr("teacher") == null || ((Enterprise)getSessionAttr("teacher")).getIsTeacher()!=1) {
      // 检测cookie中是否存在EnterpriseId
      if (getCookie("die") == null || getCookie("die").equals("")) {
        // 检测是否来自微信请求
        if (!(getPara("code") == null || getPara("code").equals(""))) {
          User user = WP.me.getUserByCode(getPara("code"));
          Enterprise enterprise = Enterprise.dao.findFirst("select * from enterprise where state=1 and isTeacher=1 and userId=?", user.getUserId());
          // 检测是否有权限
          if (enterprise != null) {
            setSessionAttr("teacher", enterprise);
            setCookie("die", enterprise.getId().toString(), 60 * 30);
            render("/static/NoticeForTeacher.html");
          } else {
            redirect("/");
          }
        } else {
          redirect("/");
        }
      } else {
        Enterprise enterprise = Enterprise.dao.findById(getCookie("die"));
        setSessionAttr("teacher", enterprise);
        render("/static/NoticeForTeacher.html");
      }
    } else {
      render("/static/NoticeForTeacher.html");
    }
  }
  public void forParent() throws WeixinException {
    // 检测session中是否存在teacher
    if (getSessionAttr("parent") == null || ((Enterprise)getSessionAttr("parent")).getIsParent()!=1) {
      // 检测cookie中是否存在EnterpriseId
      if (getCookie("die") == null || getCookie("die").equals("")) {
        // 检测是否来自微信请求
        if (!(getPara("code") == null || getPara("code").equals(""))) {
          User user = WP.me.getUserByCode(getPara("code"));
          Enterprise enterprise = Enterprise.dao.findFirst("select * from enterprise where state=1 and isParent=1 and userId=?", user.getUserId());
          // 检测是否有权限
          if (enterprise != null) {
            setSessionAttr("parent", enterprise);
            setCookie("die", enterprise.getId().toString(), 60 * 30);
            render("/static/NoticeForParent.html");
          } else {
            redirect("/");
          }
        } else {
          redirect("/");
        }
      } else {
        Enterprise enterprise = Enterprise.dao.findById(getCookie("die"));
        setSessionAttr("parent", enterprise);
        render("/static/NoticeForParent.html");
      }
    } else {
      render("/static/NoticeForParent.html");
    }
  }
  @Before(AjaxManager.class)
  public void queryAll() {
    Page<Record> notices= Db.paginate(getParaToInt("pageCurrent"), getParaToInt("pageSize"), "select notice.*,enterprise.name",
            "FROM notice left join enterprise on notice.teacher_id=enterprise.id WHERE notice.title LIKE '%"+getPara("queryString")+"%' or notice.content LIKE '%"+getPara("queryString")+"%' ORDER BY notice.id DESC");
    renderJson(notices.getList());
  }
  @Before(AjaxManager.class)
  public void totalAll() {
    Long count = Db.queryLong("select count(*) from notice where (title like '%"+ getPara("queryString") +"%' or content LIKE '%"+getPara("queryString")+"%')");
    if (count%getParaToInt("pageSize")==0) {
      renderText((count/getParaToInt("pageSize"))+"");
    } else {
      renderText((count/getParaToInt("pageSize")+1)+"");
    }
  }
  @Before(AjaxParent.class)
  public void queryParent() {
    Page<Record> notices= Db.paginate(getParaToInt("pageCurrent"), getParaToInt("pageSize"), "select notice.*,enterprise.name",
            "FROM notice left join enterprise on notice.teacher_id=enterprise.id WHERE (notice.type=3 or notice.type=1) and notice.title LIKE '%"+getPara("queryString")+"%' or notice.content LIKE '%"+getPara("queryString")+"%' ORDER BY notice.id DESC");
    renderJson(notices.getList());
  }
  @Before(AjaxParent.class)
  public void totalParent() {
    Long count = Db.queryLong("select count(*) from notice where (type=3 or type=1) and (title like '%"+ getPara("queryString") +"%' or content LIKE '%"+getPara("queryString")+"%')");
    if (count%getParaToInt("pageSize")==0) {
      renderText((count/getParaToInt("pageSize"))+"");
    } else {
      renderText((count/getParaToInt("pageSize")+1)+"");
    }
  }
  @Before(AjaxTeacher.class)
  public void queryTeacher() {
    Page<Record> notices= Db.paginate(getParaToInt("pageCurrent"), getParaToInt("pageSize"), "select notice.*,enterprise.name",
            "FROM notice left join enterprise on notice.teacher_id=enterprise.id WHERE (notice.type=2 or notice.type=1) and  notice.title LIKE '%"+getPara("queryString")+"%' or notice.content LIKE '%"+getPara("queryString")+"%' ORDER BY notice.id DESC");
    renderJson(notices.getList());
  }
  @Before(AjaxTeacher.class)
  public void totalTeacher() {
    Long count = Db.queryLong("select count(*) from notice where (type=2 or type=1) and title like '%"+ getPara("queryString") +"%' or content LIKE '%"+getPara("queryString")+"%'");
    if (count%getParaToInt("pageSize")==0) {
      renderText((count/getParaToInt("pageSize"))+"");
    } else {
      renderText((count/getParaToInt("pageSize")+1)+"");
    }
  }
  @Before({Ajax.class, Login.class})
  public void getById() {
    Notice notice = Notice.dao.findById(getPara("id"));
    renderJson(notice);
  }
  @Before({Tx.class,AjaxManager.class})
  public void save(){
    if (getPara("title").length()>20){
      renderText("输入标题超过20字符!");
    } else if(getPara("content").length()>300){
      renderText("输入内容超过300字符!");
    } else{
      Notice notice = new Notice();
      notice.set("title",getPara("title"))
              .set("content",getPara("content"))
              .set("time",new Date())
              .set("type",getPara("type"))
              .set("teacher_id",((Enterprise) getSessionAttr("manager")).getId())
              .save();
      IdParameter idParameter1 = new IdParameter();
      IdParameter idParameter2 = new IdParameter();
      if (getPara("type").equals("1")) {
        List<Enterprise> enterprises1 = Enterprise.dao.paginate(1, 999, "SELECT *",
                "FROM enterprise WHERE state=1").getList();
        List<Enterprise> enterprises2 = Enterprise.dao.paginate(2, 999, "SELECT *",
                "FROM enterprise WHERE state=1").getList();
        for (Enterprise enterprise : enterprises1){
          idParameter1.putUserIds(enterprise.get("userId").toString());
        }
        for (Enterprise enterprise : enterprises2){
          idParameter2.putUserIds(enterprise.get("userId").toString());
        }
      } else if (getPara("type").equals("2")){
        List<Enterprise> enterprises1 = Enterprise.dao.paginate(1, 999, "SELECT *",
                "FROM enterprise WHERE state=1 and isTeacher=1").getList();
        List<Enterprise> enterprises2 = Enterprise.dao.paginate(2, 999, "SELECT *",
                "FROM enterprise WHERE state=1 and isTeacher=1").getList();
        for (Enterprise enterprise : enterprises1){
          idParameter1.putUserIds(enterprise.get("userId").toString());
        }
        for (Enterprise enterprise : enterprises2){
          idParameter2.putUserIds(enterprise.get("userId").toString());
        }
      } else if (getPara("type").equals("3")){
        List<Enterprise> enterprises1 = Enterprise.dao.paginate(1, 999, "SELECT *",
                "FROM enterprise WHERE state=1 and isParent=1").getList();
        List<Enterprise> enterprises2 = Enterprise.dao.paginate(2, 999, "SELECT *",
                "FROM enterprise WHERE state=1 and isParent=1").getList();
        for (Enterprise enterprise : enterprises1){
          idParameter1.putUserIds(enterprise.get("userId").toString());
        }
        for (Enterprise enterprise : enterprises2){
          idParameter2.putUserIds(enterprise.get("userId").toString());
        }
      }else{

      }
      StringBuffer buffer = new StringBuffer();
      buffer.append(getPara("title")).append("\n");
      buffer.append(getPara("content"));
      try {
        if (idParameter1.getUserIds().size()!=0 ) {
          WP.me.sendNotifyMessage(new NotifyMessage(0, new Text(buffer.toString()), idParameter1, false));
        }
        if (idParameter2.getUserIds().size()!=0 ) {
          WP.me.sendNotifyMessage(new NotifyMessage(0, new Text(buffer.toString()), idParameter2, false));
        }
      } catch (Exception e) {
        renderText(e.getMessage());
      }
      renderText("OK");
    }
  }
}
