package com.wts.entity;

import me.chanjar.weixin.cp.api.*;

public class WxService {
    // 定义一个私有的构造方法
    private WxService() {
        WxCpInMemoryConfigStorage config = new WxCpInMemoryConfigStorage();
        config.setCorpId("wx947e7da41f395af7");      // 设置微信企业号的appid
        config.setCorpSecret("thcMcEeJdhuSDw7S_9314ILJ2hqPRCCxhupksDvPDQkUgfqfjDxVx16jPR_ztzzK");  // 设置微信企业号的app corpSecret
        config.setAgentId(26);     // 设置微信企业号应用ID
        config.setToken("weixin4j");       // 设置微信企业号应用的token
        config.setAesKey("vUiM1GBGtPzce8Jh3UikdsHvn3NMghUT9l9Fr8UarrT");      // 设置微信企业号应用的EncodingAESKey

        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(config);
    }

    // 将自身的实例对象设置为一个属性,并加上Static和final修饰符
    private static final WxCpService me = new WxCpServiceImpl();

    // 静态方法返回该类的实例
    public static me.chanjar.weixin.cp.api.WxCpService getMe() {
        return me;
    }

}
