package com.wts.service;


import me.chanjar.weixin.cp.api.WxCpInMemoryConfigStorage;
import me.chanjar.weixin.cp.api.WxCpMessageRouter;
import me.chanjar.weixin.cp.api.WxCpServiceImpl;

public class teacherService extends WxCpServiceImpl {
  public void init() {
    final WxCpInMemoryConfigStorage config = new WxCpInMemoryConfigStorage();
    config.setCorpId("wx947e7da41f395af7");      // 设置微信企业号的appid
    config.setCorpSecret("thcMcEeJdhuSDw7S_9314ILJ2hqPRCCxhupksDvPDQkUgfqfjDxVx16jPR_ztzzK");  // 设置微信企业号的app corpSecret
    config.setAgentId(26);     // 设置微信企业号应用ID
    config.setToken("weixin4j");       // 设置微信企业号应用的token
    config.setAesKey("vUiM1GBGtPzce8Jh3UikdsHvn3NMghUT9l9Fr8UarrT");      // 设置微信企业号应用的EncodingAESKey

    super.setWxCpConfigStorage(config);

    this.refreshRouter();
  }

  private void refreshRouter() {
    final WxCpMessageRouter newRouter = new WxCpMessageRouter(this);
  }
}
