package com.wts.entity;

import com.wts.util.ParamesAPI;
import me.chanjar.weixin.cp.api.WxCpInMemoryConfigStorage;
import me.chanjar.weixin.cp.api.WxCpServiceImpl;

public class WxService extends WxCpServiceImpl{
  private volatile static WxService wxService;

  private WxService() {
    WxCpInMemoryConfigStorage config = new WxCpInMemoryConfigStorage();
    config.setCorpId(ParamesAPI.corpId);
    config.setCorpSecret(ParamesAPI.secret);
    config.setAgentId(26);
    config.setToken(ParamesAPI.token);
    config.setAesKey(ParamesAPI.encodingAESKey);

    this.setWxCpConfigStorage(config);
  }

  public static WxService getMe() {
    if (wxService == null) {
      synchronized (WxService.class) {
        if (wxService == null) {
          wxService = new WxService();
        }
      }
    }
    return wxService;
  }
}