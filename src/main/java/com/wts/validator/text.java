package com.wts.validator;

import com.wts.entity.WxService;
import me.chanjar.weixin.cp.api.WxCpInMemoryConfigStorage;
import me.chanjar.weixin.cp.api.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpMessage;

public class text {
  public static void main(String[] args) {
    WxCpInMemoryConfigStorage config = new WxCpInMemoryConfigStorage();
    config.setCorpId("wx947e7da41f395af7");
    config.setCorpSecret("thcMcEeJdhuSDw7S_9314ILJ2hqPRCCxhupksDvPDQkUgfqfjDxVx16jPR_ztzzK");
    config.setAgentId(26);
    config.setToken("weixin4j");
    config.setAesKey("vUiM1GBGtPzce8Jh3UikdsHvn3NMghUT9l9Fr8UarrT");

    WxCpServiceImpl wxCpService = new WxCpServiceImpl();
    wxCpService.setWxCpConfigStorage(config);
    String userId = "WangTianShuo";
    WxCpMessage message = WxCpMessage.TEXT().agentId(26).toUser(userId).content("Hello World").build();
    try {
      System.out.println(message.toJson());

      //wxCpService.messageSend(message);
      WxService.getMe().messageSend(message);
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }
}
