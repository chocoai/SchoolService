package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.WP;
import com.wts.entity.model.Course;
import com.wts.entity.model.Courseroom;
import com.wts.entity.model.Teacher;
import com.wts.interceptor.Ajax;
import com.wts.interceptor.AjaxManager;
import com.wts.interceptor.Login;

import java.util.List;

import static com.wts.controller.SemesterController.getNow;
import static com.wts.util.Util.getString;

public class CourseController extends Controller {
  /**
   * 登录移动_管理_课程
   */
  public void Mobile_Manager_Course() throws WeixinException {
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
            render("/static/html/mobile/manager/Mobile_Manager_Course.html");
          } else {
            redirect("/");
          }
        } else {
          redirect("/");
        }
      } else {
        Teacher manager = Teacher.dao.findById(getCookie("dim"));
        setSessionAttr("manager", manager);
        render("/static/html/mobile/manager/Mobile_Manager_Course.html");
      }
    } else {
      render("/static/html/mobile/manager/Mobile_Manager_Course.html");
    }
  }
  /**
   * 查询
   */
  @Before(AjaxManager.class)
  public void query() {
    renderJson(Course.dao.paginate(getParaToInt("pageCurrent"), getParaToInt("pageSize"), "SELECT *", "FROM course WHERE name LIKE '%"+getPara("queryString")+"%' OR detail LIKE '%"+getPara("queryString")+"%' ORDER BY id ASC").getList());
  }
  /**
   * 计数
   */
  @Before(AjaxManager.class)
  public void total() {
    Long count = Db.queryLong("SELECT COUNT(*) FROM course WHERE name LIKE '%"+getPara("queryString")+"%' OR detail LIKE '%"+getPara("queryString")+"%'");
    if (count%getParaToInt("pageSize")==0) {
      renderText((count/getParaToInt("pageSize"))+"");
    } else {
      renderText((count/getParaToInt("pageSize")+1)+"");
    }
  }
  /**
   * 获取课程名称
   */
  @Before({Login.class, Ajax.class})
  public void getCourseName() {
    renderText(Course.dao.findById(getPara("id")).getName());
  }
  /**
   * 获取
   */
  @Before({Login.class, Ajax.class})
  public void get() {
    renderJson(Course.dao.findById(getPara("id")));
  }
  /**
   * 激活
   */
  @Before({Tx.class, AjaxManager.class})
  public void active() {
    Course course = Course.dao.findById(getPara("id"));
    if (course == null) {
      renderText("要重新激活的课程不存在!");
    } else if (course.get("state").toString().equals("1")) {
      renderText("该课程已激活!");
    } else {
      course.set("state", 1).update();
      renderText("OK");
    }
  }
  /**
   * 注销
   */
  @Before({Tx.class, AjaxManager.class})
  public void inactive() {
    Course course = Course.dao.findById(getPara("id"));
    if (course == null) {
      renderText("要重新注销的课程不存在!");
    } else if (course.get("state").toString().equals("0")) {
      renderText("该课程已注销!");
    } else {
      course.set("state", 0).update();
      renderText("OK");
    }
  }
  /**
   * 保存
   */
  @Before({Tx.class, AjaxManager.class})
  public void save() {
    if (Course.dao.find("SELECT * FROM course WHERE name = ?", getPara("name")).size() != 0) {
      renderText("该名称已存在!");
    } else {
      Course course = new Course();
      course.set("name", getPara("name"))
              .set("detail", getPara("detail"))
              .set("type", getPara("type"))
              .set("state", 1)
              .save();
      renderText("OK");
    }
  }
  /**
   * 修改
   */
  @Before({Tx.class, AjaxManager.class})
  public void edit() {
    Course course = Course.dao.findById(getPara("id"));
    if (course == null) {
      renderText("要修改的课程不存在!");
    } else {
      if (!getString(course.getStr("name")).equals(getPara("name"))
              && Course.dao.find("SELECT * FROM course WHERE name = ?", getPara("name")).size() != 0) {
        renderText("该名称已存在!");
      } else {
        course.set("name", getPara("name"))
                .set("detail", getPara("detail"))
                .set("type", getPara("type"))
                .update();
        renderText("OK");
      }
    }
  }
  /**
   * 检测名称_新增
   */
  @Before(AjaxManager.class)
  public void checkNameForAdd() {
    if (Course.dao.find("SELECT * FROM course WHERE name = ?", getPara("name")).size() != 0) {
      renderText("该课程名称已存在!");
    } else {
      renderText("OK");
    }
  }

  /**
   * 检测名称_修改
   */
  @Before(AjaxManager.class)
  public void checkNameForEdit() {
    if (!Course.dao.findById(getPara("id")).get("name").equals(getPara("name"))
            && Course.dao.find("SELECT * FROM course WHERE name = ?", getPara("name")).size() != 0) {
      renderText("该课程名称已存在!");
    } else {
      renderText("OK");
    }
  }
  @Before(AjaxManager.class)
  public void getCourseListAll() {
    renderJson(Course.dao.find("SELECT * FROM course"));
  }

  @Before(AjaxManager.class)
  public void getCourseListAble() {
    renderJson(Course.dao.find("SELECT * FROM course WHERE state = 1"));
  }

  @Before(AjaxManager.class)
  public void getCourseListUnable() {
    renderJson(Course.dao.find("SELECT * FROM course WHERE state = 0"));
  }

  @Before(AjaxManager.class)
  public void getCourseAListAble() {
    renderJson(Course.dao.find("SELECT * FROM course WHERE state = 1 AND type = 1"));
  }

  @Before(AjaxManager.class)
  public void getCourseAListUnable() {
    renderJson(Course.dao.find("SELECT * FROM course WHERE state = 0 AND type = 1"));
  }

  @Before(AjaxManager.class)
  public void getCourseBListAble() {
    renderJson(Course.dao.find("SELECT * FROM course WHERE state = 1 AND type = 2"));
  }

  @Before(AjaxManager.class)
  public void getCourseBListUnable() {
    renderJson(Course.dao.find("SELECT * FROM course WHERE state = 0 AND type = 2"));
  }

  @Before(AjaxManager.class)
  public void getCourseAble() {
    List<Course> courses = Course.dao.find("SELECT * FROM course WHERE state = 1");
    String ss = "";
    if (courses.size() != 0) {
      for (Course i : courses) {
        if (Courseroom.dao.find("SELECT * FROM courseroom WHERE course_id = ? AND room_id = ? AND semester_id = ?", i.getId(), getPara("roomId"), getNow().getId()).size() != 0) {
          ss = ss + "'course" + i.getId() + "': true,";
        } else {
          ss = ss + "'course" + i.getId() + "': false,";
        }
      }
    }
    renderText("{" + ss.substring(0, ss.length() - 1) + "}");
  }

}
