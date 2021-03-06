//package com.wts.controller;
//
//import com.foxinmy.weixin4j.exception.WeixinException;
//import com.foxinmy.weixin4j.qy.model.User;
//import com.jfinal.aop.Before;
//import com.jfinal.core.Controller;
//import com.jfinal.plugin.activerecord.Db;
//import com.jfinal.plugin.activerecord.tx.Tx;
//import com.wts.entity.WP;
//import com.wts.entity.model.*;
//import com.wts.interceptor.*;
//
//import java.util.List;
//
//import static com.wts.util.IDNumber.*;
//import static com.wts.util.Util.getString;
//
//public class StudentController extends Controller {
//    private String getSQL(String sql) {
//        return "FROM Student WHERE `name` LIKE '%" + sql +
//                "%' OR `number` LIKE '%" + sql +
//                "%' OR `code` LIKE '%" + sql +
//                "%' OR `address` LIKE '%" + sql +
//                "%' ORDER BY id DESC";
//    }
//    private String getSQLByRoom(String sql,String roomId) {
//        return "FROM ((roomstudent " +
//                "LEFT JOIN Room ON roomstudent.room_id = Room.id) " +
//                "LEFT JOIN Student ON roomstudent.student_id = Student.id) " +
//                "WHERE Student.`name` LIKE '%" + sql +
//                "%' OR Student.`number` LIKE '%" + sql +
//                "%' OR Student.`code` LIKE '%" + sql +
//                "%' OR Student.`address` LIKE '%" + sql +
//                "%' AND Student.`state`=1 AND Room.`state`=1 AND Room.id=" + roomId +
//                "ORDER BY Student.`name` ASC";
//    }
//    /**
//     * 登录移动_管理_学生
//     */
//    public void Mobile_Manager_Student() throws WeixinException {
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
//                        render("/static/html/mobile/manager/Mobile_Manager_Student.html");
//                    } else {
//                        redirect("/");
//                    }
//                } else {
//                    redirect("/");
//                }
//            } else {
//                Teacher manager = Teacher.dao.findById(getCookie("dim"));
//                setSessionAttr("manager", manager);
//                render("/static/html/mobile/manager/Mobile_Manager_Student.html");
//            }
//        } else {
//            render("/static/html/mobile/manager/Mobile_Manager_Student.html");
//        }
//    }
//    /**
//     * 登录移动_教师_学生
//     */
//    public void Mobile_Teacher_Student() throws WeixinException {
//        if (getSessionAttr("Teacher") == null) {
//            if (getCookie("dit") == null || getCookie("dit").equals("")) {
//                if (!(getPara("code") == null || getPara("code").equals(""))) {
//                    User user = WP.me.getUserByCode(getPara("code"));
//                    Teacher Teacher = Teacher.dao.findFirst("SELECT * FROM Teacher WHERE userId = ? AND state = ?", user.getUserId(), 1);
//                    if (Teacher != null) {
//                        setSessionAttr("Teacher", Teacher);
//                        setCookie("dit", Teacher.getId().toString(), 60 * 30);
//                        if (user.getAvatar().equals(Teacher.getPicUrl())) {
//                            Teacher.set("picUrl", user.getAvatar()).update();
//                        }
//                        render("/static/html/mobile/manager/Mobile_Teacher_Student.html");
//                    } else {
//                        redirect("/");
//                    }
//                } else {
//                    redirect("/");
//                }
//            } else {
//                Teacher Teacher = Teacher.dao.findById(getCookie("dit"));
//                setSessionAttr("Teacher", Teacher);
//                render("/static/html/mobile/manager/Mobile_Teacher_Student.html");
//            }
//        } else {
//            render("/static/html/mobile/manager/Mobile_Teacher_Student.html");
//        }
//    }
//
//    /**
//     * 查询
//     */
//    @Before(AjaxManager.class)
//    public void query() {
//        renderJson(Student.dao.paginate(getParaToInt("pageCurrent"), getParaToInt("pageSize"), "SELECT *", getSQL(getPara("queryString"))).getList());
//    }
//    /**
//     * 查询
//     */
//    @Before(AjaxManager.class)
//    public void queryByRoom() {
//        renderJson(Student.dao.paginate(getParaToInt("pageCurrent"), getParaToInt("pageSize"),
//                "SELECT Student.*,Room.name AS rname",
//                getSQLByRoom(getPara("queryString"),getPara("roomId"))).getList());
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
//    /**
//     * 计数
//     */
//    @Before(AjaxManager.class)
//    public void totalByRoom() {
//        Long count = Db.queryLong("SELECT COUNT(*) " + getSQLByRoom(getPara("queryString"),getPara("roomId")));
//        if (count % getParaToInt("pageSize") == 0) {
//            renderText((count / getParaToInt("pageSize")) + "");
//        } else {
//            renderText((count / getParaToInt("pageSize") + 1) + "");
//        }
//    }
//
//
//
//
//
//    @Before(AjaxManager.class)
//    public void inactive() {
//        Student Student = Student.dao.findById(getPara("id"));
//        if (Student == null) {
//            renderText("未找到指定id的学生");
//        } else if (Student.get("state").toString().equals("2")) {
//            renderText("该学生已处于注销状态!");
//        } else {
//            Student.set("state",2).update();
//            renderText("OK");
//        }
//    }
//    @Before(AjaxManager.class)
//    public void active() {
//        Student Student = Student.dao.findById(getPara("id"));
//        if (Student == null) {
//            renderText("未找到指定id的学生");
//        } else if (Student.get("state").toString().equals("1")) {
//            renderText("该学生已处于激活状态!");
//        } else {
//            Student.set("state",1).update();
//            renderText("OK");
//        }
//    }
//
//    /**
//     * 新增时检测学生证件号码
//     * */
//    @Before(AjaxManager.class)
//    public void checkNumberForAdd() {
//        if (!checkIDNumberDetailB(getPara("number"))){
//            renderText(checkIDNumberDetail(getPara("number")));
//        } else if (Student.dao.find("select * from Student where number=?", getPara("number")).size()!=0) {
//            renderText("该证件号码已存在!");
//        } else {
//            renderText("OK");
//        }
//    }
//    /**
//     * 修改时检测学生证件号码
//     * */
//    @Before(AjaxManager.class)
//    public void checkNumberForEdit() {
//        if (!checkIDNumberDetailB(getPara("number"))){
//            renderText(checkIDNumberDetail(getPara("number")));
//        } else if (!Student.dao.findById(getPara("id")).get("number").equals(getPara("number"))
//                && Student.dao.find("select * from Student where number=?", getPara("number")).size()>=1) {
//            renderText("该证件号码已存在!");
//        } else {
//            renderText("OK");
//        }
//    }
//    /**
//     * 新增时检测学生学籍号
//     * */
//    @Before(AjaxManager.class)
//    public void checkCodeForAdd() {
//        renderText("OK");
//    }
//    /**
//     * 修改时检测学生学籍号
//     * */
//    @Before(AjaxManager.class)
//    public void checkCodeForEdit() {
//        renderText("OK");
//    }
//
//    @Before({Login.class, OverdueCheck.class})
//    public void get() {
//
//        String rr = "";
//        for (int i=1;i<7;i++){
//            String r = "";
//            Relation relation = Relation.dao.findFirst("SELECT * FROM relation WHERE student_id=? AND identity_id=?", getPara("id"),i);
//            if (relation != null) {
//                Parent Parent = Parent.dao.findFirst("SELECT * FROM Parent WHERE id=?", relation.getParentId());
//                r = "{\"parentName\":\"" + Parent.getName() + "\",\"parentUserId\":\"" + Parent.getUserId() + "\",\"parentMobile\":\"" + Parent.getMobile() + "\",\"parentAble\":true}";
//            } else {
//                r = "{\"parentName\":\"\",\"parentUserId\":\"\",\"parentMobile\":\"\",\"parentAble\":false}";
//            }
//            rr=rr+r+",";
//        }
//        String parentString = "\"Parent\": [" + rr.substring(0, rr.length() - 1) + "]";
//
//
//        List<Room> rooms = Room.dao.find("SELECT * FROM Room WHERE state = 1 ORDER BY id DESC");
//        String roomString;
//        if (rooms.size() != 0) {
//            String ro = "";
//            for (Room i : rooms) {
//                ro = ro + "{\"id\": \""+i.getId()+"\", \"name\": \""+i.getName()+"\", \"slogan\": \""+i+"\", \"state\": \""+i.getState()+"\"}"+ ",";
//            }
//            roomString = "\"rooms\": [" + ro.substring(0, ro.length() - 1) + "]";
//        } else {
//            roomString = "\"rooms\": []";
//        }
//        Student Student = Student.dao.findById(getPara("id"));
//        String studentString;
//        Roomstudent roomstudent = Roomstudent.dao.findFirst("SELECT * FROM roomstudent WHERE student_id=?",getPara("id"));
//        if (roomstudent!=null) {
//            Room Room = Room.dao.findById(roomstudent.getRoomId());
//            studentString = "\"Student\": [{\"id\":\"" + Student.getId() + "\",\"name\": \"" + Student.getName() + "\",\"number\": \"" + Student.getNumber() + "\",\"code\": \"" + Student.getCode() + "\",\"address\": \"" + Student.getAddress() + "\",\"state\": \"" + Student.getState() + "\",\"roomName\": \"" + Room.getName() + "\"}]";
//        }else{
//            studentString = "\"Student\": [{\"id\":\"" + Student.getId() + "\",\"name\": \"" + Student.getName() + "\",\"number\": \"" + Student.getNumber() + "\",\"code\": \"" + Student.getCode() + "\",\"address\": \"" + Student.getAddress() + "\",\"state\": \"" + Student.getState() + "\",\"roomName\": \"请选择班级\"}]";
//        }
//        renderJson("{"+parentString+","+roomString+","+studentString+"}");
////
////
////
////
////
////
////
////        Relation relation1 = Relation.dao.findFirst("select * from relation where student_id=? and identity_id=1", getPara("id"));
////        Relation relation2 = Relation.dao.findFirst("select * from relation where student_id=? and identity_id=2", getPara("id"));
////        Relation relation3 = Relation.dao.findFirst("select * from relation where student_id=? and identity_id=3", getPara("id"));
////        Relation relation4 = Relation.dao.findFirst("select * from relation where student_id=? and identity_id=4", getPara("id"));
////        Relation relation5 = Relation.dao.findFirst("select * from relation where student_id=? and identity_id=5", getPara("id"));
////        Relation relation6 = Relation.dao.findFirst("select * from relation where student_id=? and identity_id=6", getPara("id"));
////        String r1 = "";
////        String r2 = "";
////        String r3 = "";
////        String r4 = "";
////        String r5 = "";
////        String r6 = "";
////        if (relation1 != null) {
////            Parent parent1 = Parent.dao.findFirst("select * from Parent where id=?", relation1.getParentId());
////            r1 = "'parentName1':'" + parent1.getName() + "','parentUserId1':'" + parent1.getUserId() + "','parentMobile1':'" + parent1.getMobile() + "','parentAble1':true";
////        } else {
////            r1 = "'parentName1':'','parentUserId1':'','parentMobile1':'','parentAble1':false";
////        }
////        if (relation2 != null) {
////            Parent parent2 = Parent.dao.findFirst("select * from Parent where id=?", relation2.getParentId());
////            r2 = "'parentName2':'" + parent2.getName() + "','parentUserId2':'" + parent2.getUserId() + "','parentMobile2':'" + parent2.getMobile() + "','parentAble2':true";
////        } else {
////            r2 = "'parentName2':'','parentUserId2':'','parentMobile2':'','parentAble2':false";
////        }
////        if (relation3 != null) {
////            Parent parent3 = Parent.dao.findFirst("select * from Parent where id=?", relation3.getParentId());
////            r3 = "'parentName3':'" + parent3.getName() + "','parentUserId3':'" + parent3.getUserId() + "','parentMobile3':'" + parent3.getMobile() + "','parentAble3':true";
////        } else {
////            r3 = "'parentName3':'','parentUserId3':'','parentMobile3':'','parentAble3':false";
////        }
////        if (relation4 != null) {
////            Parent parent4 = Parent.dao.findFirst("select * from Parent where id=?", relation4.getParentId());
////            r4 = "'parentName4':'" + parent4.getName() + "','parentUserId4':'" + parent4.getUserId() + "','parentMobile4':'" + parent4.getMobile() + "','parentAble4':true";
////        } else {
////            r4 = "'parentName4':'','parentUserId4':'','parentMobile4':'','parentAble4':false";
////        }
////        if (relation5 != null) {
////            Parent parent5 = Parent.dao.findFirst("select * from Parent where id=?", relation5.getParentId());
////            r5 = "'parentName5':'" + parent5.getName() + "','parentUserId5':'" + parent5.getUserId() + "','parentMobile5':'" + parent5.getMobile() + "','parentAble5':true";
////        } else {
////            r5 = "'parentName5':'','parentUserId5':'','parentMobile5':'','parentAble5':false";
////        }
////        if (relation6 != null) {
////            Parent parent6 = Parent.dao.findFirst("select * from Parent where id=?", relation6.getParentId());
////            r6 = "'parentName6':'" + parent6.getName() + "','parentUserId6':'" + parent6.getUserId() + "','parentMobile6':'" + parent6.getMobile() + "','parentAble6':true";
////        } else {
////            r6 = "'parentName6':'','parentUserId6':'','parentMobile6':'','parentAble6':false";
////        }
////        renderText("{" + r1 + "," + r2 + "," + r3 + "," + r4 + "," + r5 + "," + r6 + "}");
//    }
//
//    @Before({Tx.class,AjaxManager.class})
//    public void save()  {
//        if (!getString(getPara("name")).matches("^[\\u4e00-\\u9fa5]{2,}$")) {
//            renderText("学生姓名为两个以上汉字!");
//        } else if (!checkIDNumberDetailB(getPara("number"))){
//            renderText(checkIDNumberDetail(getPara("number")));
//        } else if (Student.dao.find("select * from Student where number=?", getPara("number")).size()!=0) {
//            renderText("该证件号码已存在!");
//        } else if (Student.dao.find("select * from Student where code=?", getPara("code")).size()!=0) {
//            renderText("该学籍号码已存在!");
//        } else if (getPara("room_id")==null) {
//            renderText("未指定学生所属班级!");
//        } else {
//            Student Student = new Student();
//            Student.set("name",getPara("name"))
//                    .set("number",getPara("number"))
//                    .set("code",getPara("code"))
//                    .set("address", getPara("address"))
//                    .set("sex",getSex(getPara("number")))
//                    .set("birth",getBirthDate(getPara("number")))
//                    .set("state",1)
//                    .save();
//            Roomstudent roomstudent = new Roomstudent();
//            roomstudent.set("room_id",getPara("room_id"))
//                    .set("student_id",Student.getId())
//                    .save();
//            renderText("OK");
//        }
//    }
//
//    @Before({Tx.class, AjaxManager.class})
//    public void edit() {
//        Student Student = Student.dao.findById(getPara("id"));
//        if (Student == null) {
//            renderText("要修改的学生不存在!");
//        } else {
//            Roomstudent roomstudent = Roomstudent.dao.findFirst("SELECT * FROM roomstudent WHERE student_id=?",Student.getId());
//            if (getString(Student.getStr("name")).equals(getPara("name").trim())
//                    && getString(Student.getStr("number")).equals(getPara("number").trim())
//                    && getString(Student.getStr("code")).equals(getPara("code").trim())
//                    && (getString(Student.getStr("address")).equals(getPara("address").trim())
//                    || ((Student.getStr("address"))==null && getPara("address")==null))
//                    && roomstudent.get("room_id").equals(getString(getPara("room_id")).trim())
//                    ) {
//                renderText("未找到修改内容!");
//            } else if (!getString(getPara("name")).matches("^[\\u4e00-\\u9fa5]{2,}$")) {
//                renderText("学生姓名为两个以上汉字!");
//            } else if (!checkIDNumberDetailB(getPara("number"))) {
//                renderText(checkIDNumberDetail(getPara("number")));
//            } else if (!Student.getStr("number").equals(getPara("number"))
//                    && Student.dao.find("select * from Student where number=?", getPara("number")).size() != 0) {
//                renderText("该证件号码已存在!");
//            } else {
//                Student.set("name", getPara("name"))
//                        .set("number", getPara("number"))
//                        .set("code", getPara("code"))
//                        .set("address", getPara("address"))
//                        .set("sex", getSex(getPara("number")))
//                        .set("birth", getBirthDate(getPara("number")))
//                        .set("state", 1)
//                        .update();
//                roomstudent.set("room_id",getPara("room_id")).update();
//                renderText("OK");
//            }
//        }
//    }
//
//}