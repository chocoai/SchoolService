package com.wts.controller.teacher;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.wts.entity.WP;
import com.wts.entity.WxService;
import com.wts.entity.model.Parent;
import com.wts.entity.model.Student;
import com.wts.entity.model.Teacher;
import com.wts.interceptor.OverdueCheck;
import com.wts.interceptor.PageCheck;
import com.wts.interceptor.PermissionCheck;
import com.wts.util.ExportUtil;
import com.wts.util.ParamesAPI;
import com.wts.util.Util;
import com.wts.validator.Query;
import com.wts.validator.student.Student_Edit;
import com.wts.validator.student.Student_Exist;
import com.wts.validator.student.Student_Save;
import com.wts.validator.teacher.Teacher_Edit;
import com.wts.validator.teacher.Teacher_Exist;
import com.wts.validator.teacher.Teacher_Save;
import me.chanjar.weixin.cp.bean.WxCpUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.wts.controller.MainController.getUserId;
import static com.wts.util.IDNumber.*;
import static com.wts.util.Util.PermissionString;


public class TeacherDesktop extends Controller {
  /**
   * 页面
   */
  @Before({OverdueCheck.class, PageCheck.class})
  public void Page() {
    // 未登录
    if (getSessionAttr("teacher") == null) {
      // cookie过期
      if (getCookie("dit").equals("")) {
        redirect("/desktop");
      } else {
        Teacher teacher = Teacher.dao.findById(getCookie("dit"));
        setSessionAttr("teacher", teacher);
        setCookie("dit", teacher.getId().toString(), 60 * 60 * 3);
        setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(), teacher.getId().toString()), 60 * 6 * 10);
        render("/static/html/desktop/teacher/Desktop_Teacher_Teacher.html");
      }
    } else {
      String teacherId = ((Teacher) getSessionAttr("teacher")).getId().toString();
      setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(), teacherId), 60 * 6 * 10);
      render("/static/html/desktop/teacher/Desktop_Teacher_Teacher.html");
    }
  }

  /**
   * 权限
   */
  public void Permission() {
    String teacherId = ((Teacher) getSessionAttr("teacher")).getId().toString();
    setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(), teacherId), 60 * 6 * 10);
    setCookie("name", ((Teacher) getSessionAttr("teacher")).getName(), 60 * 6 * 10);
  }

  /**
   * 查找
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Query.class})
  public void Query() {
    renderJson(Db.paginate(
            getParaToInt("pageCurrent"),
            getParaToInt("pageSize"),
            "SELECT *",
            "FROM teacher WHERE del = 0 AND name LIKE '%" + getPara("keyword") + "%' " +
                    "OR userId LIKE '%" + getPara("keyword") + "%' " +
                    "OR mobile LIKE '%" + getPara("keyword") + "%' ORDER BY id DESC").getList());
  }

  /**
   * 计数
   */
  @Before({OverdueCheck.class, PermissionCheck.class})
  public void Total() {
    Long count = Db.queryLong("SELECT COUNT(*) FROM teacher WHERE del = 0 AND name LIKE '%" + getPara("keyword") + "%' " +
            "OR userId LIKE '%" + getPara("keyword") + "%' " +
            "OR mobile LIKE '%" + getPara("keyword") + "%'");
    renderText(count.toString());
  }

  /**
   * 读取
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Teacher_Exist.class})
  public void Get() {
    renderJson(Teacher.dao.findById(getPara("id")));
  }

  /**
   * 邀请关注
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Teacher_Exist.class})
  public void Active() {
    Teacher object = Teacher.dao.findById(getPara("id"));
    if (object.get("state").toString().equals("4")) {
      renderText("该教师已处于未关注状态!");
    } else {
      User user = new User(object.get("userId").toString(), object.get("name").toString());
      user.setMobile(object.get("mobile").toString());
      user.setPartyIds(1);
      try {
        WP.me.createUser(user);
        List<String> userIds = new ArrayList<String>();
        userIds.add(object.get("userId").toString());
        WP.me.addTagUsers(ParamesAPI.teacherTagId, userIds, new ArrayList<Integer>());
        object.set("state", 1).update();
        renderText("OK");
      } catch (WeixinException e) {
        renderText(e.getErrorText());
      }
    }
  }

  /**
   * 取消关注
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Teacher_Exist.class})
  public void Inactive() {
    Teacher object = Teacher.dao.findById(getPara("id"));
    if (object.get("state").toString().equals("3")) {
      renderText("该教师已处于取消关注状态!");
    } else {
      try {
        WP.me.deleteUser(object.getUserId());
        object.set("state", 3).update();
        renderText("OK");
      } catch (WeixinException e) {
        renderText(e.getErrorText());
      }
    }
  }

  /**
   * 删除
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Teacher_Exist.class})
  public void Delete() {
    Teacher object = Teacher.dao.findById(getPara("id"));
    if (object.get("del").toString().equals("1")) {
      renderText("该教师已删除!");
    } else {
      try {
        WP.me.deleteUser(object.getUserId());
        object.set("state", 3).update();
        object.set("del", 1).update();
        renderText("OK");
      } catch (WeixinException e) {
        renderText(e.getErrorText());
      }
    }
  }

  /**
   * 保存
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Teacher_Save.class})
  public void Save() {
    User user = new User(getUserId(getPara("name")), getPara("name").trim());
    user.setMobile(getPara("mobile").trim());
    user.setPartyIds(1);
    try {
      WP.me.createUser(user);
      List<String> userIds = new ArrayList<String>();
      userIds.add(getUserId(getPara("name")));
      WP.me.addTagUsers(ParamesAPI.teacherTagId, userIds, new ArrayList<Integer>());
      Teacher object = new Teacher();
      object.set("name", getPara("name"))
              .set("mobile", getPara("mobile"))
              .set("type", getPara("type"))
              .set("state", 4)
              .set("del", 0)
              .save();
      renderText("OK");
    } catch (WeixinException e) {
      renderText(e.getErrorText());
    }
  }

  /**
   * 修改
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Teacher_Exist.class, Teacher_Edit.class})
  public void Edit() {
    try {
      Teacher object = Teacher.dao.findById(getPara("id"));
      User user = new User(object.get("userId").toString(), object.get("name").toString());
      user.setMobile(getPara("mobile").trim());
      WP.me.updateUser(user);
      object.set("name", getPara("name"))
              .set("mobile", getPara("mobile"))
              .set("type", getPara("type"))
              .update();
      renderText("OK");
    } catch (WeixinException e) {
      renderText(e.getErrorText());
    }
  }

  /**
   * 导出
   */
  @Before({OverdueCheck.class, PermissionCheck.class})
  public void Download() throws IOException {
    String[] title = {"序号", "教师姓名", "联系电话", "教师类型", "关注状态"};
    String fileName = "Teacher";
    String SQL = "select id AS 序号,name AS 教师姓名, mobile AS 联系电话, " +
            "(case type when 1 then '在编' when 2 then '聘用' when 3 then '校外' else '未知' end ) AS 教师类型, " +
            "(case state when 1 then '关注' when 2 then '已冻结' when 3 then '取消关注' when 4 then '未关注' else '错误' end ) AS 关注状态 " +
            "from teacher where del = 0 AND name like '%" + getPara("keyword") + "%' OR mobile LIKE '%" + getPara("keyword") + "%' " +
            "ORDER BY id ASC";
    ExportUtil.export(title, fileName, SQL, getResponse());
  }

  /**
   * 检测联系电话_新增
   */
  @Before(OverdueCheck.class)
  public void checkMobileForAdd() {
    if (Teacher.dao.find("SELECT * FROM teacher WHERE mobile = ?", getPara("mobile")).size() != 0) {
      renderText("该教师的联系电话已存在!");
    } else if (Parent.dao.find("SELECT * FROM parent WHERE mobile = ?", getPara("mobile")).size() != 0) {
      renderText("该教师的联系电话已存在!");
    } else {
      renderText("OK");
    }
  }

  /**
   * 检测联系电话_修改
   */
  @Before(OverdueCheck.class)
  public void checkMobileForEdit() {
    if (!Teacher.dao.findById(getPara("id")).get("mobile").equals(getPara("mobile"))
            && Db.find("SELECT * FROM teacher WHERE mobile = ?", getPara("mobile")).size() != 0) {
      renderText("该教师的联系电话已存在!");
    } else if (Db.find("SELECT * FROM parent WHERE mobile = ?", getPara("mobile")).size() != 0) {
      renderText("该教师的联系电话已存在!");
    } else {
      renderText("OK");
    }
  }

}
