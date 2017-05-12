package com.wts.weixin.config;

import com.wts.util.ParamesAPI;
import me.chanjar.weixin.cp.api.WxCpInMemoryConfigStorage;

public class TeacherConfig extends WxCpInMemoryConfigStorage {
    private volatile static TeacherConfig teacherConfig;

    private TeacherConfig() {
        this.setCorpId(ParamesAPI.corpId);
        this.setCorpSecret(ParamesAPI.teacherSecret);
        this.setAgentId(ParamesAPI.teacherAgentId);
        this.setToken(ParamesAPI.teacherToken);
        this.setAesKey(ParamesAPI.teacherEncodingAESKey);
    }

    public static TeacherConfig me() {
        if (teacherConfig == null) {
            synchronized (TeacherConfig.class) {
                if (teacherConfig == null) {
                    teacherConfig = new TeacherConfig();
                }
            }
        }
        return teacherConfig;
    }


}