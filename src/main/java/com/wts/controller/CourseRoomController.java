package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.model.*;
import com.wts.interceptor.AjaxTeacher;

import java.util.List;

import static com.wts.controller.SemesterController.getNow;

public class CourseRoomController extends Controller {

    private String getSQL(String sql) {
        return "FROM ((courseroomteacher " +
                "LEFT JOIN course ON courseroomteacher.course_id = course.id) " +
                "LEFT JOIN room ON courseroomteacher.room_id = room.id) " +
                "WHERE course.`name` LIKE '%" + sql +
                "%' OR course.`detail` LIKE '%" + sql +
                "%' OR room.`name` LIKE '%" + sql +
                "%' OR room.`slogan` LIKE '%" + sql +
                "%' AND courseroomteacher.semester_id= " + getNow().getId().toString() +
                " AND courseroomteacher.teacher_id= " + ((Teacher) getSessionAttr("Teacher")).getId().toString() +
                " AND course.state = 1" +
                " AND room.state = 1" +
                " ORDER BY courseroomteacher.id DESC";
    }

    /**
     * 查询
     */
    @Before(AjaxTeacher.class)
    public void query() {
        renderJson(Courseroomteacher.dao.paginate(getParaToInt("pageCurrent"), getParaToInt("pageSize"), "SELECT courseroomteacher.id, course.name AS cname, room.name AS rname", getSQL(getPara("queryString"))).getList());
    }

    /**
     * 计数
     */
    @Before(AjaxTeacher.class)
    public void total() {
        Long count = Db.queryLong("SELECT COUNT(*) " + getSQL(getPara("queryString")));
        if (count % getParaToInt("pageSize") == 0) {
            renderText((count / getParaToInt("pageSize")) + "");
        } else {
            renderText((count / getParaToInt("pageSize") + 1) + "");
        }
    }

    /**
     * 列表
     */
    @Before(AjaxTeacher.class)
    public void list() {
        List<Room> rooms = Room.dao.find("SELECT * FROM room WHERE state = 1 ORDER BY id DESC");
        String roomString;
        if (rooms.size() != 0) {
            String ro = "";
            for (Room i : rooms) {
                ro = ro + "{\"id\": \""+i.getId()+"\", " +
                        "\"name\": \""+i.getName()+"\", " +
                        "\"slogan\": \""+i+"\", " +
                        "\"state\": \""+i.getState()+"\"}, ";
            }
            roomString = "\"rooms\": [" + ro.substring(0, ro.length() - 1) + "]";
        } else {
            roomString = "\"rooms\": []";
        }
        List<Course> courses = Course.dao.find("SELECT * FROM course WHERE state = 1");
        String courseString;
        if (courses.size() != 0) {
            String ro = "";
            for (Course i : courses) {
                ro = ro + "{\"id\": \""+i.getId()+"\", " +
                        "\"name\": \""+i.getName()+"\", " +
                        "\"detail\": \""+i.getDetail()+"\", " +
                        "\"type\": \""+i.getType()+"\", " +
                        "\"state\": \""+i.getState()+"\"}, ";
            }
            courseString = "\"courses\": [" + ro.substring(0, ro.length() - 1) + "]";
        } else {
            courseString = "\"courses\": []";
        }
        renderJson("{"+roomString+","+courseString+"}");
    }
    @Before({Tx.class,AjaxTeacher.class})
    public void save()  {
        if (Courseroomteacher.dao.find("SELECT * FROM courseroomteacher " +
                "WHERE course_id = ? AND room_id = ? AND teacher_id = ? AND semester_id = ?",
                getPara("course_id"),getPara("room_id"),((Teacher) getSessionAttr("Teacher")).getId().toString(),getNow().getId().toString()).size() != 0) {
            renderText("已选择，请重新选择!");
        } else {
            if (Courseroom.dao.find("SELECT * FROM courseroom " +
                            "WHERE course_id = ? AND room_id = ? AND semester_id = ?",
                    getPara("course_id"),getPara("room_id"),getNow().getId().toString()).size() == 0) {
                Courseroom courseroom = new Courseroom();
                courseroom.set("course_id",getPara("course_id"))
                        .set("room_id",getPara("room_id"))
                        .set("semester_id",getNow().getId().toString())
                        .save();
            }
            Courseroomteacher courseroomteacher = new Courseroomteacher();
            courseroomteacher.set("course_id",getPara("course_id"))
                    .set("room_id",getPara("room_id"))
                    .set("semester_id",getNow().getId().toString())
                    .set("teacher_id",((Teacher) getSessionAttr("Teacher")).getId().toString())
                    .save();
            renderText("OK");
        }
    }
    @Before({Tx.class,AjaxTeacher.class})
    public void delete(){
        Courseroomteacher courseroomteacher = Courseroomteacher.dao.findById(getPara("id"));
        if (courseroomteacher == null) {
            renderText("要删除的记录不存在!");
        } else if(courseroomteacher.getSemesterId().equals(getNow().getId())){
            renderText("要删除的记录与当前学期不一致!");
        } else if(courseroomteacher.getTeacherId().equals(((Teacher) getSessionAttr("Teacher")).getId())){
            renderText("要删除的记录与当前教师不一致!");
        } else {
            int course = courseroomteacher.getCourseId();
            int room = courseroomteacher.getRoomId();
            int semester = courseroomteacher.getSemesterId();
            Courseroomteacher.dao.deleteById(getPara("id"));
            List<Courseroomteacher> crt = Courseroomteacher.dao.find("SELECT * FROM courseroomteacher WHERE course_id=? AND room_id=? AND semester_id=?",
                    course,room,semester);
            if (crt.size()==0){
                Courseroom.dao.deleteById(course,room,semester);
            }
            renderText("OK");
        }
    }
}
