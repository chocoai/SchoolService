package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.wts.entity.WP;
import com.wts.entity.model.Enterprise;
import com.wts.interceptor.AjaxFunction;
import com.wts.util.ParamesAPI;
import com.wts.util.PinyinTool;
import com.wts.util.Util;
import com.wts.util.msg.Util.MessageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnterpriseController extends Controller {
//  /**
//   * 检测姓名
//   * */
//  @Before(AjaxFunction.class)
//  public void checkName() {
//    if (!Util.getString(getPara("name")).matches("^[\\u4e00-\\u9fa5]{2,}$")) {
//      renderText("请输入两个以上的汉字!");
//    } else {
//      renderText("OK");
//    }
//  }
  /**
   * 新增时检测手机号码
   * */
  @Before(AjaxFunction.class)
  public void checkMobileForNew() {
//    if (!Util.getString(getPara("mobile")).matches("^1(3|4|5|7|8)\\d{9}$")) {
//      renderText("手机号码格式错误!");
    if (Enterprise.dao.find("select * from enterprise where mobile=?", getPara("mobile")).size()!=0) {
      renderText("该手机号码已存在!");
    } else {
      renderText("OK");
    }
  }
  /**
   * 新增时检测账号
   * */
  @Before(AjaxFunction.class)
  public void checkUserIdForNew() {
//    if (!Util.getString(getPara("userId")).matches("^[A-Za-z0-9]+$")) {
//      renderText("账号名应为字母或数字的组合!");
    if (Enterprise.dao.find("select * from enterprise where userId=?", getPara("userId")).size()!=0) {
      renderText("该账号已存在");
    } else {
      renderText("OK");
    }
  }

  /**
   * 修改时检测手机号码
   * */
  @Before(AjaxFunction.class)
  public void checkMobileForEdit() {
//    if (!Util.getString(getPara("mobile")).matches("^1(3|4|5|7|8)\\d{9}$")) {
//      renderText("手机号码格式错误!");
    if (!Enterprise.dao.findById(getPara("id")).get("mobile").equals(getPara("mobile"))
            && Enterprise.dao.find("select * from enterprise where mobile=?", getPara("mobile")).size()!=0) {
      renderText("该手机号码已存在!");
    } else {
      renderText("OK");
    }
  }
  @Before(AjaxFunction.class)
  public void getById() {
    Enterprise enterprise = Enterprise.dao.findById(getPara("id"));
    renderJson(enterprise);
  }
  @Before(AjaxFunction.class)
  public void inactiveById() {
    Enterprise enterprise = Enterprise.dao.findById(getPara("id"));
    if (enterprise == null) {
      renderText("要取消关注的人员不存在!");
    } else if (enterprise.get("state").toString().equals("4")) {
      renderText("该人员已处于取消关注状态!");
    }  else if (enterprise.get("state").toString().equals("3")) {
      renderText("该人员已处于冻结状态!");
    } else {
      try {
        WP.me.deleteUser(enterprise.getUserId());
        enterprise.set("state",4).update();
        renderText("OK");
      } catch (WeixinException e) {
        renderText(e.getErrorText());
      }
    }
  }
  @Before(AjaxFunction.class)
  public void activeById() {
    Enterprise enterprise = Enterprise.dao.findById(getPara("id"));
    if (enterprise == null) {
      renderText("要重新邀请关注的人员不存在!");
    } else if (enterprise.get("state").toString().equals("2")) {
      renderText("该人员已处于关注状态!");
    } else {
      User user = new User(enterprise.get("userId").toString(),enterprise.get("name").toString());
      user.setMobile(enterprise.get("mobile").toString());
      user.setPartyIds(1);
      try {
        WP.me.createUser(user);
        List<String> userIds = new ArrayList<String>();
        userIds.add(enterprise.get("userId").toString());
        if (enterprise.get("isTeacher").toString().trim().equals("1")) {
          WP.me.addTagUsers(ParamesAPI.teacherTagId, userIds, new ArrayList<Integer>());
        }
        if (enterprise.get("isManager").toString().trim().equals("1")) {
          WP.me.addTagUsers(ParamesAPI.managerTagId,userIds,new ArrayList<Integer>());
        }
        if (enterprise.get("isParent").toString().trim().equals("1")) {
          WP.me.addTagUsers(ParamesAPI.parentTagId,userIds,new ArrayList<Integer>());
        }
        enterprise.set("state",2).update();
        renderText("OK");
      } catch (WeixinException e) {
        renderText(e.getErrorText());
      }
    }
  }
  @Before(AjaxFunction.class)
  public void getUserId() {
    try {
      String UserId = new PinyinTool().toPinYin(getPara("name"), "", PinyinTool.Type.FIRSTUPPER);
      String UserIds;
      if (Enterprise.dao.findFirst("select userId from enterprise where userId=?", UserId) == null) {
        renderText(UserId);
      } else {
        int i = 1;
        do {
          UserIds = UserId + i;
          i++;
        } while (Enterprise.dao.findFirst("select userId from enterprise where userId=?", UserIds) != null);
        renderText(UserIds);
      }
    } catch (Exception e) {
      renderText("");
    }
  }
  /**
   * 处理微信发来的关注
   *
   * @param request
   */
  public static void subscribe(String request) {
    try {
      // 调用parseXml方法解析请求消息
      Map<String, String> requestMap = MessageUtil.parseXml(request);
      // 发送方帐号
      String FromUserName = requestMap.get("FromUserName");
      if (requestMap.get("Event").equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
        User user = WP.me.getUser(FromUserName);
        Enterprise enterprise = Enterprise.dao.findFirst("select * from enterprise where userId=?", FromUserName);
        enterprise.set("sex", user.getGender())
                .set("picUrl", user.getAvatar())
                .set("state", 1)
                .update();
      }
      if (requestMap.get("Event").equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
        Enterprise enterprise = Enterprise.dao.findFirst("select * from enterprise where userId=?", FromUserName);
        enterprise.set("picUrl", "")
                .set("state", 4)
                .update();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
