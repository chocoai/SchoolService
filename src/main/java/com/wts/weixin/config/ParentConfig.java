package com.wts.weixin.config;

import com.wts.util.ParamesAPI;
import me.chanjar.weixin.cp.api.WxCpInMemoryConfigStorage;

public class ParentConfig extends WxCpInMemoryConfigStorage {
    private volatile static ParentConfig parentConfig;

    private ParentConfig() {
        this.setCorpId(ParamesAPI.corpId);
        this.setCorpSecret(ParamesAPI.parentSecret);
        this.setAgentId(ParamesAPI.parentAgentId);
        this.setToken(ParamesAPI.parentToken);
        this.setAesKey(ParamesAPI.parentEncodingAESKey);
    }

    public static ParentConfig me() {
        if (parentConfig == null) {
            synchronized (ParentConfig.class) {
                if (parentConfig == null) {
                    parentConfig = new ParentConfig();
                }
            }
        }
        return parentConfig;
    }


}