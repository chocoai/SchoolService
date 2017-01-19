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
  @Before({AjaxFunction.class,LoginTeacher.class})
  public void getName() throws WeixinException {
//    User u =(User) getSessionAttr("user");
    System.out.println(getRequest().getHeader("X-Requested-With"));
    renderText("济南市育明小学");
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
  public void checkMobileForEdit() {
    if (!Util.getString(getPara("mobile")).matches("^1(3|4|5|7|8)\\d{9}$")) {
      renderText("手机号码格式错误!");
    } else if (Enterprise.dao.find("select * from enterprise where mobile=?", getPara("mobile")).size()>=1) {
      renderText("该手机号码已存在!");
    } else {
      renderText("OK");
    }
  }


  @Before(Tx.class)
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

  @Before(Tx.class)
  public void edit() {
    Enterprise teacher = Enterprise.dao.findById(getPara("id"));
    if (teacher == null) {
      renderText("要修改的教师不存在!");
    } else {
      if (Util.getString(teacher.getStr("name")).equals(getPara("name").trim())
              && Util.getString(teacher.getStr("mobile")).equals(getPara("mobile").trim())
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
        try{
          User user = WP.me.getUser(teacher.getUserId());
          user.setName(getPara("name").trim());
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
  public void queryByName() {
    Page<Enterprise> teachers= Enterprise.dao.teacherQueryByName(getParaToInt("pageCurrent"),getParaToInt("pageSize"),getPara("queryString"));
    renderJson(teachers.getList());
  }
  public void totalByName() {
    Long count = Db.queryLong("select count(*) from enterprise where isTeacher=1 and name like '%"+ getPara("queryString") +"%'");
    if (count%getParaToInt("pageSize")==0) {
      renderText((count/getParaToInt("pageSize"))+"");
    } else {
      renderText((count/getParaToInt("pageSize")+1)+"");
    }
  }

  public void getById() {
    if (getPara("id") != null) {
      Enterprise teacher = Enterprise.dao.findById(getPara("id"));
      renderJson(teacher);
    } else {
      renderText("未找到参数!");
    }
  }

  public void deleteById() {
    if (getPara("id") != null) {
      if (Enterprise.dao.findById(getPara("id")) == null) {
        renderText("要删除的教师不存在!");
      } else {
        Enterprise.dao.deleteById(getPara("id"));
      }
      renderText("OK");
    } else {
      renderText("未找到参数!");
    }
  }

  public void getUserId() {
    try {
      renderText(new PinyinTool().toPinYin(getPara("name"), "", PinyinTool.Type.FIRSTUPPER));
    } catch (Exception e) {
      renderText("");
    }
  }
}
