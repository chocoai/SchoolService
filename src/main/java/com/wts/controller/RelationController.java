package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.model.*;
import com.wts.interceptor.AjaxParent;

import static com.wts.util.IDNumber.*;
import static com.wts.util.Util.getString;

public class RelationController extends Controller {
    private String getSQL(String sql) {
        return "FROM (((relation " +
                "LEFT JOIN Parent ON relation.parent_id = Parent.id) " +
                "LEFT JOIN Student ON relation.student_id = Student.id) " +
                "LEFT JOIN identity ON relation.identity_id = identity.id) " +
                "WHERE Student.`name` LIKE '%" + sql +
                "%' OR Student.`number` LIKE '%" + sql +
                "%' OR Student.`code` LIKE '%" + sql +
                "%' OR Student.`address` LIKE '%" + sql +
                "%' AND Parent.id=" + ((Parent) getSessionAttr("Parent")).getId() +
                "ORDER BY Student.`name` ASC";
    }
    /**
     * 列表
     */
    @Before(AjaxParent.class)
    public void list() {
        renderJson(Identity.dao.find("select * from identity order by id asc"));
    }
    /**
     * 查询
     */
    @Before(AjaxParent.class)
    public void query() {
        renderJson(Student.dao.paginate(getParaToInt("pageCurrent"), getParaToInt("pageSize"), "SELECT *", getSQL(getPara("queryString"))).getList());
    }
    /**
     * 计数
     */
    @Before(AjaxParent.class)
    public void total() {
        Long count = Db.queryLong("SELECT COUNT(*) " + getSQL(getPara("queryString")));
        if (count % getParaToInt("pageSize") == 0) {
            renderText((count / getParaToInt("pageSize")) + "");
        } else {
            renderText((count / getParaToInt("pageSize") + 1) + "");
        }
    }
    /**
     * 保存
     */
    @Before({Tx.class,AjaxParent.class})
    public void save() {
        if (!getString(getPara("name")).matches("^[\\u4e00-\\u9fa5]{2,}$")) {
            renderText("学生姓名为两个以上汉字!");
        } else if (!checkIDNumberDetailB(getPara("number"))){
            renderText(checkIDNumberDetail(getPara("number")));
        } else if (getPara("identity_id")==null) {
            renderText("未指定您的身份!");
        } else if (Student.dao.find("SELECT * FROM Student WHERE number=? AND code=? AND name=?", getPara("number"), getPara("code"), getPara("name")).size()==0) {
            renderText("请核实相关信息再关联学生!");
        } else {
            Student student = Student.dao.findFirst("SELECT * FROM Student WHERE number=? AND code=? AND name=?", getPara("number"), getPara("code"), getPara("name"));
            if (Relation.dao.find("SELECT * FROM relation WHERE parent_id=? AND student_id=? AND identity_id=?",
                    ((Parent) getSessionAttr("Parent")).getId(),student.getId(),getPara("identity_id")).size()!=0){
                renderText("不能重复绑定!");
            }else{
                Relation relation = new Relation();
                relation.set("parent_id",((Parent) getSessionAttr("Parent")).getId())
                        .set("student_id",student.getId())
                        .set("identity_id",getPara("identity_id"))
                        .save();
                Parent.dao.findById(((Parent) getSessionAttr("Parent")).getId()).set("bind",1).update();
                student.set("bind",1).update();
                renderText("OK");
            }
        }
    }
    @Before({Tx.class,AjaxParent.class})
    public void delete(){
        Relation relation = Relation.dao.findById(getPara("id"));
        if (relation == null) {
            renderText("要删除的记录不存在!");
        } else if(relation.getParentId().equals(((Parent) getSessionAttr("Parent")).getId())){
            renderText("要删除的记录与当前家长不一致!");
        } else {
            int parentId = relation.getParentId();
            int studentId = relation.getStudentId();
            Relation.dao.deleteById(getPara("id"));
            if (Relation.dao.find("SELECT * FROM relation WHERE parent_id=?",parentId).size()==0){
                Parent.dao.findById(parentId).set("bind",0).update();
            }
            if (Relation.dao.find("SELECT * FROM relation WHERE student_id=?",studentId).size()==0){
                Student.dao.findById(studentId).set("bind",0).update();
            }
            renderText("OK");
        }
    }
}
