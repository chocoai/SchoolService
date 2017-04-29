package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.WP;
import com.wts.entity.model.Course;
import com.wts.entity.model.Teacher;
import com.wts.interceptor.Ajax;
import com.wts.interceptor.AjaxManager;
import com.wts.interceptor.Login;
import com.wts.util.ExportUtil;
import java.io.IOException;
import java.util.List;

import static com.wts.util.Util.getString;

public class CourseController extends Controller {
    /**
     * 登录移动_管理_课程
     */
    public void Mobile_Manager_Course() throws WeixinException {
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
                        render("/static/html/mobile/manager/Mobile_Manager_Course.html");
                    } else {
                        redirect("/");
                    }
                } else {
                    redirect("/");
                }
            } else {
                Teacher manager = Teacher.dao.findById(getCookie("dim"));
                setSessionAttr("manager", manager);
                render("/static/html/mobile/manager/Mobile_Manager_Course.html");
            }
        } else {
            render("/static/html/mobile/manager/Mobile_Manager_Course.html");
        }
    }
    /**
     * 登录移动_教师_课程
     */
    public void Mobile_Teacher_Course() throws WeixinException {
        if (getSessionAttr("teacher") == null) {
            if (getCookie("dit") == null || getCookie("dim").equals("")) {
                if (!(getPara("code") == null || getPara("code").equals(""))) {
                    User user = WP.me.getUserByCode(getPara("code"));
                    Teacher teacher = Teacher.dao.findFirst("SELECT * FROM teacher WHERE userId = ? AND state = ? ", user.getUserId(), 1);
                    if (teacher != null) {
                        setSessionAttr("teacher", teacher);
                        setCookie("dit", teacher.getId().toString(), 60 * 30);
                        if (user.getAvatar().equals(teacher.getPicUrl())) {
                            teacher.set("picUrl", user.getAvatar()).update();
                        }
                        render("/static/html/mobile/teacher/Mobile_Teacher_Course.html");
                    } else {
                        redirect("/");
                    }
                } else {
                    redirect("/");
                }
            } else {
                Teacher teacher = Teacher.dao.findById(getCookie("dit"));
                setSessionAttr("teacher", teacher);
                render("/static/html/mobile/teacher/Mobile_Teacher_Course.html");
            }
        } else {
            render("/static/html/mobile/teacher/Mobile_Teacher_Course.html");
        }
    }

    /**
     * 登录电脑_管理_课程
     */
    public void PC_Manager_Course() {
        render("/static/html/pc/manager/PC_Manager_Course.html");
//        // 未登录
//        if (getSessionAttr("teacher") == null) {
//            // cookie过期
//            if (getCookie("dim").equals("")) {
//                redirect("/pc");
//            } else {
//                Teacher teacher = Teacher.dao.findById(getCookie("dim"));
//                setSessionAttr("teacher", teacher);
//                render("/static/html/pc/manager/PC_Manager_Course.html");
//            }
//        } else {
//            render("/static/html/pc/manager/PC_Manager_Course.html");
//        }
    }



    /**
     * 列表
     */
    @Before(AjaxManager.class)
    public void list() {
        renderJson(Course.dao.find("SELECT * FROM course WHERE state = ? AND type= ? ",getPara("state"),getPara("type")));
    }

    /**
     * 电脑_查询
     */

    public void PC_Query() {
        renderJson(Course.dao.paginate(
                getParaToInt("pageCurrent"),
                getParaToInt("pageSize"),
                "SELECT *, " +
                "(case type when 1 then '必修课' when 2 then '选修课' else '错误' end ) as tname, " +
                "(case state when 1 then '可用' when 2 then '停用' else '错误' end ) as sname ",
                "FROM course WHERE name LIKE '%" + getPara("keyword") + "%' OR detail LIKE '%" + getPara("keyword") + "%' ORDER BY id ASC").getList());
    }
    /**
     * 手机_查询
     */
    public void Mobile_Query() {
        renderJson(Course.dao.paginate(
                getParaToInt("pageCurrent"),
                getParaToInt("pageSize"),
                "SELECT *, " +
                        "(case type when 1 then '必修课' when 2 then '选修课' else '错误' end ) as tname, " +
                        "(case state when 1 then '可用' when 2 then '停用' else '错误' end ) as sname ",
                "FROM course WHERE name LIKE '%" + getPara("keyword") + "%' OR detail LIKE '%" + getPara("keyword") + "%' ORDER BY id ASC").getList());
    }
    /**
     * 电脑_计数
     */
    public void PC_Total() {
        Long count = Db.queryLong("SELECT COUNT(*) FROM course WHERE name LIKE '%" + getPara("keyword") + "%' OR detail LIKE '%" + getPara("keyword") + "%'");
        renderText(count.toString());
    }

    /**
     * 手机_计数
     */
    public void Mobile_Total() {
        Long count = Db.queryLong("SELECT COUNT(*) FROM course WHERE name LIKE '%" + getPara("queryString") + "%' OR detail LIKE '%" + getPara("queryString") + "%'");
        if (count % getParaToInt("pageSize") == 0) {
            renderText((count / getParaToInt("pageSize")) + "");
        } else {
            renderText((count / getParaToInt("pageSize") + 1) + "");
        }
    }
    /**
     * 获取
     */
    public void get() {
        renderJson(Course.dao.findById(getPara("id")));
    }

    /**
     * 激活
     */
