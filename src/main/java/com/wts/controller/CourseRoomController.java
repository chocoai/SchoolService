package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.wts.entity.model.Courseroom;
import com.wts.interceptor.AjaxManager;

import java.util.List;

import static com.wts.controller.SemesterController.getNow;

public class CourseRoomController extends Controller {
    /**
     * 获取课程字符串
     */
    @Before(AjaxManager.class)
    public void getCourse() {
        List<Courseroom> courseRooms = Courseroom.dao.find("SELECT * FROM courseroom WHERE room_id = ? AND semester_id=?",getPara("id"),getNow().getId());
        String course = "";
        if (courseRooms.size() > 0) {
            for (Courseroom c : courseRooms) {
                course = course + c.getCourseId() + ",";
            }
            renderJson("[" + course.substring(0, course.length() - 1) + "]");
        } else {
            renderJson("[]");
        }
    }
}
