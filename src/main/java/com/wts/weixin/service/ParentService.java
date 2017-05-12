package com.wts.weixin.service;

import com.wts.weixin.config.ParentConfig;
import me.chanjar.weixin.cp.api.WxCpServiceImpl;

public class ParentService extends WxCpServiceImpl{
  private volatile static ParentService parentService;

  private ParentService() {
    this.setWxCpConfigStorage(ParentConfig.me());
  }

  public static ParentService me() {
    if (parentService == null) {
      synchronized (ParentService.class) {
        if (parentService == null) {
          parentService = new ParentService();
        }
      }
    }
    return parentService;
  }


}