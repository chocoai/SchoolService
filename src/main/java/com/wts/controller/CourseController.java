package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.wts.entity.model.Course;
import com.wts.interceptor.Ajax;
import com.wts.interceptor.AjaxManager;
import com.wts.interceptor.Login;

import java.util.List;

public class CourseController extends Controller {

    @Before({Login.class, Ajax.class})
    public void getCourseName() {
        renderText(Course.dao.findById(getPara("id")).getName());
    }
    @Before({Login.class, Ajax.class})
    public void getCourse() {
        renderJson(Course.dao.findById(getPara("id")));
    }
    @Before(AjaxManager.class)
    public  void getCourseList() {
        renderJson(Course.dao.find("select * from course"));
    }
}
