package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.model.Teacher;

import java.util.List;

public class TeacherController extends Controller {

  public void index() {
    render("/dist/TeacherManage.html");
  }

  public void getName() {
    renderText("王天硕");
  }

  public void checkName() {
    if (!getPara("name").matches("^[\\u4e00-\\u9fa5]{2,}$")) {
      // 输入内容不是汉字
      renderText("1");
    } else if (Teacher.dao.find("select * from teacher where name=?", getPara("name")).size()!=0) {
      // 有重名现象
      renderText("2");
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
    if (getPara("weixin").matches("^[\\u4e00-\\u9fa5]{0,}$")) {
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
    if (!getPara("email").matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
      // 输入内容格式不正确
      renderText("1");
    } else if (Teacher.dao.find("select * from teacher where email=?", getPara("email")).size()!=0) {
      // 该电子邮箱已存在已存在
      renderText("2");
    } else {
      renderText("0");
    }
  }
  @Before(Tx.class)
  public void save() {
    if (getPara("name").trim().equals("")) {
      // 姓名为空
      renderText("1");
    } else if (!getPara("name").matches("^[\\u4e00-\\u9fa5]{2,}$")) {
      // 输入内容不应为两个以上汉字
      renderText("2");
    } else if (getPara("phone").trim().equals("") && getPara("weixin").trim().equals("")) {
      // 手机号码或微信号为空
      renderText("3");
    } else if (!getPara("phone").trim().equals("") && !getPara("phone").matches("\\d{11}")) {
      // 输入内容不是11位数字
      renderText("4");
    } else if (!getPara("phone").trim().equals("") && Teacher.dao.find("select * from teacher where phone=?", getPara("phone")).size()!=0) {
      // 该手机号码已存在已存在
      renderText("5");
    } else if (!getPara("weixin").trim().equals("") && getPara("weixin").matches("^.*[\u4e00-\u9fa5].*$")) {
      // 输入内容不能是中文
      renderText("6");
    } else if (!getPara("weixin").trim().equals("") && Teacher.dao.find("select * from teacher where weixinId=?", getPara("weixin")).size()!=0) {
      // 该微信号码已存在
      renderText("7");
    } else if (!getPara("email").trim().equals("") && !getPara("email").matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
      // 输入内容格式不正确
      renderText("8");
    } else if (!getPara("email").trim().equals("") && Teacher.dao.find("select * from teacher where email=?", getPara("email")).size()!=0) {
      // 该电子邮箱已存在已存在
      renderText("9");
    } else {
      Teacher teacher = new Teacher();
      teacher.set("name", getPara("name").trim())
              .set("phone", getPara("phone").trim())
              .set("weixinId", getPara("weixin").trim())
              .set("email", getPara("email").trim())
              .set("remark", getPara("remark").trim())
              .save();
      renderText("0");
    }
  }

  public void query() {
    List<Record> teachers= Db.find("SELECT * FROM teacher WHERE name LIKE '%"+getPara("name")+"%' ORDER BY id DESC");
    renderJson(teachers);
  }
  public void get() {
    Teacher teacher =Teacher.dao.findById(getPara("id"));
    renderJson(teacher);
  }
}
