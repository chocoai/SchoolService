package com.wts.controller.Desktop.Teacher;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.wts.entity.WP;
import com.wts.entity.model.Parent;
import com.wts.entity.model.Teacher;
import com.wts.interceptor.OverdueCheck;
import com.wts.interceptor.PageCheck;
import com.wts.interceptor.PermissionCheck;
import com.wts.util.ExportUtil;
import com.wts.util.ParamesAPI;
import com.wts.validator.Query;
import com.wts.validator.parent.Parent_Edit;
import com.wts.validator.parent.Parent_Exist;
import com.wts.validator.parent.Parent_Save;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.wts.controller.MainController.getUserId;
import static com.wts.util.Util.PermissionString;


public class ParentDesktop extends Controller {
  private static Logger logger = Logger.getLogger(ParentDesktop.class);

  /**
   * 页面
   */
  @Before({OverdueCheck.class, PageCheck.class})
  public void Page() {
    // 未登录
    if (getSessionAttr("Teacher") == null) {
      // cookie过期
      if (getCookie("dit").equals("")) {
        redirect("/desktop");
      } else {
        Teacher teacher = Teacher.dao.findById(getCookie("dit"));
        setSessionAttr("Teacher", teacher);
        setCookie("dit", teacher.getId().toString(), 60 * 60 * 3);
        setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(), teacher.getId().toString()), 60 * 6 * 10);
        render("/static/html/desktop/Teacher/Desktop_Teacher_Parent.html");
      }
    } else {
      String teacherId = ((Teacher) getSessionAttr("Teacher")).getId().toString();
      setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(), teacherId), 60 * 6 * 10);
      render("/static/html/desktop/Teacher/Desktop_Teacher_Parent.html");
    }
  }

  /**
   * 权限
   */
  public void Permission() {
    String teacherId = ((Teacher) getSessionAttr("Teacher")).getId().toString();
    setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(), teacherId), 60 * 6 * 10);
    setCookie("name", ((Teacher) getSessionAttr("Teacher")).getName(), 60 * 6 * 10);
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
            "FROM parent WHERE del = 0 AND (name LIKE '%" + getPara("keyword") + "%' " +
                    "OR userId LIKE '%" + getPara("keyword") + "%' " +
                    "OR mobile LIKE '%" + getPara("keyword") + "%') ORDER BY id DESC").getList());
  }

  /**
   * 计数
   */
  @Before({OverdueCheck.class, PermissionCheck.class})
  public void Total() {
    Long count = Db.queryLong("SELECT COUNT(*) FROM parent WHERE del = 0 AND (name LIKE '%" + getPara("keyword") + "%' " +
            "OR userId LIKE '%" + getPara("keyword") + "%' " +
            "OR mobile LIKE '%" + getPara("keyword") + "%')");
    renderText(count.toString());
  }

  /**
   * 读取
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Parent_Exist.class})
  public void Get() {
    renderJson(Parent.dao.findById(getPara("id")));
  }

  /**
   * 邀请关注
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Parent_Exist.class})
  public void Active() {
    Parent object = Parent.dao.findById(getPara("id"));
    if (object.get("state").toString().equals("4")) {
      renderText("该家长已处于未关注状态!");
    } else {
      User user = new User(object.get("userId").toString(), object.get("name").toString());
      user.setMobile(object.get("mobile").toString());
      user.setPartyIds(1);
      try {
        WP.me.createUser(user);
        List<String> userIds = new ArrayList<String>();
        userIds.add(object.get("userId").toString());
        WP.me.addTagUsers(ParamesAPI.parentTagId, userIds, new ArrayList<Integer>());
        object.set("state", 1).update();
        logger.warn("function:"+this.getClass().getSimpleName()+"/Active;"+"teacher_id:"+((Teacher) getSessionAttr("Teacher")).getId().toString()+";parent_id:"+getPara("id")+";");
        renderText("OK");
      } catch (WeixinException e) {
        renderText(e.getErrorText());
      }
    }
  }

  /**
   * 取消关注
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Parent_Exist.class})
  public void Inactive() {
    Parent object = Parent.dao.findById(getPara("id"));
    if (object.get("state").toString().equals("3")) {
      renderText("该家长已处于取消关注状态!");
    } else {
      try {
        WP.me.deleteUser(object.getUserId());
        object.set("state", 3).update();
        logger.warn("function:"+this.getClass().getSimpleName()+"/Inactive;"+"teacher_id:"+((Teacher) getSessionAttr("Teacher")).getId().toString()+";parent_id:"+getPara("id")+";");
        renderText("OK");
      } catch (WeixinException e) {
        renderText(e.getErrorText());
      }
    }
  }

  /**
   * 删除
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Parent_Exist.class})
  public void Delete() {
    Parent object = Parent.dao.findById(getPara("id"));
    if (object.get("del").toString().equals("1")) {
      renderText("该家长已删除!");
    } else {
      try {
        WP.me.deleteUser(object.getUserId());
        object.set("state", 3).update();
        object.set("del", 1).update();
        logger.warn("function:"+this.getClass().getSimpleName()+"/Delete;"+"teacher_id:"+((Teacher) getSessionAttr("Teacher")).getId().toString()+";parent_id:"+getPara("id")+";");
        renderText("OK");
      } catch (WeixinException e) {
        renderText(e.getErrorText());
      }
    }
  }

  /**
   * 保存
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Parent_Save.class})
  public void Save() {
    User user = new User(getUserId(getPara("name")), getPara("name").trim());
    user.setMobile(getPara("mobile").trim());
    user.setPartyIds(1);
    try {
      WP.me.createUser(user);
      List<String> userIds = new ArrayList<String>();
      userIds.add(getUserId(getPara("name")));
      WP.me.addTagUsers(ParamesAPI.parentTagId, userIds, new ArrayList<Integer>());
      Parent object = new Parent();
      object.set("name", getPara("name"))
              .set("userId", getUserId(getPara("name")))
              .set("mobile", getPara("mobile"))
              .set("state", 4)
              .set("del", 0)
              .save();
      logger.warn("function:"+this.getClass().getSimpleName()+"/Save;"+"teacher_id:"+((Teacher) getSessionAttr("Teacher")).getId().toString()+";parent_id:"+object.get("id")+";");
      renderText("OK");
    } catch (WeixinException e) {
      renderText(e.getErrorText());
    }
  }

  /**
   * 修改
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Parent_Exist.class, Parent_Edit.class})
  public void Edit() {
    try {
      Parent object = Parent.dao.findById(getPara("id"));
      User user = new User(object.get("userId").toString(), object.get("name").toString());
      user.setMobile(getPara("mobile").trim());
      WP.me.updateUser(user);
      object.set("name", getPara("name"))
              .set("mobile", getPara("mobile"))
              .update();
      logger.warn("function:"+this.getClass().getSimpleName()+"/Edit;"+"teacher_id:"+((Teacher) getSessionAttr("Teacher")).getId().toString()+";parent_id:"+getPara("id")+";");
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
    String[] title = {"序号", "家长姓名", "联系电话",  "关注状态"};
    String fileName = "Parent";
    String SQL = "select id AS 序号,name AS 家长姓名, mobile AS 联系电话, " +
            "(case state when 1 then '关注' when 2 then '已冻结' when 3 then '取消关注' when 4 then '未关注' else '错误' end ) AS 关注状态 " +
            "from parent where del = 0 AND name like '%" + getPara("keyword") + "%' OR mobile LIKE '%" + getPara("keyword") + "%' " +
            "ORDER BY id ASC";
    ExportUtil.export(title, fileName, SQL, getResponse());
  }

  /**
   * 检测联系电话_新增
   */
  @Before(OverdueCheck.class)
  public void checkMobileForAdd() {
    if (Teacher.dao.find("SELECT * FROM Teacher WHERE mobile = ?", getPara("mobile")).size() != 0) {
      renderText("该家长的联系电话已存在!");
    } else if (Parent.dao.find("SELECT * FROM parent WHERE mobile = ?", getPara("mobile")).size() != 0) {
      renderText("该家长的联系电话已存在!");
    } else {
      renderText("OK");
    }
  }

  /**
   * 检测联系电话_修改
   */
  @Before(OverdueCheck.class)
  public void checkMobileForEdit() {
    if (Db.find("SELECT * FROM Teacher WHERE mobile = ?", getPara("mobile")).size() != 0) {
      renderText("该家长的联系电话已存在!");
    } else if (!Parent.dao.findById(getPara("id")).get("mobile").equals(getPara("mobile"))
            && Db.find("SELECT * FROM parent WHERE mobile = ?", getPara("mobile")).size() != 0) {
      renderText("该家长的联系电话已存在!");
    } else {
      renderText("OK");
    }
  }

}
