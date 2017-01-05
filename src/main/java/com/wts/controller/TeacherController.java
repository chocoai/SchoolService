package com.wts.controller;

import com.jfinal.core.Controller;
import com.wts.entity.model.Teacher;

public class TeacherController extends Controller {

  public void index() {
    render("/dist/TeacherManage.html");
  }

  public void getName() {
    renderText("王天硕");
  }

  public void checkName() {
    if (!getPara("name").matches("[\u4e00-\u9fa5]+")) {
      // 输入内容不是汉字
      renderText("1");
    } else if (getPara("name").length()<2) {
      // 输入内容字数不足两个
      renderText("2");
    } else if (Teacher.dao.find("select * from teacher where name=?", getPara("name")).size()!=0) {
      // 有重名现象
      renderText("3");
    } else {
      renderText("0");
    }
  }
  public void checkPhone() {
    if (!getPara("phone").matches("\\d{11}")) {
      // 输入内容不是11位数字
      renderText("1");
    } else if (Teacher.dao.find("select * from teacher where phone=?", getPara("phone")).size()!=0) {
      // 该手机号码已存在已存在
      renderText("2");
    } else {
      renderText("0");
    }
  }
  public void checkWeixin() {
    if (getPara("weixin").matches("[\u4e00-\u9fa5]+")) {
      // 输入内容不能是中文
      renderText("1");
    } else if (Teacher.dao.find("select * from teacher where weixinId=?", getPara("weixin")).size()!=0) {
      // 该微信号码已存在已存在
      renderText("2");
    } else {
      renderText("0");
    }
  }
  public void checkEmail() {
    if (!getPara("email").matches("^([a-z0-9A-Z]+[-|\\\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\\\.)+[a-zA-Z]{2,}$")) {
      // 输入内容格式不正确
      renderText("1");
    } else if (Teacher.dao.find("select * from teacher where email=?", getPara("email")).size()!=0) {
      // 该电子邮箱已存在已存在
      renderText("2");
    } else {
      renderText("0");
    }
  }
}