//    @Before({Tx.class, AjaxManager.class})
    public void active() {
        Course course = Course.dao.findById(getPara("id"));
        if (course == null) {
            renderText("要重新激活的课程不存在!");
        } else if (course.get("state").toString().equals("1")) {
            renderText("该课程已激活!");
        } else {
            course.set("state", 1).update();
            renderText("OK");
        }
    }

    /**
     * 注销
     */
//    @Before({Tx.class, AjaxManager.class})
    public void inactive() {
        Course course = Course.dao.findById(getPara("id"));
        if (course == null) {
            renderText("要重新注销的课程不存在!");
        } else if (course.get("state").toString().equals("0")) {
            renderText("该课程已注销!");
        } else {
            course.set("state", 0).update();
            renderText("OK");
        }
    }

    /**
     * 检测名称_新增
     */

    public void checkNameForAdd() {
        if (Course.dao.find("SELECT * FROM course WHERE name = ?", getPara("name")).size() != 0) {
            renderText("该课程名称已存在!");
        } else {
            renderText("OK");
        }
    }

    /**
     * 检测名称_修改
     */
    public void checkNameForEdit() {
        if (!Course.dao.findById(getPara("id")).get("name").equals(getPara("name"))
                && Course.dao.find("SELECT * FROM course WHERE name = ?", getPara("name")).size() != 0) {
            renderText("该课程名称已存在!");
        } else {
            renderText("OK");
        }
    }

    /**
     * 保存
     */
    @Before({Tx.class, AjaxManager.class})
    public void save() {
        if (Course.dao.find("SELECT * FROM course WHERE name = ?", getPara("name")).size() != 0) {
            renderText("该名称已存在!");
        } else {
            Course course = new Course();
            course.set("name", getPara("name"))
                    .set("detail", getPara("detail"))
                    .set("type", getPara("type"))
                    .set("state", 1)
                    .save();
            renderText("OK");
        }
    }
    /**
     * 保存
     */

    public void PC_save() {
        if (Course.dao.find("SELECT * FROM course WHERE name = ?", getPara("name")).size() != 0) {
            renderText("该名称已存在!");
        } else {
            Course course = new Course();
            course.set("name", getPara("name"))
                    .set("detail", getPara("detail"))
                    .set("type", getPara("type"))
                    .set("state", getPara("state"))
                    .save();
            renderText("OK");
        }
    }
    /**
     * 修改
     */
    public void PC_edit() {
        Course course = Course.dao.findById(getPara("id"));
        if (course == null) {
            renderText("要修改的课程不存在!");
        } else {
            if (!getString(course.getStr("name")).equals(getPara("name"))
                    && Course.dao.find("SELECT * FROM course WHERE name = ?", getPara("name")).size() != 0) {
                renderText("该名称已存在!");
            } else {
                course.set("name", getPara("name"))
                        .set("detail", getPara("detail"))
                        .set("type", getPara("type"))
                        .set("state", getPara("state"))
                        .update();
                renderText("OK");
            }
        }
    }


    /**
     * 获取全选时的字符串
     */
    @Before(AjaxManager.class)
    public void all() {
        List<Course> courses = Course.dao.find("SELECT id FROM course WHERE state=1");
        String course = "";
        if (courses.size() > 0) {
            for (Course c : courses) {
                course = course + c.getId() + ",";
            }
            renderJson("[" + course.substring(0, course.length() - 1) + "]");
        } else {
            renderJson("[]");
        }
    }

    /**
     * 导出
     */
    public void download() throws IOException {
        String[] title={"序号","课程名称","课程详情","课程类型","课程状态"};
        String fileName = "Course";
        String SQL = "select id AS 序号,name AS 课程名称, detail AS 课程详情, " +
                "(case type when 1 then '必修课' when 2 then '选修课' else '错误' end ) AS 课程类型, " +
                "(case state when 1 then '可用' when 2 then '停用' else '错误' end ) AS 课程状态 " +
                "from course where name like '%"+getPara("keyword")+"%' OR detail LIKE '%" + getPara("keyword") + "%' " +
                "ORDER BY id ASC";
        ExportUtil.export(title,fileName,SQL,getResponse());
    }
}
