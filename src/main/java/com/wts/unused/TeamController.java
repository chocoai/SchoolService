//package com.wts.controller;
//
//import com.foxinmy.weixin4j.exception.WeixinException;
//import com.foxinmy.weixin4j.qy.message.NotifyMessage;
//import com.foxinmy.weixin4j.qy.model.IdParameter;
//import com.foxinmy.weixin4j.qy.model.User;
//import com.foxinmy.weixin4j.tuple.Text;
//import com.jfinal.aop.Before;
//import com.jfinal.core.Controller;
//import com.jfinal.plugin.activerecord.Db;
//import com.jfinal.plugin.activerecord.Page;
//import com.jfinal.plugin.activerecord.Record;
//import com.jfinal.plugin.activerecord.tx.Tx;
//import com.wts.entity.WP;
//import com.wts.entity.model.*;
//import com.wts.interceptor.*;
//import com.wts.util.ParamesAPI;
//import com.wts.util.Util;
//
//import java.util.List;
//
//public class TeamController extends Controller {
//
//  public void forManager() throws WeixinException {
//    // 检测session中是否存在teacher
//    if (getSessionAttr("manager") == null || ((Enterprise)getSessionAttr("manager")).getIsManager()!=1) {
//      // 检测cookie中是否存在EnterpriseId
//      if (getCookie("die") == null || getCookie("die").equals("")) {
//        // 检测是否来自微信请求
//        if (!(getPara("code") == null || getPara("code").equals(""))) {
//          User user = WP.me.getUserByCode(getPara("code"));
//          Enterprise enterprise = Enterprise.dao.findFirst("select * from enterprise where state=1 and isManager=1 and userId=?", user.getUserId());
//          // 检测是否有权限
//          if (enterprise != null) {
//            setSessionAttr("manager", enterprise);
//            setCookie("die", enterprise.getId().toString(), 60 * 30);
//            render("/static/TeamForManager.html");
//          } else {
//            redirect("/");
//          }
//        } else {
//          redirect("/");
//        }
//      } else {
//        Enterprise enterprise = Enterprise.dao.findById(getCookie("die"));
//        setSessionAttr("manager", enterprise);
//        render("/static/TeamForManager.html");
//      }
//    } else {
//      render("/static/TeamForManager.html");
//    }
//  }
//  @Before(AjaxManager.class)
//  public void queryByName() {
//    Page<Team> teams= Team.dao.queryByName(getParaToInt("pageCurrent"),getParaToInt("pageSize"),getPara("queryString"));
//    renderJson(teams.getList());
//  }
//  @Before(AjaxManager.class)
//  public void totalByName() {
//    Long count = Db.queryLong("select count(*) from team where name like '%"+ getPara("queryString") +"%'");
//    if (count%getParaToInt("pageSize")==0) {
//      renderText((count/getParaToInt("pageSize"))+"");
//    } else {
//      renderText((count/getParaToInt("pageSize")+1)+"");
//    }
//  }
//  @Before(AjaxTeacher.class)
//  public void teacherTeamList() {
//    List<Record> teams = Db.find("select DISTINCT team_id as id,team.name,team.state from teamplan left join team on team.id=teamplan.team_id where teacher_id=?",((Enterprise) getSessionAttr("Teacher")).getId());
//    renderJson(teams);
//  }
//  @Before(AjaxTeacher.class)
//  public void teacherTeamFirst() {
//    Teamplan teamplan = Teamplan.dao.findFirst("select DISTINCT team_id as id from teamplan where teacher_id=?",((Enterprise) getSessionAttr("Teacher")).get("id").toString());
//    if (teamplan!=null){
//      renderText(teamplan.get("id").toString());
//    }else{
//      renderText("0");
//    }
//  }
//  @Before({Login.class, Ajax.class})
//  public void getNameById() {
//    Team team = Team.dao.findById(getPara("id"));
//    renderText(team.get("name").toString());
//  }
//  @Before(AjaxManager.class)
//  public void teamList() {
//    List<Team> teams = Team.dao.find("select * from team where state=1 order by name desc");
//    renderJson(teams);
//  }
//  @Before(AjaxParent.class)
//  public void parentTeamList() {
//    List<Record> teams = Db.find("select distinct student.team_id as id,team.name as name,team.state as state" +
//            " from student" +
//            " left join team" +
//            " on team.id=student.team_id" +
//            " where team.state=1 and student.state=1 and student.id in (select distinct student_id from relation where parent_id=?)",((Enterprise) getSessionAttr("parent")).getId());
//    renderJson(teams);
//  }
//  @Before(AjaxParent.class)
//  public void parentTeamFirst() {
//    Record team = Db.findFirst("select distinct student.team_id as id,team.name as name,team.state as state" +
//            " from student" +
//            " left join team" +
//            " on team.id=student.team_id" +
//            " where team.state=1 and student.state=1 and student.id in (select distinct student_id from relation where parent_id=?)",((Enterprise) getSessionAttr("parent")).getId());
//    if (team!=null){
//      renderText(team.get("id").toString());
//    }else{
//      renderText("0");
//    }
//  }
//  @Before(AjaxManager.class)
//  public void checkNameForNew() {
//    if (Team.dao.find("select * from team where name=?", getPara("name")).size()!=0) {
//      renderText("该社团名称已存在!");
//    } else {
//      renderText("OK");
//    }
//  }
//  @Before(AjaxManager.class)
//  public void checkNameForEdit() {
//    if (!Team.dao.findById(getPara("id")).getName().equals(getPara("name"))
//            && Team.dao.find("select * from team where name=?", getPara("name")).size() != 0) {
//      renderText("该社团名称已存在!");
//    } else {
//      renderText("OK");
//    }
//  }
//  @Before(AjaxManager.class)
//  public void getById() {
//    Team team = Team.dao.findById(getPara("id"));
//    renderJson(team);
//  }
//  @Before(AjaxManager.class)
//  public void inactiveById() {
//    Team team = Team.dao.findById(getPara("id"));
//    if (team == null) {
//      renderText("未找到指定id的社团");
//    } else if (team.get("state").toString().equals("2")) {
//      renderText("该社团已注销!");
//    } else {
//      team.set("state",2).update();
//      renderText("OK");
//    }
//  }
//  @Before(AjaxManager.class)
//  public void activeById() {
//    Team team = Team.dao.findById(getPara("id"));
//    if (team == null) {
//      renderText("未找到指定id的社团");
//    } else if (team.get("state").toString().equals("1")) {
//      renderText("该社团已激活!");
//    } else {
//      team.set("state",1).update();
//      renderText("OK");
//    }
//  }
//  @Before(AjaxManager.class)
//  public void getTeamTeacher() {
//    List<Teamplan> teamPlan = Teamplan.dao.find("select * from teamplan where team_id=?",getPara("team"));
//    String teamTeachers = "";
//    if (teamPlan.size()!=0) {
//      for (int i = 0; i < teamPlan.size(); i++) {
//        teamTeachers = teamTeachers + "'" + teamPlan.get(i).get("teacher_id") + "',";
//      }
//      teamTeachers = "teamTeachers: [" + teamTeachers.substring(0,teamTeachers.length()-1) + "]";
//    } else {
//      teamTeachers = "teamTeachers: []";
//    }
//    renderText("{"+teamTeachers+"}");
//  }
//  @Before({Tx.class,AjaxManager.class})
//  public void save()  {
//    if (Room.dao.find("select * from team where name=?", getPara("name")).size()!=0) {
//      renderText("该社团名称已存在!");
//    } else {
//      Team team = new Team();
//      team.set("name",getPara("name")).set("state",1).save();
//      String[] teamTeacher = getParaValues("teamTeacher[]");
//      if (teamTeacher!=null){
//        for (String i : teamTeacher){
//          Teamplan teamplan = new Teamplan();
//          teamplan.set("team_id",team.get("id")).set("teacher_id",i).save();
//          Enterprise Teacher = Enterprise.dao.findById(i);
//          if (Teacher.getState()==1){
//            try {
//              WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+team.getName()+"的管理老师"), new IdParameter().putUserIds(Teacher.getUserId()), false));
//            } catch (Exception e) {
//              renderText(e.getMessage());
//            }
//          }
//        }
//      }
//      renderText("OK");
//    }
//  }
//  @Before({Tx.class,AjaxManager.class})
//  public void edit()  {
//    Team team = Team.dao.findById(getPara("id"));
//    if (team == null) {
//      renderText("要修改的社团不存在!");
//    } else {
//      if (!Util.getString(team.getStr("name")).equals(getPara("name"))
//              &&  Team.dao.find("select * from team where name=?", getPara("name")).size()!=0) {
//        renderText("该社团名称已存在!");
//      } else {
//        team.set("name",getPara("name")).update();
//        Db.update("delete from teamplan where team_id = ?", getPara("id"));
//        String[] teamTeacher = getParaValues("teamTeacher[]");
//        if (teamTeacher!=null){
//          for (String i : teamTeacher){
//            Teamplan teamplan = new Teamplan();
//            teamplan.set("team_id",team.get("id")).set("teacher_id",i).save();
//            Enterprise Teacher = Enterprise.dao.findById(i);
//            if (Teacher.getState()==1){
//              try {
//                WP.me.sendNotifyMessage(new NotifyMessage(ParamesAPI.teacherId, new Text("您已被设为"+team.getName()+"的管理老师"), new IdParameter().putUserIds(Teacher.getUserId()), false));
//              } catch (Exception e) {
//                renderText(e.getMessage());
//              }
//            }
//          }
//        }
//        renderText("OK");
//      }
//    }
//  }
//}
