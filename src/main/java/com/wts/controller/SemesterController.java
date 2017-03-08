package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.WP;
import com.wts.entity.model.Semester;
import com.wts.entity.model.Teacher;
import com.wts.interceptor.AjaxManager;

import static com.wts.util.Util.getString;

public class SemesterController extends Controller {
  /**
   * 登录移动_管理_学期
   */
  public void Mobile_Manager_Semester() throws WeixinException {
    if (getSessionAttr("manager") == null || ((Teacher) getSessionAttr("manager")).getIsManager() != 1) {
      if (getCookie("dim") == null || getCookie("dim").equals("")) {
        if (!(getPara("code") == null || getPara("code").equals(""))) {
          User user = WP.me.getUserByCode(getPara("code"));
          Teacher manager = Teacher.dao.findFirst("SELECT * FROM teacher WHERE userId = ? AND state = ? AND isManager = 1", user.getUserId(), 1);
          if (manager != null) {
            setSessionAttr("manager", manager);
            setCookie("dim", manager.getId().toString(), 60 * 30);
            if(user.getAvatar().equals(manager.getPicUrl())){
              manager.set("picUrl", user.getAvatar()).update();
            }
            render("/static/html/mobile/manager/Mobile_Manager_Semester.html");
          } else {
            redirect("/");
          }
        } else {
          redirect("/");
        }
      } else {
        Teacher manager = Teacher.dao.findById(getCookie("dim"));
        setSessionAttr("manager", manager);
        render("/static/html/mobile/manager/Mobile_Manager_Semester.html");
      }
    } else {
      render("/static/html/mobile/manager/Mobile_Manager_Semester.html");
    }
  }

  /**
   * 当前学期
   */
  public static Semester getNow() {
    return Semester.dao.findFirst("SELECT * FROM semester WHERE state = 1");
  }

  /**
   * 列表
   */
  @Before(AjaxManager.class)
  public void list() {
    renderJson(Semester.dao.find("SELECT * FROM semester ORDER BY id DESC"));
  }

  /**
   * 检测名称_新增
   */
  @Before(AjaxManager.class)
  public void checkNameForAdd() {
    if (Semester.dao.find("SELECT * FROM semester WHERE name = ?", getPara("name")).size() != 0) {
      renderText("该学期名称已存在!");
    } else {
      renderText("OK");
    }
  }

  /**
   * 检测名称_修改
   */
  @Before(AjaxManager.class)
  public void checkNameForEdit() {
    if (!Semester.dao.findById(getPara("semesterId")).get("name").equals(getPara("name"))
            && Semester.dao.find("SELECT * FROM semester WHERE name = ?", getPara("name")).size() != 0) {
      renderText("该学期名称已存在!");
    } else {
      renderText("OK");
    }
  }

  /**
   * 获取
   */
  @Before(AjaxManager.class)
  public void get() {
    renderJson(Semester.dao.findById(getPara("semesterId")));
  }

  /**
   * 查询
   */
  @Before(AjaxManager.class)
  public void query() {
    renderJson(Semester.dao.paginate(getParaToInt("pageCurrent"), getParaToInt("pageSize"), "SELECT *", "FROM semester WHERE name LIKE '%" + getPara("queryString") + "%' ORDER BY id DESC").getList());
  }

  /**
   * 计数
   */
  @Before(AjaxManager.class)
  public void total() {
    Long count = Db.queryLong("SELECT COUNT(*) FROM semester WHERE name LIKE '%" + getPara("queryString") + "%'");
    if (count % getParaToInt("pageSize") == 0) {
      renderText((count / getParaToInt("pageSize")) + "");
    } else {
      renderText((count / getParaToInt("pageSize") + 1) + "");
    }
  }

  /**
   * 激活
   */
  @Before({Tx.class, AjaxManager.class})
  public void active() {
    Semester semester = Semester.dao.findById(getPara("semesterId"));
    if (semester == null) {
      renderText("要重新激活的学期不存在!");
    } else if (semester.get("state").toString().equals("1")) {
      renderText("该学期已激活!");
    } else {
      semester.set("state", 1).update();
      Db.update("UPDATE semester SET state = 0 WHERE id <> ?", getPara("semesterId"));
      renderText("OK");
    }
  }

  /**
   * 保存
   */
  @Before({Tx.class, AjaxManager.class})
  public void save() {
    if (Semester.dao.find("SELECT * FROM semester WHERE name = ?", getPara("name")).size() != 0) {
      renderText("该名称已存在!");
    } else if(getParaToDate("timeStart").after(getParaToDate("timeEnd"))){
      renderText("开始时间晚于终止时间!");
    } else {
      Semester semester = new Semester();
      semester.set("name", getPara("name"))
              .set("time_start", getPara("timeStart"))
              .set("time_end", getPara("timeEnd"))
              .set("state", 0)
              .save();
      renderText("OK");
    }
  }

  /**
   * 修改
   */
  @Before({Tx.class, AjaxManager.class})
  public void edit() {
    Semester semester = Semester.dao.findById(getPara("semesterId"));
    if (semester == null) {
      renderText("要修改的学期不存在!");
    } else {
      if (!getString(semester.getStr("name")).equals(getPara("name"))
              && Semester.dao.find("SELECT * FROM semester WHERE name = ?", getPara("name")).size() != 0) {
        renderText("该名称已存在!");
      } else if(getParaToDate("timeStart").after(getParaToDate("timeEnd"))){
        renderText("开始时间晚于终止时间!");
      } else {
        semester.set("name", getPara("name"))
                .set("time_start", getPara("timeStart"))
                .set("time_end", getPara("timeEnd"))
                .update();
        renderText("OK");
      }
    }
  }
}
