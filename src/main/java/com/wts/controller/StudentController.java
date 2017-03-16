package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.WP;
import com.wts.entity.model.*;
import com.wts.interceptor.*;

import java.util.List;

import static com.wts.util.IDNumber.*;
import static com.wts.util.Util.getString;

public class StudentController extends Controller {
    private String getSQL(String sql) {
        return "FROM student WHERE `name` LIKE '%" + sql +
                "%' OR `number` LIKE '%" + sql +
                "%' OR `code` LIKE '%" + sql +
                "%' OR `address` LIKE '%" + sql +
                "%' ORDER BY id DESC";
    }
    /**
     * 登录移动_管理_学生
     */
    public void Mobile_Manager_Student() throws WeixinException {
        if (getSessionAttr("manager") == null || ((Teacher) getSessionAttr("manager")).getIsManager() != 1) {
            if (getCookie("dim") == null || getCookie("dim").equals("")) {
                if (!(getPara("code") == null || getPara("code").equals(""))) {
                    User user = WP.me.getUserByCode(getPara("code"));
                    Teacher manager = Teacher.dao.findFirst("SELECT * FROM teacher WHERE userId = ? AND state = ? AND isManager = 1", user.getUserId(), 1);
                    if (manager != null) {
                        setSessionAttr("manager", manager);
                        setCookie("dim", manager.getId().toString(), 60 * 30);
                        if (user.getAvatar().equals(manager.getPicUrl())) {
                            manager.set("picUrl", user.getAvatar()).update();
                        }
                        render("/static/html/mobile/manager/Mobile_Manager_Student.html");
                    } else {
                        redirect("/");
                    }
                } else {
                    redirect("/");
                }
            } else {
                Teacher manager = Teacher.dao.findById(getCookie("dim"));
                setSessionAttr("manager", manager);
                render("/static/html/mobile/manager/Mobile_Manager_Student.html");
            }
        } else {
            render("/static/html/mobile/manager/Mobile_Manager_Student.html");
        }
    }

