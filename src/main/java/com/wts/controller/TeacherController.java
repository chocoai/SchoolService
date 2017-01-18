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
import com.wts.entity.model.Teacher;
import com.wts.interceptor.AjaxFunction;
import com.wts.interceptor.LoginTeacher;
import com.wts.util.Util;

public class TeacherController extends Controller {

  public void index() throws WeixinException {
    if (getSessionAttr("teacher") == null) {
      if (!(getPara("code") == null || getPara("code").equals(""))) {
        User user = WP.me.getUserByCode(getPara("code"));
        System.out.println(user.getWeixinId());
        System.out.println(user.getUserId());
        System.out.println(user.getAvatar());
        System.out.println(user.getPosition());
        System.out.println(user.getPosition());
//        Teacher teacher = Teacher.dao.findById(Enterprise.dao.findFirst("select * from enterprise where state=1 and userId=?", user.getUserId()).getTeacherId());
//        setSessionAttr("teacher", teacher);
        render("/static/TeacherManage.html");
      } else {
        redirect("/");
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
   * 微信号和登录名保持一致
   * */
  public void checkWeixinForNew() {
    if (getPara("weixin")!=null) {
      if (getPara("weixin").matches("^[\\u4e00-\\u9fa5]{0,}$")) {
        // 输入内容不能是中文
        renderText("1");
      } else if (Teacher.dao.find("select * from teacher where login=?", getPara("weixin")).size()!=0) {
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
    if (getPara("email")!=null) {
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
      } else if (Teacher.dao.find("select * from teacher where login=?", getPara("weixin")).size()>=1) {
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
  public void save()  {
    if (getPara("name").trim().equals("")) {
      renderText("姓名不能为空！");
    } else if (!getPara("name").matches("^[\\u4e00-\\u9fa5]{2,}$")) {
      renderText("姓名应为两个以上汉字");
    } else if (getPara("phone").trim().equals("") && getPara("weixin").trim().equals("")) {
      renderText("微信号和手机号码不能同时为空");
    } else if (!getPara("phone").trim().equals("") && !getPara("phone").matches("\\d{11}")) {
      renderText("手机号码应为11位数字");
    } else if (!getPara("phone").trim().equals("") && Teacher.dao.find("select * from teacher where phone=?", getPara("phone")).size()!=0) {
      renderText("该手机号码已存在");
    } else if (!getPara("weixin").trim().equals("") && getPara("weixin").matches("^.*[\u4e00-\u9fa5].*$")) {
      renderText("微信号中不应包含中文");
    } else if (!getPara("weixin").trim().equals("") && Teacher.dao.find("select * from teacher where userId=?", getPara("weixin")).size()!=0) {
      renderText("该微信号已存在");
    } else if (getPara("login").trim().equals("")) {
      renderText("登录名不能为空！");
    } else if (getPara("login").matches("^.*[\u4e00-\u9fa5].*$")) {
      renderText("登录名中不应包含中文");
    } else if (Teacher.dao.find("select * from teacher where login=?", getPara("login")).size()!=0) {
      renderText("该登录名已存在");
    } else if (!getPara("email").trim().equals("") && !getPara("email").matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
      renderText("电子邮箱格式不正确");
    } else if (!getPara("email").trim().equals("") && Teacher.dao.find("select * from teacher where email=?", getPara("email")).size()!=0) {
      renderText("该电子邮箱已存在");
    } else {
      // 用login代替userId
      User user =new User(getPara("weixin").trim(),getPara("name").trim());
      user.setWeixinId(getPara("weixin").trim());
      user.setEmail(getPara("email").trim());
      user.setMobile(getPara("phone").trim());
      user.setPartyIds(1);
      try{
        WP.me.createUser(user);

        Teacher teacher = new Teacher();
        teacher.set("name", getPara("name").trim())
                .set("phone", getPara("phone").trim())
                .set("login", getPara("weixin").trim())
                .set("email", getPara("email").trim())
                .set("remark", getPara("remark").trim())
                .save();
        renderText("0");
      }catch(WeixinException e){
        renderText(e.getErrorText());
      }
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
              && Util.CheckNull(teacher.getStr("login")).equals(getPara("weixin").trim())
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
      } else if (!Util.CheckNull(teacher.getStr("login")).equals(getPara("weixin"))
              &&  Teacher.dao.find("select * from teacher where login=?", getPara("weixin")).size()!=0) {
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
                .set("login",getPara("weixin").trim())
                .set("email",getPara("email").trim())
                .set("remark",Util.CheckNull(getPara("remark").trim()))
                .update();
        renderText("0");
      }
    }
  }
  public void queryByName() {
    Page<Teacher> teachers= Teacher.dao.queryByName(getParaToInt("pageCurrent"),getParaToInt("pageSize"),getPara("queryString"));
    renderJson(teachers.getList());
  }
  public void totalByName() {
    Long count = Db.queryLong("select count(*) from teacher where name like '%"+ getPara("queryString") +"%'");
    if (count%getParaToInt("pageSize")==0) {
      renderText((count/getParaToInt("pageSize"))+"");
    } else {
      renderText((count/getParaToInt("pageSize")+1)+"");
    }
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
