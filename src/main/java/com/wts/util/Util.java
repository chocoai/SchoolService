package com.wts.util;


import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

public class Util {



  public static String getString(String str) {
    if (str == null) {
      return "";
    } else {
      return str.trim();
    }
  }

  public static String PermissionString(String sqlString, String teacherId) {
    List<Record> lists = Db.find(
            "SELECT permission.url, teacherpermission.state FROM teacherpermission " +
                    " LEFT JOIN permission" +
                    " ON teacherpermission.permission_id = permission.id" +
                    " WHERE teacher_id=? AND permission.url LIKE '%" + sqlString + "%'"
            , teacherId);
    String permission = "";
    if (lists.size() != 0) {
      for (int i = 0; i < lists.size(); i++) {
        if (lists.get(i).get("state").toString().equals("1")) {
          permission = permission + "\"" + lists.get(i).get("url") + "\": true, ";
        } else {
          permission = permission + "\"" + lists.get(i).get("url") + "\": false, ";
        }
      }
      return "{" + permission.substring(0, permission.length() - 2).replace(sqlString + "/", "") + "}";
    } else {
      return "{}";
    }
  }

  public static void main(String[] args) {
    String b = "^([\\d]{4}(((0[13578]|1[02])((0[1-9])|([12][0-9])|(3[01])))|(((0[469])|11)((0[1-9])|([12][1-9])|30))|(02((0[1-9])|(1[0-9])|(2[1-8])))))|((((([02468][048])|([13579][26]))00)|([0-9]{2}(([02468][048])|([13579][26]))))(((0[13578]|1[02])((0[1-9])|([12][0-9])|(3[01])))|(((0[469])|11)((0[1-9])|([12][1-9])|30))|(02((0[1-9])|(1[0-9])|(2[1-9])))))";
    System.out.println("201p0229".matches(b));
    // System.out.println("111"+CheckNull(null));
  }
}
