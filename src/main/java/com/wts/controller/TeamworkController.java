package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.core.Controller;
import com.wts.entity.WP;
import com.wts.entity.model.Enterprise;

public class TeamworkController extends Controller {
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
            render("/static/TeamworkForTeacher.html");
          } else {
            redirect("/");
          }
        } else {
          redirect("/");
        }
      } else {
        Enterprise enterprise = Enterprise.dao.findById(getCookie("die"));
        setSessionAttr("teacher", enterprise);
        render("/static/TeamworkForTeacher.html");
      }
    } else {
      render("/static/TeamworkForTeacher.html");
    }
  }
}
