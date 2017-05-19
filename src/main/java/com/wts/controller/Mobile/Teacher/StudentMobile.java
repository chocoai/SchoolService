package com.wts.controller.Mobile.Teacher;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.wts.entity.model.Student;
import com.wts.entity.model.Teacher;
import com.wts.interceptor.OverdueCheck;
import com.wts.interceptor.PageCheck;
import com.wts.interceptor.PermissionCheck;
import com.wts.validator.Query;
import com.wts.validator.Student.Student_Edit;
import com.wts.validator.Student.Student_Exist;
import com.wts.validator.Student.Student_Save;
import com.wts.validator.Total;
import org.apache.log4j.Logger;

import static com.wts.util.IDNumber.*;
import static com.wts.util.Util.PermissionString;


public class StudentMobile extends Controller {
  private static Logger logger = Logger.getLogger(StudentMobile.class);

  /**
   * 页面
   */
  @Before({OverdueCheck.class, PageCheck.class})
  public void Page() {
    // 未登录
    if (getSessionAttr("Teacher") == null) {
      // cookie过期
      if (getCookie("dit").equals("")) {
        redirect("/desktop");
      } else {
        Teacher teacher = Teacher.dao.findById(getCookie("dit"));
        setSessionAttr("Teacher", teacher);
        setCookie("dit", teacher.getId().toString(), 60 * 60 * 3);
        setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(),teacher.getId().toString()), 60 * 6 * 10);
        render("/static/html/mobile/Teacher/Mobile_Teacher_Student.html");
      }
    } else {
      String teacherId = ((Teacher) getSessionAttr("Teacher")).getId().toString();
      setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(),teacherId), 60 * 6 * 10);
      render("/static/html/mobile/Teacher/Mobile_Teacher_Student.html");
    }
  }

  /**
   * 权限
   * */
  public void Permission() {
    String teacherId = ((Teacher) getSessionAttr("Teacher")).getId().toString();
    setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(),teacherId), 60 * 6 * 10);
    setCookie("name",((Teacher) getSessionAttr("Teacher")).getName(),60 * 6 * 10);
  }

  /**
   * 查找
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Query.class})
  public void Query() {
    renderJson(Db.paginate(
            getParaToInt("pageCurrent"),
            getParaToInt("pageSize"),
            "SELECT *",
            "FROM student WHERE del = 0 AND (name LIKE '%" + getPara("keyword") + "%' OR number LIKE '%" + getPara("keyword") + "%' OR code LIKE '%" + getPara("keyword") + "%') ORDER BY id DESC").getList());
  }

  /**
   * 计数
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Total.class})
  public void Total() {
    Long count = Db.queryLong("SELECT COUNT(*) FROM student WHERE del = 0 AND (name LIKE '%" + getPara("keyword") + "%' OR number LIKE '%" + getPara("keyword") + "%' OR code LIKE '%" + getPara("keyword") + "%')");
    if (count % getParaToInt("pageSize") == 0) {
      renderText((count / getParaToInt("pageSize")) + "");
    } else {
      renderText((count / getParaToInt("pageSize") + 1) + "");
    }
  }

  /**
   * 读取
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Student_Exist.class})
  public void Get() {
    renderJson(Student.dao.findById(getPara("id")));
  }

  /**
   * 激活
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Student_Exist.class})
  public void Active() {
    Student object = Student.dao.findById(getPara("id"));
    if (object.get("state").toString().equals("1")) {
      renderText("该学生已激活!");
    } else {
      object.set("state", 1).update();
      logger.warn("function:"+this.getClass().getSimpleName()+"/Active;"+"teacher_id:"+((Teacher) getSessionAttr("Teacher")).getId().toString()+";student_id:"+getPara("id")+";");
      renderText("OK");
    }
  }

  /**
   * 注销
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Student_Exist.class})
  public void Inactive() {
    Student object = Student.dao.findById(getPara("id"));
    if (object.get("state").toString().equals("0")) {
      renderText("该学生已注销!");
    } else {
      object.set("state", 0).update();
      logger.warn("function:"+this.getClass().getSimpleName()+"/Inactive;"+"teacher_id:"+((Teacher) getSessionAttr("Teacher")).getId().toString()+";student_id:"+getPara("id")+";");
      renderText("OK");
    }
  }

  /**
   * 删除
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Student_Exist.class})
  public void Delete() {
    Student object = Student.dao.findById(getPara("id"));
    if (object.get("del").toString().equals("1")) {
      renderText("该学生已删除!");
    } else {
      object.set("del", 1).update();
      logger.warn("function:"+this.getClass().getSimpleName()+"/Delete;"+"teacher_id:"+((Teacher) getSessionAttr("Teacher")).getId().toString()+";student_id:"+getPara("id")+";");
      renderText("OK");
    }
  }

  /**
   * 保存
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Student_Save.class})
  public void Save() {
    Student object = new Student();
    object.set("name", getPara("name"))
            .set("number", getPara("number"))
            .set("code", getPara("code"))
            .set("sex", getSex(getPara("number")))
            .set("birth", getBirthDate(getPara("number")))
            .set("address", getPara("address"))
            .set("state", getPara("state"))
            .set("bind", 0)
            .set("del", 0)
            .save();
    logger.warn("function:"+this.getClass().getSimpleName()+"/Save;"+"teacher_id:"+((Teacher) getSessionAttr("Teacher")).getId().toString()+";student_id:"+object.get("id")+";");
    renderText("OK");
  }

  /**
   * 修改
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Student_Exist.class, Student_Edit.class})
  public void Edit() {
    Student object = Student.dao.findById(getPara("id"));
    object.set("name", getPara("name"))
            .set("number", getPara("number"))
            .set("code", getPara("code"))
            .set("sex", getSex(getPara("number")))
            .set("birth", getBirthDate(getPara("number")))
            .set("address", getPara("type"))
            .set("bind", 0)
            .update();
    logger.warn("function:"+this.getClass().getSimpleName()+"/Edit;"+"teacher_id:"+((Teacher) getSessionAttr("Teacher")).getId().toString()+";student_id:"+getPara("id")+";");
    renderText("OK");
  }


  /**
   * 检测身份证号码_新增
   */
  @Before(OverdueCheck.class)
  public void checkNumberForAdd() {
    if (!checkIDNumberDetailB(getPara("number"))){
      renderText(checkIDNumberDetail(getPara("number")));
    } else if (Db.find("SELECT * FROM student WHERE number = ?", getPara("number")).size() != 0) {
      renderText("该学生身份证号码已存在!");
    } else {
      renderText("OK");
    }
  }

  /**
   * 检测身份证号码_修改
   */
  @Before(OverdueCheck.class)
  public void checkNumberForEdit() {
    if (!checkIDNumberDetailB(getPara("number"))){
      renderText(checkIDNumberDetail(getPara("number")));
    } else if (!Student.dao.findById(getPara("id")).get("number").equals(getPara("number"))
            && Db.find("SELECT * FROM student WHERE number = ?", getPara("number")).size() != 0) {
      renderText("该学生身份证号码已存在!");
    } else {
      renderText("OK");
    }
  }
  /**
   * 检测学籍号码_新增
   */
  @Before(OverdueCheck.class)
  public void checkCodeForAdd() {
    if (Db.find("SELECT * FROM student WHERE code = ?", getPara("code")).size() != 0) {
      renderText("该学生学籍号码已存在!");
    } else {
      renderText("OK");
    }
  }

  /**
   * 检测学籍号码_修改
   */
  @Before(OverdueCheck.class)
  public void checkCodeForEdit() {
    if (!Student.dao.findById(getPara("id")).get("code").equals(getPara("code"))
            && Db.find("SELECT * FROM student WHERE code = ?", getPara("code")).size() != 0) {
      renderText("该学生学籍号码已存在!");
    } else {
      renderText("OK");
    }
  }

}
