package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.wts.entity.model.Courseroom;
import com.wts.entity.model.Courseroomteacher;
import com.wts.entity.model.Student;
import com.wts.interceptor.AjaxManager;

import java.util.List;

import static com.wts.controller.SemesterController.getNow;

public class CourseRoomController extends Controller {
//    /**
//     * 获取课程字符串
//     */
//    @Before(AjaxManager.class)
//    public void getCourse() {
//        List<Courseroom> courseRooms = Courseroom.dao.find("SELECT * FROM courseroom WHERE room_id = ? AND semester_id=?",getPara("id"),getNow().getId());
//        String course = "";
//        if (courseRooms.size() > 0) {
//            for (Courseroom c : courseRooms) {
//                course = course + c.getCourseId() + ",";
//            }
//            renderJson("[" + course.substring(0, course.length() - 1) + "]");
//        } else {
//            renderJson("[]");
//        }
//    }
    private String getSQL(String sql) {
        return "FROM courseroomteacher WHERE `name` LIKE '%" + sql +
                "%' OR `number` LIKE '%" + sql +
                "%' OR `code` LIKE '%" + sql +
                "%' OR `address` LIKE '%" + sql +
                "%' ORDER BY id DESC";
    }

    /**
     * 查询
     */
    @Before(AjaxManager.class)
    public void query() {
        renderJson(Courseroomteacher.dao.paginate(getParaToInt("pageCurrent"), getParaToInt("pageSize"), "SELECT *", getSQL(getPara("queryString"))).getList());

    }

    /**
     * 计数
     */
    @Before(AjaxManager.class)
    public void total() {
        Long count = Db.queryLong("SELECT COUNT(*) " + getSQL(getPara("queryString")));
        if (count % getParaToInt("pageSize") == 0) {
            renderText((count / getParaToInt("pageSize")) + "");
        } else {
            renderText((count / getParaToInt("pageSize") + 1) + "");
        }
    }
}
