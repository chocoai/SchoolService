package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.WP;
import com.wts.entity.model.*;
import com.wts.interceptor.AjaxFunction;
import com.wts.util.Util;

import java.util.List;

public class TeamController extends Controller {

  public void index() throws WeixinException {
    // 检测session中是否存在teacher
    if (getSessionAttr("teacher") == null) {
      // 检测cookie中是否存在EnterpriseId
      if (getCookie("die") == null || getCookie("die").equals("")) {
        // 检测是否来自微信请求
        if (!(getPara("code") == null || getPara("code").equals(""))) {
          User user = WP.me.getUserByCode(getPara("code"));
          Enterprise teacher = Enterprise.dao.findFirst("select * from enterprise where state=1 and userId=?", user.getUserId());
          setSessionAttr("teacher", teacher);
          render("/static/TeamManage.html");
        } else {
          redirect("/");
        }
      } else {
        Enterprise teacher = Enterprise.dao.findById(getCookie("die"));
        setSessionAttr("teacher", teacher);
        render("/static/TeamManage.html");
      }
    } else {
      render("/static/TeamManage.html");
    }
  }
  @Before(AjaxFunction.class)
  public void queryByName() {
    Page<Team> teams= Team.dao.queryByName(getParaToInt("pageCurrent"),getParaToInt("pageSize"),getPara("queryString"));
    renderJson(teams.getList());
  }
  @Before(AjaxFunction.class)
  public void totalByName() {
    Long count = Db.queryLong("select count(*) from team where name like '%"+ getPara("queryString") +"%'");
    if (count%getParaToInt("pageSize")==0) {
      renderText((count/getParaToInt("pageSize"))+"");
    } else {
      renderText((count/getParaToInt("pageSize")+1)+"");
    }
  }
  @Before(AjaxFunction.class)
  public void checkNameForNew() {
    if (Team.dao.find("select * from team where name=?", getPara("name")).size()!=0) {
      renderText("该社团名称已存在!");
    } else {
      renderText("OK");
    }
  }
  @Before(AjaxFunction.class)
  public void checkNameForEdit() {
    if (!Team.dao.findById(getPara("id")).getName().equals(getPara("name"))
            && Team.dao.find("select * from team where name=?", getPara("name")).size() != 0) {
      renderText("该社团名称已存在!");
    } else {
      renderText("OK");
    }
  }
  @Before(AjaxFunction.class)
  public void getById() {
    Team team = Team.dao.findById(getPara("id"));
    renderJson(team);
  }
  @Before(AjaxFunction.class)
  public void deleteById() {
    if (Team.dao.findById(getPara("id")) == null) {
      renderText("未找到指定id的社团");
    } else {
      Team team = Team.dao.findById(getPara("id"));
      team.set("state",2).update();
      renderText("OK");
    }
  }
  @Before(AjaxFunction.class)
  public void teacherList() {
    List<Enterprise> teachers = Enterprise.dao.find("select * from enterprise where (isTeacher=1 or isManager=1) and (state=1 or state=2) order by name asc");
    renderJson(teachers);
  }

  @Before(AjaxFunction.class)
  public void getTeamTeacher() {
    List<Teamplan> teamPlan = Teamplan.dao.find("select * from teamplan where team_id=?",getPara("team"));
    String teamTeachers = "";
    if (teamPlan.size()!=0) {
      for (int i = 0; i < teamPlan.size(); i++) {
        teamTeachers = teamTeachers + "'" + teamPlan.get(i).get("teacher_id") + "',";
      }
      teamTeachers = "teamTeachers: [" + teamTeachers.substring(0,teamTeachers.length()-1) + "]";
    } else {
      teamTeachers = "teamTeachers: []";
    }
    renderText("{"+teamTeachers+"}");
  }
  @Before({Tx.class,AjaxFunction.class})
  public void save()  {
    if (Room.dao.find("select * from team where name=?", getPara("name")).size()!=0) {
      renderText("该社团名称已存在!");
    } else {
      Team team = new Team();
      team.set("name",getPara("name")).set("state",1).save();
      String[] teamTeacher = getParaValues("teamTeacher[]");
      if (teamTeacher!=null){
        for (String i : teamTeacher){
          Teamplan teamplan = new Teamplan();
          teamplan.set("team_id",team.get("id")).set("teacher_id",i).save();
        }
      }
      renderText("OK");
    }
  }
  @Before({Tx.class,AjaxFunction.class})
  public void edit()  {
    Team team = Team.dao.findById(getPara("id"));
    if (team == null) {
      renderText("要修改的社团不存在!");
    } else {
      if (!Util.getString(team.getStr("name")).equals(getPara("name"))
              &&  Team.dao.find("select * from team where name=?", getPara("name")).size()!=0) {
        renderText("该社团名称已存在!");
      } else {
        team.set("name",getPara("name")).update();
        Db.update("delete from teamplan where team_id = ?", getPara("id"));
        String[] teamTeacher = getParaValues("teamTeacher[]");
        if (teamTeacher!=null){
          for (String i : teamTeacher){
            Teamplan teamplan = new Teamplan();
            teamplan.set("team_id",team.get("id")).set("teacher_id",i).save();
          }
        }
        renderText("OK");
      }
    }
  }
}
