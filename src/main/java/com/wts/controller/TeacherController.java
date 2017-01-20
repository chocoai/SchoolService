package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.http.weixin.ApiResult;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.WP;
import com.wts.entity.model.Enterprise;
import com.wts.interceptor.AjaxFunction;
import com.wts.interceptor.LoginTeacher;
import com.wts.util.PinyinTool;
import com.wts.util.Util;

public class TeacherController extends Controller {

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
          render("/static/TeacherManage.html");
        } else {
          redirect("/");
        }
      } else {
        Enterprise teacher = Enterprise.dao.findById(getCookie("die"));
        setSessionAttr("teacher", teacher);
        render("/static/TeacherManage.html");
      }
    } else {
      render("/static/TeacherManage.html");
    }
  }

  /**
   * 新增时检测教师姓名
  * */
  @Before(AjaxFunction.class)
  public void checkNameForNew() {
    if (!Util.getString(getPara("name")).matches("^[\\u4e00-\\u9fa5]{2,}$")) {
      renderText("请输入两个以上的汉字!");
    } else {
      renderText("OK");
    }
  }
  /**
   * 新增时检测教师手机号码
   * */
  @Before(AjaxFunction.class)
  public void checkMobileForNew() {
    if (!Util.getString(getPara("mobile")).matches("^1(3|4|5|7|8)\\d{9}$")) {
      renderText("手机号码格式错误!");
    } else if (Enterprise.dao.find("select * from enterprise where mobile=?", getPara("mobile")).size()!=0) {
      renderText("该手机号码已存在!");
    } else {
      renderText("OK");
    }
  }
  /**
   * 新增时检测教师账号
   * */
  @Before(AjaxFunction.class)
  public void checkUserIdForNew() {
    if (!Util.getString(getPara("userId")).matches("^[A-Za-z0-9]+$")) {
      renderText("账号名应为字母或数字的组合!");
    } else if (Enterprise.dao.find("select * from enterprise where userId=?", getPara("userId")).size()!=0) {
      renderText("该账号已存在");
    } else {
      renderText("OK");
    }
  }

  /**
   * 修改时检测教师姓名
   * */
  @Before(AjaxFunction.class)
  public void checkNameForEdit() {
    if (!Util.getString(getPara("name")).matches("^[\\u4e00-\\u9fa5]{2,}$")) {
      renderText("请输入两个以上汉字!");
    } else {
      renderText("OK");
    }
  }
  /**
   * 修改时检测教师手机号码
   * */
  @Before(AjaxFunction.class)
  public void checkMobileForEdit() {
    if (!Util.getString(getPara("mobile")).matches("^1(3|4|5|7|8)\\d{9}$")) {
      renderText("手机号码格式错误!");
    } else if (!Enterprise.dao.findById(getPara("id")).get("mobile").equals(getPara("mobile"))
            && Enterprise.dao.find("select * from enterprise where mobile=?", getPara("mobile")).size()!=0) {
      renderText("该手机号码已存在!");
    } else {
      renderText("OK");
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
        Enterprise teacher = new Enterprise();
        teacher.set("name", getPara("name").trim())
                .set("mobile", getPara("mobile").trim())
                .set("userId", getPara("userId").trim())
                .set("pass", "wts")
                .set("state", 2)
                .set("isManager", getPara("isManager").trim())
                .set("isTeacher", 1)
                .set("isParent", 0)
                .save();
        renderText("OK");
      }catch(WeixinException e){
        renderText(e.getErrorText());
      }
    }
  }

  @Before({Tx.class,AjaxFunction.class})
  public void edit() {
    Enterprise teacher = Enterprise.dao.findById(getPara("id"));
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
              &&  Enterprise.dao.find("select * from enterprise where mobile=?", getPara("mobile")).size()!=0) {
        renderText("该手机号码已存在!");
      } else {
        if (Util.getString(teacher.getStr("name")).equals(getPara("name").trim())
                && Util.getString(teacher.getStr("mobile")).equals(getPara("mobile").trim())
                && !Util.getString(teacher.get("isManager").toString()).equals(getPara("isManager").trim())
                ){
          teacher.set("name",getPara("name").trim())
                  .set("mobile",getPara("mobile").trim())
                  .set("isManager",getPara("isManager").trim())
                  .update();
          renderText("OK");
        } else {
          try{
            User user = new User(teacher.get("userId").toString(),teacher.get("name").toString());
            user.setMobile(getPara("mobile").trim());
            WP.me.updateUser(user);
            teacher.set("name",getPara("name").trim())
                    .set("mobile",getPara("mobile").trim())
                    .update();
            renderText("OK");
          }catch(WeixinException e){
            renderText(e.getErrorText());
          }
        }
      }
    }
  }
  public void query() {
    Page<Enterprise> teachers= Enterprise.dao.teacherQuery(getParaToInt("pageCurrent"),getParaToInt("pageSize"),getPara("queryString"));
    renderJson(teachers.getList());
  }
  @Before(AjaxFunction.class)
  public void totalByName() {
    Long count = Db.queryLong("select count(*) from enterprise where isTeacher=1 and name like '%"+ getPara("queryString") +"%'");
    if (count%getParaToInt("pageSize")==0) {
      renderText((count/getParaToInt("pageSize"))+"");
    } else {
      renderText((count/getParaToInt("pageSize")+1)+"");
    }
  }
  @Before(AjaxFunction.class)
  public void getById() {
    Enterprise teacher = Enterprise.dao.findById(getPara("id"));
    renderJson(teacher);
  }
  @Before(AjaxFunction.class)
  public void deleteById() {
    if (Enterprise.dao.findById(getPara("id")) == null) {
      renderText("要取消关注的教师不存在!");
    } else {
      Enterprise teacher = Enterprise.dao.findById(getPara("id"));
      try {
        WP.me.deleteUser(teacher.getUserId());
        teacher.set("state",4).update();
        renderText("OK");
      } catch (WeixinException e) {
        renderText(e.getErrorText());
      }
    }
  }
  @Before(AjaxFunction.class)
  public void resaveById() {
    if (Enterprise.dao.findById(getPara("id")) == null) {
      renderText("要重新邀请关注的教师不存在!");
    } else {
      Enterprise teacher = Enterprise.dao.findById(getPara("id"));
      User user = new User(teacher.get("userId").toString(),teacher.get("name").toString());
      user.setMobile(teacher.get("mobile").toString());
      user.setPartyIds(1);
      try {
        WP.me.createUser(user);
        teacher.set("state",2).update();
        renderText("OK");
      } catch (WeixinException e) {
        renderText(e.getErrorText());
      }
    }
  }
  @Before(AjaxFunction.class)
  public void getUserId() {
    try {
      renderText(new PinyinTool().toPinYin(getPara("name"), "", PinyinTool.Type.FIRSTUPPER));
    } catch (Exception e) {
      renderText("");
    }
  }
}
