//package com.wts.controller;
//
//import com.foxinmy.weixin4j.exception.WeixinException;
//import com.foxinmy.weixin4j.qy.model.User;
//import com.jfinal.aop.Before;
//import com.jfinal.core.Controller;
//import com.wts.entity.WP;
//import com.wts.entity.model.Enterprise;
//import com.wts.entity.model.Parent;
//import com.wts.entity.model.Room;
//import com.wts.entity.model.Teacher;
//import com.wts.interceptor.*;
//import com.wts.util.ParamesAPI;
//import com.wts.util.PinyinTool;
//import com.wts.util.msg.Util.MessageUtil;
//import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
//import org.apache.log4j.Logger;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class EnterpriseController extends Controller {
//  private static Logger logger = Logger.getLogger(EnterpriseController.class);
////  /**
////   * 检测姓名
////   * */
////  @Before(AjaxManager.class)
////  public void checkName() {
////    if (!Util.getString(getPara("name")).matches("^[\\u4e00-\\u9fa5]{2,}$")) {
////      renderText("请输入两个以上的汉字!");
////    } else {
////      renderText("OK");
////    }
////  }
//  /**
//   * 新增时检测手机号码
//   * */
//  @Before(AjaxManager.class)
//  public void checkMobileForNew() {
////    if (!Util.getString(getPara("mobile")).matches("^1(3|4|5|7|8)\\d{9}$")) {
////      renderText("手机号码格式错误!");
//    if (Enterprise.dao.find("select * from enterprise where mobile=?", getPara("mobile")).size()!=0) {
//      renderText("该手机号码已存在!");
//    } else {
//      renderText("OK");
//    }
//  }
//  /**
//   * 新增时检测账号
//   * */
//  @Before(AjaxManager.class)
//  public void checkUserIdForNew() {
////    if (!Util.getString(getPara("userId")).matches("^[A-Za-z0-9]+$")) {
////      renderText("账号名应为字母或数字的组合!");
//    if (Enterprise.dao.find("select * from enterprise where userId=?", getPara("userId")).size()!=0) {
//      renderText("该账号已存在");
//    } else {
//      renderText("OK");
//    }
//  }
//
//  /**
//   * 修改时检测手机号码
//   * */
//  @Before(AjaxManager.class)
//  public void checkMobileForEdit() {
////    if (!Util.getString(getPara("mobile")).matches("^1(3|4|5|7|8)\\d{9}$")) {
////      renderText("手机号码格式错误!");
//    if (!Enterprise.dao.findById(getPara("id")).get("mobile").equals(getPara("mobile"))
//            && Enterprise.dao.find("select * from enterprise where mobile=?", getPara("mobile")).size()!=0) {
//      renderText("该手机号码已存在!");
//    } else {
//      renderText("OK");
//    }
//  }
//  /**
//   * 修改时检测手机号码
//   * */
//  @Before(AjaxTeacher.class)
//  public void checkMobileForEditForTeacher() {
////    if (!Util.getString(getPara("mobile")).matches("^1(3|4|5|7|8)\\d{9}$")) {
////      renderText("手机号码格式错误!");
//    if (!Enterprise.dao.findById(((Enterprise) getSessionAttr("Teacher")).getId()).get("mobile").equals(getPara("mobile"))
//            && Enterprise.dao.find("select * from enterprise where mobile=?", getPara("mobile")).size()!=0) {
//      renderText("该手机号码已存在!");
//    } else {
//      renderText("OK");
//    }
//  }
//  /**
//   * 修改时检测手机号码
//   * */
//  @Before(AjaxParent.class)
//  public void checkMobileForEditForParent() {
////    if (!Util.getString(getPara("mobile")).matches("^1(3|4|5|7|8)\\d{9}$")) {
////      renderText("手机号码格式错误!");
//    if (!Enterprise.dao.findById(((Enterprise) getSessionAttr("Parent")).getId()).get("mobile").equals(getPara("mobile"))
//            && Enterprise.dao.find("select * from enterprise where mobile=?", getPara("mobile")).size()!=0) {
//      renderText("该手机号码已存在!");
//    } else {
//      renderText("OK");
//    }
//  }
//  @Before(AjaxManager.class)
//  public void getById() {
//    Enterprise enterprise = Enterprise.dao.findById(getPara("id"));
//    renderJson(enterprise);
//  }
//  @Before(AjaxTeacher.class)
//  public void getTeacher() {
//    Enterprise enterprise = Enterprise.dao.findById(((Enterprise) getSessionAttr("Teacher")).getId());
//    renderJson(enterprise);
//  }
//  @Before(AjaxParent.class)
//  public void getParent() {
//    Enterprise enterprise = Enterprise.dao.findById(((Enterprise) getSessionAttr("Parent")).getId());
//    renderJson(enterprise);
//  }
//  @Before({Login.class, Ajax.class})
//  public void getNameById() {
//    Enterprise enterprise = Enterprise.dao.findById(getPara("id"));
//    renderText(enterprise.get("name").toString());
//  }
//  @Before(AjaxManager.class)
//  public void inactiveById() {
//    Enterprise enterprise = Enterprise.dao.findById(getPara("id"));
//    if (enterprise == null) {
//      renderText("要取消关注的人员不存在!");
//    } else if (enterprise.get("state").toString().equals("3")) {
//      renderText("该人员已处于取消关注状态!");
//    }  else if (enterprise.get("state").toString().equals("2")) {
//      renderText("该人员已处于冻结状态!");
//    } else {
//      try {
//        WP.me.deleteUser(enterprise.getUserId());
//        enterprise.set("state",3).update();
//        renderText("OK");
//      } catch (WeixinException e) {
//        renderText(e.getErrorText());
//      }
//    }
//  }
//  @Before(AjaxManager.class)
//  public void activeById() {
//    Enterprise enterprise = Enterprise.dao.findById(getPara("id"));
//    if (enterprise == null) {
//      renderText("要重新邀请关注的人员不存在!");
//    } else if (enterprise.get("state").toString().equals("1")) {
//      renderText("该人员已处于关注状态!");
//    } else {
//      User user = new User(enterprise.get("userId").toString(),enterprise.get("name").toString());
//      user.setMobile(enterprise.get("mobile").toString());
//      user.setPartyIds(1);
//      try {
//        WP.me.createUser(user);
//        List<String> userIds = new ArrayList<String>();
//        userIds.add(enterprise.get("userId").toString());
//        if (enterprise.get("isTeacher").toString().trim().equals("1")) {
//          WP.me.addTagUsers(ParamesAPI.teacherTagId, userIds, new ArrayList<Integer>());
//        }
//        if (enterprise.get("isManager").toString().trim().equals("1")) {
//          WP.me.addTagUsers(ParamesAPI.managerTagId,userIds,new ArrayList<Integer>());
//        }
//        if (enterprise.get("isParent").toString().trim().equals("1")) {
//          WP.me.addTagUsers(ParamesAPI.parentTagId,userIds,new ArrayList<Integer>());
//        }
//        enterprise.set("state",1).update();
//        renderText("OK");
//      } catch (WeixinException e) {
//        renderText(e.getErrorText());
//      }
//    }
//  }
//  @Before({Login.class, Ajax.class})
//  public void update() {
//    try {
//      Enterprise enterprise = Enterprise.dao.findFirst("select * from enterprise where userId=?", getPara("userId"));
//      if (enterprise.getState().equals("1")){
//        User user = WP.me.getUser(getPara("userId"));
//        enterprise.set("sex", user.getGender())
//                .set("picUrl", user.getAvatar())
//                .update();
//        renderText("");
//      }
//    } catch (Exception e) {
//      logger.error(e.getMessage());
//      renderText("");
//    }
//  }
//

//}
