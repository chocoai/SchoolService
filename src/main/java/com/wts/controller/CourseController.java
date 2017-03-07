package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.wts.entity.model.Course;
import com.wts.entity.model.Courseroom;
import com.wts.interceptor.Ajax;
import com.wts.interceptor.AjaxManager;
import com.wts.interceptor.Login;

import java.util.List;

import static com.wts.controller.SemesterController.getNow;

public class CourseController extends Controller {
  @Before(AjaxManager.class)
  public void query() {
    renderJson(Course.dao.paginate(getParaToInt("pageCurrent"), getParaToInt("pageSize"), "SELECT *", "FROM course WHERE name LIKE '%?%' OR describe LIKE '%?%' ORDER BY id ASC", getPara("queryString"), getPara("queryString")).getList());
  }
  @Before(AjaxManager.class)
  public void total() {
    Long count = Db.queryLong("SELECT COUNT(*) FROM course WHERE name LIKE '%?%' OR describe LIKE '%?%'", getPara("queryString"), getPara("queryString"));
    if (count%getParaToInt("pageSize")==0) {
      renderText((count/getParaToInt("pageSize"))+"");
    } else {
      renderText((count/getParaToInt("pageSize")+1)+"");
    }
  }

  @Before({Login.class, Ajax.class})
  public void getCourseName() {
    renderText(Course.dao.findById(getPara("courseId")).getName());
  }

  @Before({Login.class, Ajax.class})
  public void getCourse() {
    renderJson(Course.dao.findById(getPara("courseId")));
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
