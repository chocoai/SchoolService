package com.wts.weixin.service;

import com.wts.weixin.config.TeacherConfig;
import me.chanjar.weixin.cp.api.WxCpServiceImpl;

public class TeacherService extends WxCpServiceImpl{
  private volatile static TeacherService teacherService;

  private TeacherService() {
    this.setWxCpConfigStorage(TeacherConfig.me());
  }

  public static TeacherService me() {
    if (teacherService == null) {
      synchronized (TeacherService.class) {
        if (teacherService == null) {
          teacherService = new TeacherService();
        }
      }
    }
    return teacherService;
  }


}