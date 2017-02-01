package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wts.entity.WP;
import com.wts.entity.model.Enterprise;
import com.wts.entity.model.Room;
import com.wts.entity.model.Roomwork;
import com.wts.interceptor.AjaxManager;
import com.wts.interceptor.AjaxTeacher;


public class RoomworkController extends Controller {
    public void forTeacher() throws WeixinException {
        // 检测session中是否存在teacher
        if (getSessionAttr("teacher") == null || ((Enterprise) getSessionAttr("teacher")).getIsTeacher() != 1) {
            // 检测cookie中是否存在EnterpriseId
            if (getCookie("die") == null || getCookie("die").equals("")) {
                // 检测是否来自微信请求
                if (!(getPara("code") == null || getPara("code").equals(""))) {
                    User user = WP.me.getUserByCode(getPara("code"));
                    Enterprise enterprise = Enterprise.dao.findFirst("select * from enterprise where state=1 and isTeacher=1 and userId=?", user.getUserId());
                    // 检测是否有权限
                    if (enterprise != null) {
                        setSessionAttr("teacher", enterprise);
                        setCookie("die", enterprise.getId().toString(), 60 * 30);
                        render("/static/RoomworkForTeacher.html");
                    } else {
                        redirect("/");
                    }
                } else {
                    redirect("/");
                }
            } else {
                Enterprise enterprise = Enterprise.dao.findById(getCookie("die"));
                setSessionAttr("teacher", enterprise);
                render("/static/RoomworkForTeacher.html");
            }
        } else {
            render("/static/RoomworkForTeacher.html");
        }
    }

    @Before(AjaxTeacher.class)
    public void queryByRoomId() {
        Page<Record> roomworks = Db.paginate(getParaToInt("pageCurrent"), getParaToInt("pageSize"), "SELECT roomwork.*,course.name", "FROM roomwork left join course on roomwork.course_id=course.id WHERE roomwork.room_id = "+ getPara("roomId") +" and roomwork.content LIKE '%"+getPara("queryString")+"%' and roomwork.teacher_id = "+ ((Enterprise) getSessionAttr("teacher")).getId() +" ORDER BY course.id DESC");
        renderJson(roomworks.getList());
    }
    @Before(AjaxTeacher.class)
    public void totalByRoomId() {
        Long count = Db.queryLong("select count(*) from roomwork where room_id = "+ getPara("roomId") + " and content like '%"+ getPara("queryString") +"%' and roomwork.teacher_id = "+ ((Enterprise) getSessionAttr("teacher")).getId());
        if (count%getParaToInt("pageSize")==0) {
            renderText((count/getParaToInt("pageSize"))+"");
        } else {
            renderText((count/getParaToInt("pageSize")+1)+"");
        }
    }
}