    /**
     * 查询
     */
    @Before(AjaxManager.class)
    public void query() {
        renderJson(Student.dao.paginate(getParaToInt("pageCurrent"), getParaToInt("pageSize"), "SELECT *", getSQL(getPara("queryString"))).getList());

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
//    @Before({Ajax.class,Login.class})
//    public  void studentListByRoom() {
//        List<Student> students = Student.dao.find("select * from student where state=1 and room_id=?",getPara("id"));
//        renderJson(students);
//    }
//    @Before({Ajax.class,Login.class})
//    public  void studentCodeByRoom() {
//        List<Student> students = Student.dao.find("select id from student where state=1 and room_id=?",getPara("id"));
//        String st = "";
//        if (students.size() > 0) {
//            for (Student s : students) {
//                st = st + s.getId() + ",";
//            }
//            renderJson("[" + st.substring(0, st.length() - 1) + "]");
//        } else {
//            renderJson("[]");
//        }
//    }
//    @Before({Ajax.class,Login.class})
//    public  void studentListByTeam() {
//        List<Student> students = Student.dao.find("select * from student where state=1 and team_id=?",getPara("id"));
//        renderJson(students);
//    }
//    @Before({Ajax.class,Login.class})
//    public  void studentCodeByTeam() {
//        List<Student> students = Student.dao.find("select id from student where state=1 and team_id=?",getPara("id"));
//        String st = "";
//        if (students.size() > 0) {
//            for (Student s : students) {
//                st = st + s.getId() + ",";
//            }
//            renderJson("[" + st.substring(0, st.length() - 1) + "]");
//        } else {
//            renderJson("[]");
//        }
//    }
//
//    @Before({Login.class, Ajax.class})
//    public void getById() {
//        Student student = Student.dao.findById(getPara("id"));
//        renderJson(student);
//    }
//    @Before(AjaxManager.class)
//    public void getNameById() {
//        Student student = Student.dao.findById(getPara("id"));
//        renderText(student.get("name").toString());
//    }
    @Before(AjaxManager.class)
    public void inactive() {
        Student student = Student.dao.findById(getPara("id"));
        if (student == null) {
            renderText("未找到指定id的学生");
        } else if (student.get("state").toString().equals("2")) {
            renderText("该学生已处于注销状态!");
        } else {
            student.set("state",2).update();
            renderText("OK");
        }
    }
    @Before(AjaxManager.class)
    public void active() {
        Student student = Student.dao.findById(getPara("id"));
        if (student == null) {
            renderText("未找到指定id的学生");
        } else if (student.get("state").toString().equals("1")) {
            renderText("该学生已处于激活状态!");
        } else {
            student.set("state",1).update();
            renderText("OK");
        }
    }
//    @Before(AjaxManager.class)
//    public void queryForManager() {
//        Page<Student> students= Student.dao.studentQuery(getParaToInt("pageCurrent"),getParaToInt("pageSize"),getPara("queryString"));
//        renderJson(students.getList());
//    }
//    @Before(AjaxManager.class)
//    public void totalForManager() {
//        Long count = Db.queryLong("select count(*) from student where (name like '%"+ getPara("queryString") +"%' or number LIKE '%"+getPara("queryString")+"%' or code LIKE '%"+getPara("queryString")+"%') ORDER BY name ASC");
//        if (count%getParaToInt("pageSize")==0) {
//            renderText((count/getParaToInt("pageSize"))+"");
//        } else {
//            renderText((count/getParaToInt("pageSize")+1)+"");
//        }
//    }
//    @Before(AjaxParent.class)
//    public void queryForParent() {
//        Page<Record> students = Db.paginate(getParaToInt("pageCurrent"),
//                getParaToInt("pageSize"),
//                "select student.*,identity.name as iname",
//                " from (((student" +
//                        " left join relation on relation.student_id = student.id)" +
//                        " left join enterprise on relation.parent_id = enterprise.id)" +
//                        " left join identity on relation.identity_id = identity.id)" +
//                        " where student.state = 1 and enterprise.id = "+((Enterprise) getSessionAttr("parent")).getId().toString()+
//                        " and (student.name LIKE '%"+getPara("queryString")+"%' or student.number LIKE '%"+getPara("queryString")+"%' or student.code LIKE '%"+getPara("queryString")+"%')"+
//                        " ORDER BY student.name ASC"
//        );
//        renderJson(students.getList());
//    }
//    @Before(AjaxParent.class)
//    public void totalForParent() {
//        Long count = Db.queryLong("select count(*) from (((student" +
//                " left join relation on relation.student_id = student.id)" +
//                " left join enterprise on relation.parent_id = enterprise.id)" +
//                " left join identity on relation.identity_id = identity.id)" +
//                " where student.state = 1 and enterprise.id = "+((Enterprise) getSessionAttr("parent")).getId().toString()+
//                " and (student.name LIKE '%"+getPara("queryString")+"%' or student.number LIKE '%"+getPara("queryString")+"%' or student.code LIKE '%"+getPara("queryString")+"%')"+
//                " ORDER BY student.name ASC");
//        if (count%getParaToInt("pageSize")==0) {
//            renderText((count/getParaToInt("pageSize"))+"");
//        } else {
//            renderText((count/getParaToInt("pageSize")+1)+"");
//        }
//    }
//    @Before(AjaxTeacher.class)
//    public void queryByNameRoomId() {
//        Page<Student> students= Student.dao.studentQueryByRoomId(getParaToInt("pageCurrent"),getParaToInt("pageSize"),getPara("queryString"),getPara("roomId"));
//        renderJson(students.getList());
//    }
//    @Before(AjaxTeacher.class)
//    public void totalByNameRoomId() {
//        Long count = Db.queryLong("select count(*) from student where state=1 and  room_id = "+getPara("roomId")+" and (name like '%"+ getPara("queryString") +"%' or number LIKE '%"+getPara("queryString")+"%' or code LIKE '%"+getPara("queryString")+"%') ORDER BY name ASC");
//        if (count%getParaToInt("pageSize")==0) {
//            renderText((count/getParaToInt("pageSize"))+"");
//        } else {
//            renderText((count/getParaToInt("pageSize")+1)+"");
//        }
//    }
//    @Before(AjaxTeacher.class)
//    public void queryByNameTeamId() {
//        Page<Student> students= Student.dao.studentQueryByTeamId(getParaToInt("pageCurrent"),getParaToInt("pageSize"),getPara("queryString"),getPara("teamId"));
//        renderJson(students.getList());
//    }
//    @Before(AjaxTeacher.class)
//    public void totalByNameTeamId() {
//        Long count = Db.queryLong("select count(*) from student where state=1 and team_id = "+getPara("teamId")+" and (name like '%"+ getPara("queryString") +"%' or number LIKE '%"+getPara("queryString")+"%' or code LIKE '%"+getPara("queryString")+"%') ORDER BY name ASC");
//        if (count%getParaToInt("pageSize")==0) {
//            renderText((count/getParaToInt("pageSize"))+"");
//        } else {
//            renderText((count/getParaToInt("pageSize")+1)+"");
//        }
//    }
    /**
     * 新增时检测学生证件号码
     * */
    @Before(AjaxManager.class)
    public void checkNumberForAdd() {
        if (!checkIDNumberDetailB(getPara("number"))){
            renderText(checkIDNumberDetail(getPara("number")));
        } else if (Student.dao.find("select * from student where number=?", getPara("number")).size()!=0) {
            renderText("该证件号码已存在!");
        } else {
            renderText("OK");
        }
    }
    /**
     * 修改时检测学生证件号码
     * */
    @Before(AjaxManager.class)
    public void checkNumberForEdit() {
        if (!checkIDNumberDetailB(getPara("number"))){
            renderText(checkIDNumberDetail(getPara("number")));
        } else if (!Student.dao.findById(getPara("id")).get("number").equals(getPara("number"))
                && Student.dao.find("select * from student where number=?", getPara("number")).size()>=1) {
            renderText("该证件号码已存在!");
        } else {
            renderText("OK");
        }
    }
    /**
     * 新增时检测学生学籍号
     * */
    @Before(AjaxManager.class)
    public void checkCodeForAdd() {
        renderText("OK");
    }
    /**
     * 修改时检测学生学籍号
     * */
    @Before(AjaxManager.class)
    public void checkCodeForEdit() {
        renderText("OK");
    }



    @Before({Login.class, Ajax.class})
    public void getParentById() {
        Relation relation1 = Relation.dao.findFirst("select * from relation where student_id=? and identity_id=1", getPara("id"));
        Relation relation2 = Relation.dao.findFirst("select * from relation where student_id=? and identity_id=2", getPara("id"));
        Relation relation3 = Relation.dao.findFirst("select * from relation where student_id=? and identity_id=3", getPara("id"));
        Relation relation4 = Relation.dao.findFirst("select * from relation where student_id=? and identity_id=4", getPara("id"));
        Relation relation5 = Relation.dao.findFirst("select * from relation where student_id=? and identity_id=5", getPara("id"));
        Relation relation6 = Relation.dao.findFirst("select * from relation where student_id=? and identity_id=6", getPara("id"));
        String r1 = "";
        String r2 = "";
        String r3 = "";
        String r4 = "";
        String r5 = "";
        String r6 = "";
        if (relation1 != null) {
            Parent parent1 = Parent.dao.findFirst("select * from parent where id=?", relation1.getParentId());
            r1 = "'parentName1':'" + parent1.getName() + "','parentUserId1':'" + parent1.getUserId() + "','parentMobile1':'" + parent1.getMobile() + "','parentAble1':true";
        } else {
            r1 = "'parentName1':'','parentUserId1':'','parentMobile1':'','parentAble1':false";
        }
        if (relation2 != null) {
            Parent parent2 = Parent.dao.findFirst("select * from parent where id=?", relation2.getParentId());
            r2 = "'parentName2':'" + parent2.getName() + "','parentUserId2':'" + parent2.getUserId() + "','parentMobile2':'" + parent2.getMobile() + "','parentAble2':true";
        } else {
            r2 = "'parentName2':'','parentUserId2':'','parentMobile2':'','parentAble2':false";
        }
        if (relation3 != null) {
            Parent parent3 = Parent.dao.findFirst("select * from parent where id=?", relation3.getParentId());
            r3 = "'parentName3':'" + parent3.getName() + "','parentUserId3':'" + parent3.getUserId() + "','parentMobile3':'" + parent3.getMobile() + "','parentAble3':true";
        } else {
            r3 = "'parentName3':'','parentUserId3':'','parentMobile3':'','parentAble3':false";
        }
        if (relation4 != null) {
            Parent parent4 = Parent.dao.findFirst("select * from parent where id=?", relation4.getParentId());
            r4 = "'parentName4':'" + parent4.getName() + "','parentUserId4':'" + parent4.getUserId() + "','parentMobile4':'" + parent4.getMobile() + "','parentAble4':true";
        } else {
            r4 = "'parentName4':'','parentUserId4':'','parentMobile4':'','parentAble4':false";
        }
        if (relation5 != null) {
            Parent parent5 = Parent.dao.findFirst("select * from parent where id=?", relation5.getParentId());
            r5 = "'parentName5':'" + parent5.getName() + "','parentUserId5':'" + parent5.getUserId() + "','parentMobile5':'" + parent5.getMobile() + "','parentAble5':true";
        } else {
            r5 = "'parentName5':'','parentUserId5':'','parentMobile5':'','parentAble5':false";
        }
        if (relation6 != null) {
            Parent parent6 = Parent.dao.findFirst("select * from parent where id=?", relation6.getParentId());
            r6 = "'parentName6':'" + parent6.getName() + "','parentUserId6':'" + parent6.getUserId() + "','parentMobile6':'" + parent6.getMobile() + "','parentAble6':true";
        } else {
            r6 = "'parentName6':'','parentUserId6':'','parentMobile6':'','parentAble6':false";
        }
        renderText("{" + r1 + "," + r2 + "," + r3 + "," + r4 + "," + r5 + "," + r6 + "}");
    }

    @Before({Tx.class,AjaxManager.class})
    public void save()  {
        if (!getString(getPara("name")).matches("^[\\u4e00-\\u9fa5]{2,}$")) {
            renderText("学生姓名为两个以上汉字!");
        } else if (!checkIDNumberDetailB(getPara("number"))){
            renderText(checkIDNumberDetail(getPara("number")));
        } else if (Student.dao.find("select * from student where number=?", getPara("number")).size()!=0) {
            renderText("该证件号码已存在!");
        } else if (Student.dao.find("select * from student where code=?", getPara("code")).size()!=0) {
            renderText("该学籍号码已存在!");
        } else if (getPara("room_id")==null) {
            renderText("未指定学生所属班级!");
        } else {
            Student student = new Student();
            student.set("name",getPara("name"))
                    .set("number",getPara("number"))
                    .set("code",getPara("code"))
                    .set("address", getPara("address"))
                    .set("sex",getSex(getPara("number")))
                    .set("birth",getBirthDate(getPara("number")))
                    .set("state",1)
                    .save();
            Roomstudent roomstudent = new Roomstudent();
            roomstudent.set("room_id",getPara("room_id"))
                    .set("student_id",student.getId())
                    .save();
            renderText("OK");
        }
    }

//    @Before({Tx.class, AjaxManager.class})
//    public void edit() {
//        Student student = Student.dao.findById(getPara("id"));
//        String room,team;
//        if (student == null) {
//            renderText("要修改的学生不存在!");
//        } else {
//            if (student.get("room_id")==null){
//                room="";
//            }else{
//                room=student.get("room_id").toString();
//            }
//            if (student.get("team_id")==null){
//                team="";
//            }else{
//                team=student.get("team_id").toString();
//            }
//            if (getString(student.getStr("name")).equals(getPara("name").trim())
//                    && getString(student.getStr("number")).equals(getPara("number").trim())
//                    && getString(student.getStr("code")).equals(getPara("code").trim())
//                    && (getString(student.getStr("address")).equals(getPara("address").trim())
//                    || ((student.getStr("address"))==null && getPara("address")==null))
//                    && room.equals(getString(getPara("room_id")).trim())
//                    && team.equals(getString(getPara("team_id")).trim())
//                    ) {
//                renderText("未找到修改内容!");
//            } else if (!getString(getPara("name")).matches("^[\\u4e00-\\u9fa5]{2,}$")) {
//                renderText("学生姓名为两个以上汉字!");
//            } else if (!checkIDNumberDetailB(getPara("number"))) {
//                renderText(checkIDNumberDetail(getPara("number")));
//            } else if (!student.getStr("number").equals(getPara("number"))
//                    && Student.dao.find("select * from student where number=?", getPara("number")).size() != 0) {
//                renderText("该证件号码已存在!");
//            } else {
//                if (!getString(getPara("room_id")).equals("")) {
//                    student.set("room_id", getPara("room_id"));
//                }
//                if (!getString(getPara("team_id")).equals("")) {
//                    student.set("team_id",getPara("team_id"));
//                }
//                student.set("name", getPara("name"))
//                        .set("number", getPara("number"))
//                        .set("code", getPara("code"))
//                        .set("address", getPara("address"))
//                        .set("sex", getSex(getPara("number")))
//                        .set("birth", getBirthDate(getPara("number")))
//                        .set("state", 1)
//                        .update();
//                renderText("OK");
//            }
//        }
//    }
//
//    @Before({Tx.class, AjaxParent.class})
//    public void editForParent() {
//        Student student = Student.dao.findById(getPara("id"));
//        if (student == null) {
//            renderText("要修改的学生不存在!");
//        } else {
//            if ((getString(student.getStr("address")).equals(getPara("address").trim())
//                    || ((student.getStr("address")) == null && getPara("address") == null))
//                    ) {
//                renderText("未找到修改内容!");
//            } else {
//                student.set("address", getPara("address"))
//                        .update();
//                renderText("OK");
//            }
//        }
//    }
//}
}