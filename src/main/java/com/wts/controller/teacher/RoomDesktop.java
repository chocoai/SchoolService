package com.wts.controller.teacher;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.wts.entity.model.Course;
import com.wts.entity.model.Room;
import com.wts.entity.model.Teacher;
import com.wts.interceptor.OverdueCheck;
import com.wts.interceptor.PageCheck;
import com.wts.interceptor.PermissionCheck;
import com.wts.util.ExportUtil;
import com.wts.validator.Query;
import com.wts.validator.course.Course_Edit;
import com.wts.validator.course.Course_Exist;
import com.wts.validator.course.Course_Save;
import com.wts.validator.room.Room_Edit;
import com.wts.validator.room.Room_Exist;
import com.wts.validator.room.Room_Save;

import java.io.IOException;
import java.util.List;

import static com.wts.util.Util.PermissionString;


public class RoomDesktop extends Controller {
  /**
   * 页面
   */
  @Before({OverdueCheck.class, PageCheck.class})
  public void Page() {
    // 未登录
    if (getSessionAttr("teacher") == null) {
      // cookie过期
      if (getCookie("dit").equals("")) {
        redirect("/desktop");
      } else {
        Teacher teacher = Teacher.dao.findById(getCookie("dit"));
        setSessionAttr("teacher", teacher);
        setCookie("dit", teacher.getId().toString(), 60 * 60 * 3);
        setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(),teacher.getId().toString()), 60 * 6 * 10);
        render("/static/html/desktop/teacher/Desktop_Teacher_Room.html");
      }
    } else {
      String teacherId = ((Teacher) getSessionAttr("teacher")).getId().toString();
      setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(),teacherId), 60 * 6 * 10);
      render("/static/html/desktop/teacher/Desktop_Teacher_Room.html");
    }
  }

  /**
   * 权限
   * */
  public void Permission() {
    String teacherId = ((Teacher) getSessionAttr("teacher")).getId().toString();
    setCookie(super.getClass().getSimpleName(), PermissionString(super.getClass().getSimpleName(),teacherId), 60 * 6 * 10);
    setCookie("name",((Teacher) getSessionAttr("teacher")).getName(),60 * 6 * 10);
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
            "FROM room WHERE del = 0 AND name LIKE '%" + getPara("keyword") + "%' ORDER BY id DESC").getList());
  }

  /**
   * 计数
   */
  @Before({OverdueCheck.class, PermissionCheck.class})
  public void Total() {
    Long count = Db.queryLong("SELECT COUNT(*) FROM room WHERE del = 0 AND name LIKE '%" + getPara("keyword") + "%'");
    renderText(count.toString());
  }

  /**
   * 读取
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Course_Exist.class})
  public void Get() {
    renderJson(Room.dao.findById(getPara("id")));
  }

  /**
   * 激活
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Course_Exist.class})
  public void Active() {
    Room object = Room.dao.findById(getPara("id"));
    if (object.get("state").toString().equals("1")) {
      renderText("该班级已激活!");
    } else {
      object.set("state", 1).update();
      renderText("OK");
    }
  }

  /**
   * 注销
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Room_Exist.class})
  public void Inactive() {
    Room object = Room.dao.findById(getPara("id"));
    if (object.get("state").toString().equals("0")) {
      renderText("该班级已注销!");
    } else {
      object.set("state", 0).update();
      renderText("OK");
    }
  }

  /**
   * 删除
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Room_Exist.class})
  public void Delete() {
    Room object = Room.dao.findById(getPara("id"));
    if (object.get("del").toString().equals("1")) {
      renderText("该班级已删除!");
    } else {
      object.set("del", 1).update();
      renderText("OK");
    }
  }
  /**
   * 保存
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Room_Save.class})
  public void Save() {
    Room object = new Room();
    object.set("name", getPara("year")+"级"+getPara("order")+"班")
            .set("year", getPara("year"))
            .set("order", getPara("order"))
            .set("state", getPara("state"))
            .set("del", 0)
            .save();
    renderText("OK");
  }

  /**
   * 修改
   */
  @Before({OverdueCheck.class, PermissionCheck.class, Room_Exist.class, Room_Edit.class})
  public void Edit() {
    Room object = Room.dao.findById(getPara("id"));
    object.set("name", getPara("year")+"级"+getPara("order")+"班")
            .set("year", getPara("year"))
            .set("order", getPara("order"))
            .set("state", getPara("state"))
            .update();
    renderText("OK");
  }

  /**
   * 导出
   */
  @Before({OverdueCheck.class, PermissionCheck.class})
  public void Download() throws IOException {
    String[] title={"序号","班级名称","入学年份","班序","班级状态"};
    String fileName = "Room";
    String SQL = "select id AS 序号,name AS 班级名称, `year` AS 入学年份, `order` AS 班序, " +
            "(case state when 1 then '可用' when 2 then '停用' else '错误' end ) AS 班级状态 " +
            "from room where del = 0 AND name like '%"+getPara("keyword")+"%'" +
            "ORDER BY id ASC";
    ExportUtil.export(title,fileName,SQL,getResponse());
  }

  /**
   * 检测名称_新增
   */
  @Before(OverdueCheck.class)
  public void checkNameForAdd() {
    if (Db.find("SELECT * FROM room WHERE name = ?", getPara("name")).size() != 0) {
      renderText("该班级名称已存在!");
    } else {
      renderText("OK");
    }
  }

  /**
   * 检测名称_修改
   */
  @Before(OverdueCheck.class)
  public void checkNameForEdit() {
    if (!Room.dao.findById(getPara("id")).get("name").equals(getPara("name"))
            && Db.find("SELECT * FROM room WHERE name = ?", getPara("name")).size() != 0) {
      renderText("该班级名称已存在!");
    } else {
      renderText("OK");
    }
  }
}
