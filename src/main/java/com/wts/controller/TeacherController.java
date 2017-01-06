package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.WP;
import com.wts.entity.model.Teacher;
import com.wts.util.Util;

import java.util.List;

public class TeacherController extends Controller {

  public void index() throws WeixinException{
//    User u= WP.me.getUserByCode(getPara("code"));
//    setSessionAttr("user",u);
//    render("/dist/TeacherManage.html");
  }

  public void getName() throws WeixinException {
//    User u =(User) getSessionAttr("user");
    renderText("济南市育明小学");
  }

  /**
   * 新增时检测教师姓名
  * */
  public void checkNameForNew() {
    if (getPara("name")!=null) {
      if (!getPara("name").matches("^[\\u4e00-\\u9fa5]{2,}$")) {
        // 输入内容不是汉字
        renderText("1");
      } else if (Teacher.dao.find("select * from teacher where name=?", getPara("name")).size()!=0) {
        // 有重名现象
        renderText("2");
      } else {
        renderText("0");
      }
    } else {
      renderText("Z");
    }
  }
  /**
   * 新增时检测教师手机号码
   * */
  public void checkPhoneForNew() {
    if (getPara("phone")!=null) {
      if (!getPara("phone").matches("\\d{11}")) {
        // 输入内容不是11位数字
        renderText("1");
      } else if (Teacher.dao.find("select * from teacher where phone=?", getPara("phone")).size()!=0) {
        // 该手机号码已存在
        renderText("2");
      } else {
        renderText("0");
      }
    } else {
      renderText("Z");
    }
  }
  /**
   * 新增时检测教师微信号
   * */
  public void checkWeixinForNew() {
    if (getPara("weixin")!=null) {
      if (getPara("weixin").matches("^[\\u4e00-\\u9fa5]{0,}$")) {
        // 输入内容不能是中文
        renderText("1");
      } else if (Teacher.dao.find("select * from teacher where weixinId=?", getPara("weixin")).size()!=0) {
        // 该微信号码已存在
        renderText("2");
      } else {
        renderText("0");
      }
    } else {
      renderText("Z");
    }
  }
  /**
   * 新增时检测教师电子邮箱
   * */
  public void checkEmailForNew() {
    if (getPara("weixin")!=null) {
      if (!getPara("email").matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
        // 输入内容格式不正确
        renderText("1");
      } else if (Teacher.dao.find("select * from teacher where email=?", getPara("email")).size()!=0) {
        // 该电子邮箱已存在
        renderText("2");
      } else {
        renderText("0");
      }
    } else {
      renderText("Z");
    }
  }

