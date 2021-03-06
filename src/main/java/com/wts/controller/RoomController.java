//package com.wts.controller;
//
//import com.foxinmy.weixin4j.exception.WeixinException;
//import com.foxinmy.weixin4j.qy.model.Tag;
//import com.foxinmy.weixin4j.qy.model.User;
//import com.jfinal.aop.Before;
//import com.jfinal.core.Controller;
//import com.jfinal.plugin.activerecord.Db;
//import com.jfinal.plugin.activerecord.Record;
//import com.jfinal.plugin.activerecord.tx.Tx;
//import com.wts.entity.WP;
//import com.wts.entity.model.*;
//import com.wts.interceptor.*;
//import com.wts.util.Util;
//
//import java.util.List;
//
//import static com.jfinal.plugin.activerecord.Db.find;
//import static com.wts.controller.SemesterController.getNow;
//
//public class RoomController extends Controller {
//    private String getSQL(String sql) {
//        return "FROM Room WHERE `name` LIKE '%" + sql +
//                "%' OR `year` LIKE '%" + sql +
//                "%' OR `order` LIKE '%" + sql +
//                "%' OR `slogan` LIKE '%" + sql +
//                "%' ORDER BY id DESC";
//    }
//
//    /**
//     * 登录移动_管理_班级
//     */
//    public void Mobile_Manager_Room() throws WeixinException {
//        if (getSessionAttr("manager") == null || ((Teacher) getSessionAttr("manager")).getIsManager() != 1) {
//            if (getCookie("dim") == null || getCookie("dim").equals("")) {
//                if (!(getPara("code") == null || getPara("code").equals(""))) {
//                    User user = WP.me.getUserByCode(getPara("code"));
//                    Teacher manager = Teacher.dao.findFirst("SELECT * FROM Teacher WHERE userId = ? AND state = ? AND isManager = 1", user.getUserId(), 1);
//                    if (manager != null) {
//                        setSessionAttr("manager", manager);
//                        setCookie("dim", manager.getId().toString(), 60 * 30);
//                        if (user.getAvatar().equals(manager.getPicUrl())) {
//                            manager.set("picUrl", user.getAvatar()).update();
//                        }
//                        render("/static/html/mobile/manager/Mobile_Manager_Room.html");
//                    } else {
//                        redirect("/");
//                    }
//                } else {
//                    redirect("/");
//                }
//            } else {
//                Teacher manager = Teacher.dao.findById(getCookie("dim"));
//                setSessionAttr("manager", manager);
//                render("/static/html/mobile/manager/Mobile_Manager_Room.html");
//            }
//        } else {
//            render("/static/html/mobile/manager/Mobile_Manager_Room.html");
//        }
//    }
//
//    /**
//     * 列表
//     */
//    @Before(AjaxManager.class)
//    public void list() {
//        renderJson(Room.dao.find("SELECT * FROM Room WHERE state = 1 ORDER BY id DESC"));
//    }
//
//    /**
//     * 查询
//     */
//    @Before(AjaxManager.class)
//    public void query() {
//        renderJson(Room.dao.paginate(getParaToInt("pageCurrent"), getParaToInt("pageSize"), "SELECT *", getSQL(getPara("queryString"))).getList());
//
//    }
//
//    /**
//     * 计数
//     */
//    @Before(AjaxManager.class)
//    public void total() {
//        Long count = Db.queryLong("SELECT COUNT(*) " + getSQL(getPara("queryString")));
//        if (count % getParaToInt("pageSize") == 0) {
//            renderText((count / getParaToInt("pageSize")) + "");
//        } else {
//            renderText((count / getParaToInt("pageSize") + 1) + "");
//        }
//    }
//
//    /**
//     * 获取
//     */
//    @Before(AjaxManager.class)
//    public void get() {
//        renderJson(Room.dao.findById(getPara("id")));
//    }
//
//    /**
//     * 激活
//     */
//    @Before({Tx.class, AjaxManager.class})
//    public void active() {
//        Room Room = Room.dao.findById(getPara("id"));
//        if (Room == null) {
//            renderText("要重新激活的班级不存在!");
//        } else if (Room.get("state").toString().equals("1")) {
//            renderText("该班级已激活!");
//        } else {
//            Room.set("state", 1).update();
//            renderText("OK");
//        }
//    }
//
//    /**
//     * 注销
//     */
//    @Before({Tx.class, AjaxManager.class})
//    public void inactive() {
//        Room Room = Room.dao.findById(getPara("id"));
//        if (Room == null) {
//            renderText("未找到指定id的班级");
//        } else if (Room.get("state").toString().equals("2")) {
//            renderText("该班级已注销!");
//        } else {
//            Room.set("state", 2).update();
//            renderText("OK");
//        }
//    }
//
//    /**
//     * 检测名称_新增
//     */
//    @Before(AjaxManager.class)
//    public void checkNameForAdd() {
//        if (!getPara("name").matches("\\d{4}[\\u7ea7]\\d{1,2}[\\u73ed]")) {
//            renderText("班级名称格式应为：XXXX级XX班!");
//        } else if (Room.dao.find("SELECT * FROM Room WHERE name = ?", getPara("name")).size() != 0) {
//            renderText("该班级名称已存在!");
//        } else {
//            renderText("OK");
//        }
//    }
//
//    /**
//     * 检测名称_修改
//     */
//    @Before(AjaxManager.class)
//    public void checkNameForEdit() {
//        if (!getPara("name").matches("\\d{4}[\\u7ea7]\\d{1,2}[\\u73ed]")) {
//            renderText("班级名称格式应为：XXXX级XX班!");
//        } else if (!Room.dao.findById(getPara("id")).getName().equals(getPara("name"))
//                && Room.dao.find("SELECT * FROM Room WHERE name = ?", getPara("name")).size() != 0) {
//            renderText("该班级名称已存在!");
//        } else {
//            renderText("OK");
//        }
//    }
//
//    /**
//     * 检测标语_新增
//     */
//    @Before(AjaxManager.class)
//    public void checkSloganForAdd() {
//        if (Room.dao.find("SELECT * FROM Room WHERE slogan = ?", getPara("slogan")).size() != 0) {
//            renderText("该班级标语已存在!");
//        } else {
//            renderText("OK");
//        }
//    }
//
//    /**
//     * 检测标语_修改
//     */
//    @Before(AjaxManager.class)
//    public void checkSloganForEdit() {
//        if (!Room.dao.findById(getPara("id")).equals(getPara("slogan"))
//                && Room.dao.find("SELECT * FROM Room WHERE slogan = ?", getPara("slogan")).size() != 0) {
//            renderText("该班级标语已存在!");
//        } else {
//            renderText("OK");
//        }
//    }
//
//    /**
//     * 保存
//     */
//    @Before({Tx.class, AjaxManager.class})
//    public void save() {
//        if (!getPara("year").matches("\\d{4}")) {
//            renderText("入学年份应为4位数字");
//        } else if (!getPara("order").matches("\\d{1,2}")) {
//            renderText("班级序号应为1-2位数字");
//        } else if (Room.dao.find("SELECT * FROM Room WHERE `year`=? AND `order`=?", getPara("year"), getPara("order")).size() != 0) {
//            renderText("该班级已存在!");
//        } else if (Room.dao.find("SELECT * FROM Room WHERE slogan=?", getPara("slogan")).size() != 0) {
//            renderText("该班级标语已存在!");
//        } else if (Room.dao.find("SELECT * FROM Room WHERE name=?", getPara("year") + "级" + getPara("order") + "班").size() != 0) {
//            renderText("该班级标签已存在!");
//        } else {
//            Room Room = new Room();
//            Room.set("name", getPara("year") + "级" + getPara("order") + "班")
//                    .set("year", getPara("year"))
//                    .set("order", getPara("order"))
//                    .set("slogan", getPara("slogan"))
//                    .set("state", 1).save();
//            try {
//                WP.me.createTag(new Tag(Room.getId(), Room.getName()));
//            } catch (WeixinException e) {
//                renderText(e.getMessage());
//            }
//            renderText("OK");
//        }
//    }
//
//    /**
//     * 修改
//     */
//    @Before({Tx.class, AjaxManager.class})
//    public void edit() {
//        Room Room = Room.dao.findById(getPara("id"));
//        if (Room == null) {
//            renderText("要修改的班级不存在!");
//        } else {
//            if (Util.getString(Room.getStr("year")).equals(getPara("year").trim())
//                    && Util.getString(Room.getStr("order")).equals(getPara("order").trim())
//                    && Util.getString(Room.getStr("slogan")).equals(getPara("slogan").trim())
//                    && Util.getString(Room.getStr("name")).equals(getPara("name").trim())
//                    ) {
//                renderText("未找到修改内容!");
//            } else if (!getPara("year").matches("\\d{4}")) {
//                renderText("入学年份应为4位数字");
//            } else if (!getPara("order").matches("\\d{1,2}")) {
//                renderText("班级序号应为1-2位数字");
//            } else if (Room.dao.find("SELECT * FROM Room WHERE slogan=?", getPara("slogan")).size() != 0) {
//                renderText("该班级标语已存在!");
//            } else if (Room.dao.find("SELECT * FROM Room WHERE name=?", getPara("year") + "级" + getPara("order") + "班").size() != 0) {
//                renderText("该班级标签已存在!");
//            } else if (!Util.getString(Room.getStr("year")).equals(getPara("year"))
//                    && !Util.getString(Room.getStr("order")).equals(getPara("order"))
//                    && Room.dao.find("SELECT * FROM Room WHERE `year`=? AND `order`=?", getPara("year"), getPara("order")).size() != 0) {
//                renderText("该班级已存在!");
//            } else {
//                Room.set("name", getPara("year") + "级" + getPara("order") + "班")
//                        .set("year", getPara("year"))
//                        .set("order", getPara("order"))
//                        .set("slogan", getPara("slogan"))
//                        .update();
//                if (!Util.getString(Room.getStr("name")).equals(getPara("name").trim())) {
//                    try {
//                        WP.me.updateTag(new Tag(Room.getId(), Room.getName()));
//                    } catch (WeixinException e) {
//                        renderText(e.getMessage());
//                    }
//                }
//                renderText("OK");
//            }
//        }
//    }
//
//
//
//    @Before(AjaxParent.class)
//    public void listOfParent() {
//        String SQL = "SELECT DISTINCT Room.id, Room.name, Room.state" +
//                " FROM ((roomstudent" +
//                " LEFT JOIN Room ON Room.id = roomstudent.room_id)" +
//                " LEFT JOIN Student ON Student.id = roomstudent.student_id)" +
//                " WHERE Room.state = 1 AND Student.state = 1 AND Student.id" +
//                " IN (SELECT DISTINCT student_id FROM relation WHERE parent_id = ?)";
//        renderJson(Db.find(SQL, ((Parent) getSessionAttr("Parent")).getId()));
//    }
//
//    @Before(AjaxParent.class)
//    public void firstOfParent() {
//        String SQL = "SELECT DISTINCT Room.id, Room.name, Room.state" +
//                " FROM ((roomstudent" +
//                " LEFT JOIN Room ON Room.id = roomstudent.room_id)" +
//                " LEFT JOIN Student ON Student.id = roomstudent.student_id)" +
//                " WHERE Room.state = 1 AND Student.state = 1 AND Student.id" +
//                " IN (SELECT DISTINCT student_id FROM relation WHERE parent_id = ?)";
//        Record record = Db.findFirst(SQL, ((Parent) getSessionAttr("Parent")).getId());
//        if (record != null) {
//            renderText(record.get("id").toString());
//        } else {
//            renderText("0");
//        }
//    }
//
//    @Before(AjaxTeacher.class)
//    public void listOfTeacher() {
//        String SQL = "SELECT DISTINCT Room.id, Room.name, Room.state" +
//                " FROM (((courseroomteacher" +
//                " LEFT JOIN Room ON Room.id = courseroomteacher.room_id)" +
//                " LEFT JOIN Teacher ON Teacher.id = courseroomteacher.teacher_id)" +
//                " LEFT JOIN Semester ON Semester.id = courseroomteacher.semester_id)" +
//                " WHERE Room.state = 1 AND Teacher.id = ? AND Semester.id = ?";
//        renderJson(Db.find(SQL, ((Teacher) getSessionAttr("Teacher")).getId(), Semester.dao.findFirst(Semester.dao.getSql("Semester.used")).getId()));
//    }
//
//    @Before(AjaxTeacher.class)
//    public void firstOfTeacher() {
//        String SQL = "SELECT DISTINCT Room.id, Room.name, Room.state" +
//                " FROM (((courseroomteacher" +
//                " LEFT JOIN Room ON Room.id = courseroomteacher.room_id)" +
//                " LEFT JOIN Teacher ON Teacher.id = courseroomteacher.teacher_id)" +
//                " LEFT JOIN Semester ON Semester.id = courseroomteacher.semester_id)" +
//                " WHERE Room.state = 1 AND Teacher.id = ? AND Semester.id = ?";
//        Record record = Db.findFirst(SQL, ((Teacher) getSessionAttr("Teacher")).getId(), Semester.dao.findFirst(Semester.dao.getSql("Semester.used")).getId());
//        if (record != null) {
//            renderText(record.get("id").toString());
//        } else {
//            renderText("0");
//        }
//    }
//
//    @Before(AjaxTeacher.class)
//    public void roomCourseList() {
//        String SQL = "SELECT DISTINCT Course.name,courseroomteacher.course_id" +
//                "FROM courseroomteacher" +
//                "LEFT JOIN Course ON courseroomteacher.course_id = Course.id" +
//                "WHERE courseroomteacher.teacher_id = ? AND courseroomteacher.semester_id = ? AND courseroomteacher.room_id = ?";
//        renderJson(Db.find(SQL, ((Teacher) getSessionAttr("Teacher")).getId(), Semester.dao.findFirst(Semester.dao.getSql("Semester.used")).getId(), getPara("id")));
//    }
//
//    @Before(AjaxTeacher.class)
//    public void roomCourseFirst() {
//        String SQL = "SELECT DISTINCT Course.name,courseroomteacher.course_id" +
//                "FROM courseroomteacher" +
//                "LEFT JOIN Course ON courseroomteacher.course_id = Course.id" +
//                "WHERE courseroomteacher.teacher_id = ? AND courseroomteacher.semester_id = ? AND courseroomteacher.room_id = ?";
//        Record record = Db.findFirst(SQL, ((Teacher) getSessionAttr("Teacher")).getId(), Semester.dao.findFirst(Semester.dao.getSql("Semester.used")).getId(), getPara("id"));
//        if (record != null) {
//            renderText(record.get("id").toString());
//        } else {
//            renderText("0");
//        }
//    }
//
//    @Before({Login.class, OverdueCheck.class})
//    public void getName() {
//        renderText(Room.dao.findById(getPara("id")).get("name").toString());
//    }
//
//    public void getCourseRoomTeachers() {
//        List<Courseroomteacher> courseroomteacher = Courseroomteacher.dao.find("select * from courseroomteacher where room_id=? and course_id=? and semester_id=?", getPara("Room"), getPara("Course"), Semester.dao.findFirst(Semester.dao.getSql("Semester.used")).getId());
//        if (courseroomteacher.size() != 0) {
//            String sp1 = "";
//            for (int i = 0; i < courseroomteacher.size(); i++) {
//                sp1 = sp1 + "'" + courseroomteacher.get(i).get("teacher_id") + "',";
//            }
//            renderText("{Course: [" + sp1.substring(0, sp1.length() - 1) + "]}");
//        } else {
//            renderText("{}");
//        }
//    }
//
//    private String crt(String courseId, String roomId) {
//        List<Courseroomteacher> courseroomteacher = Courseroomteacher.dao.find("select * from courseroomteacher where room_id=? and course_id=? and semester_id=?", roomId, courseId, getNow().getId());
//        String crt = "";
//        if (courseroomteacher.size() != 0) {
//            for (int i = 0; i < courseroomteacher.size(); i++) {
//                crt = crt + "'" + courseroomteacher.get(i).get("teacher_id") + "',";
//            }
//            crt = "Course" + courseId + ": [" + crt.substring(0, crt.length() - 1) + "]";
//        } else {
//            crt = "Course" + courseId + ": []";
//        }
//        return crt;
//    }
//
//    @Before(AjaxManager.class)
//    public void getCourseRoomTeacher() {
//        List<Course> courses = Course.dao.find("SELECT * FROM Course WHERE state = 1");
//        String crt = "";
//        if (courses.size() != 0) {
//            for (int i = 0; i < courses.size(); i++) {
//                crt = crt + crt(courses.get(i).get("id").toString(), getPara("roomId")) + ",";
//            }
//        }
//        renderText("{" + crt.substring(0, crt.length() - 1) + "}");
//    }
//    /**
//     * 班级课程页面初始化时的字符串
//     */
//    @Before({Login.class, OverdueCheck.class})
//    public void getString() {
//        String semesterName, roomName,roomState, courseA, courseB, courseAbleA, courseAbleB;
//        Room Room = Room.dao.findById(getPara("id"));
//        roomName = "\"roomName\": \"" + Room.getName() + "\"";
//        roomState = "\"roomState\": \"" + Room.getState() + "\"";
//        Semester Semester = getNow();
//        if (Semester != null) {
//            semesterName = "\"semesterName\": \"" + Semester.getName() + "\"";
//            List<Courseroom> courseroomsA = Courseroom.dao.find("SELECT * FROM courseroom LEFT JOIN Course ON courseroom.course_Id = Course.id WHERE Course.state = 1 AND Course.type = 1 AND courseroom.room_id=? AND courseroom.semester_id=?", Room.getId(), Semester.getId());
//            String sra = "";
//            if (courseroomsA.size() != 0) {
//                for (Courseroom i : courseroomsA) {
//                    sra = sra + "" + i.getCourseId() + ",";
//                }
//                courseAbleA = "\"courseAbleA\": [" + sra.substring(0, sra.length() - 1) + "]";
//            } else {
//                courseAbleA = "\"courseAbleA\": []";
//            }
//            List<Courseroom> courseroomsB = Courseroom.dao.find("SELECT * FROM courseroom LEFT JOIN Course ON courseroom.course_Id = Course.id WHERE Course.state = 1 AND Course.type = 2 AND courseroom.room_id=? AND courseroom.semester_id=?", Room.getId(), Semester.getId());
//            String srb = "";
//            if (courseroomsB.size() != 0) {
//                for (Courseroom i : courseroomsB) {
//                    srb = srb + "" + i.getCourseId() + ",";
//                }
//                courseAbleB = "\"courseAbleB\": [" + srb.substring(0, srb.length() - 1) + "]";
//            } else {
//                courseAbleB = "\"courseAbleB\": []";
//            }
//        } else {
//            semesterName = "\"semesterName\": \"\"";
//            courseAbleA = "\"courseAbleA\": []";
//            courseAbleB = "\"courseAbleB\": []";
//        }
//        List<Course> coursesA = Course.dao.find("SELECT * FROM Course WHERE state = 1 AND type= 1");
//        String sa = "";
//        if (coursesA.size() != 0) {
//            for (Course i : coursesA) {
//                sa = sa + "{\"id\": \"" + i.getId() + "\",\"name\": \"" + i.getName() + "\"},";
//            }
//            courseA = "\"courseA\": [" + sa.substring(0, sa.length() - 1) + "]";
//        } else {
//            courseA = "\"courseA\": []";
//        }
//        List<Course> coursesB = Course.dao.find("SELECT * FROM Course WHERE state = 1 AND type= 2");
//        String sb = "";
//        if (coursesB.size() != 0) {
//            for (Course i : coursesB) {
//                sb = sb + "{\"id\": \"" + i.getId() + "\",\"name\": \"" + i.getName() + "\"},";
//            }
//            courseB = "\"courseB\": [" + sb.substring(0, sb.length() - 1) + "]";
//        } else {
//            courseB = "\"courseB\": []";
//        }
//        renderJson("{" + roomName + ","+ roomState + "," + semesterName + "," + courseA + "," + courseB + "," + courseAbleA + "," + courseAbleB + "}");
//    }
//
//}
