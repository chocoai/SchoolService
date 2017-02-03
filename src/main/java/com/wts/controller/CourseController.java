package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.wts.entity.model.Course;
import com.wts.interceptor.Ajax;
import com.wts.interceptor.Login;

public class CourseController extends Controller {

    @Before({Login.class, Ajax.class})
    public void getNameById() {
        Course course = Course.dao.findById(getPara("id"));
        renderText(course.get("name").toString());
    }
}