  /**
   * 修改时检测教师姓名
   * */
  public void checkNameForEdit() {
    if (getPara("name")!=null) {
      if (!getPara("name").matches("^[\\u4e00-\\u9fa5]{2,}$")) {
        // 输入内容不是汉字
        renderText("1");
      } else if (Teacher.dao.find("select * from teacher where name=?", getPara("name")).size()>1) {
        // 有重名现象
        renderText("2");
      } else {
        renderText("0");
      }
    } else {
      renderText("Z");
    }
  }
  /**
   * 修改时检测教师手机号码
   * */
  public void checkPhoneForEdit() {
    if (getPara("phone")!=null) {
      if (!getPara("phone").matches("\\d{11}")) {
        // 输入内容不是11位数字
        renderText("1");
      } else if (Teacher.dao.find("select * from teacher where phone=?", getPara("phone")).size()>=1) {
        // 该手机号码已存在
        renderText("2");
      } else {
        renderText("0");
      }
    } else {
      renderText("Z");
    }
  }
  /**
   * 修改时检测教师微信号
   * */
  public void checkWeixinForEdit() {
    if (getPara("weixin")!=null) {
      if (getPara("weixin").matches("^[\\u4e00-\\u9fa5]{0,}$")) {
        // 输入内容不能是中文
        renderText("1");
      } else if (Teacher.dao.find("select * from teacher where weixinId=?", getPara("weixin")).size()>=1) {
        // 该微信号码已存在
        renderText("2");
      } else {
        renderText("0");
      }
    } else {
      renderText("Z");
    }
  }
  /**
   * 修改时检测教师电子邮箱
   * */
  public void checkEmailForEdit() {
    if (getPara("email")!=null) {
      if (!getPara("email").matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
        // 输入内容格式不正确
        renderText("1");
      } else if (Teacher.dao.find("select * from teacher where email=?", getPara("email")).size()>=1) {
        // 该电子邮箱已存在
        renderText("2");
      } else {
        renderText("0");
      }
    } else {
      renderText("Z");
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

  @Before(Tx.class)
  public void edit() {
    Teacher teacher = Teacher.dao.findById(getPara("id"));
    if (teacher == null) {
      // 要修改的教师不存在
      renderText("1");
    } else {
      if (Util.CheckNull(teacher.getStr("name")).equals(getPara("name").trim())
              && Util.CheckNull(teacher.getStr("phone")).equals(getPara("phone").trim())
              && Util.CheckNull(teacher.getStr("weixinId")).equals(getPara("weixin").trim())
              && Util.CheckNull(teacher.getStr("remark")).equals(getPara("remark").trim())
              ) {
        // 未找到修改内容
        renderText("2");
      } else if (!getPara("name").trim().equals("") && !getPara("name").matches("^[\\u4e00-\\u9fa5]{2,}$")) {
        // 姓名不是两个以上汉字
        renderText("3");
      } else if (!getPara("phone").trim().equals("") && !getPara("phone").matches("\\d{11}")) {
        // 联系电话不是11位数字
        renderText("4");
      } else if (!getPara("weixin").trim().equals("") && getPara("weixin").matches("^[\\u4e00-\\u9fa5]{0,}$")) {
        // 微信号码含有中文
        renderText("5");
      } else if (!getPara("email").trim().equals("") && !getPara("email").matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
        // 电子邮箱格式不正确
        renderText("6");
      } else if (getPara("phone").trim().equals("") && getPara("weixin").trim().equals("")) {
        // 手机号码和微信号同时为空
        renderText("7");
      } else if (!Util.CheckNull(teacher.getStr("phone")).equals(getPara("phone"))
              &&  Teacher.dao.find("select * from teacher where phone=?", getPara("phone")).size()!=0) {
        // 该手机号码已存在
        renderText("8");
      } else if (!Util.CheckNull(teacher.getStr("weixinId")).equals(getPara("weixin"))
              &&  Teacher.dao.find("select * from teacher where weixinId=?", getPara("weixin")).size()!=0) {
        // 该微信号已存在
        renderText("9");
      } else if (!Util.CheckNull(teacher.getStr("email")).equals(getPara("email"))
              && Teacher.dao.find("select * from teacher where email=?", getPara("email")).size()!=0
              && !getPara("email").trim().equals("")) {
        // 该电子邮箱已存在
        renderText("A");
      } else {
        teacher.set("name",getPara("name").trim())
                .set("phone",getPara("phone").trim())
                .set("weixinId",getPara("weixin").trim())
                .set("email",getPara("email").trim())
                .set("remark",Util.CheckNull(getPara("remark").trim()))
                .update();
        renderText("0");
      }
    }
  }
  public void queryByName() {
    List<Record> teachers= Db.find("SELECT * FROM teacher WHERE name LIKE '%"+getPara("name")+"%' ORDER BY id DESC");
    renderJson(teachers);
  }
  public void getById() {
    Teacher teacher =Teacher.dao.findById(getPara("id"));
    renderJson(teacher);
  }

  public void deleteById() {
    if (getPara("id") != null) {
      if (Teacher.dao.findById(getPara("id")) == null) {
        // 未找到指定id的教师
        renderText("1");
      } else {
        Teacher.dao.deleteById(getPara("id"));
      }
      renderText("0");
    } else {
      renderText("Z");
    }
  }
}
