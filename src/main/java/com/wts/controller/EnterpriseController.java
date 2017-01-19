package com.wts.controller;

import com.foxinmy.weixin4j.qy.model.User;
import com.jfinal.core.Controller;
import com.wts.entity.WP;
import com.wts.entity.model.Enterprise;
import com.wts.util.msg.Util.MessageUtil;

import java.util.Map;

/**
 * Created by 王天硕 on 2017/1/19.
 */
public class EnterpriseController extends Controller {

  /**
   * 处理微信发来的关注
   *
   * @param request
   */
  public static void subscribe(String request) {
    try {
      // 调用parseXml方法解析请求消息
      Map<String, String> requestMap = MessageUtil.parseXml(request);
      // 发送方帐号
      String FromUserName = requestMap.get("FromUserName");
      if (requestMap.get("Event").equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
        User user = WP.me.getUser(FromUserName);
        Enterprise enterprise = Enterprise.dao.findFirst("select * from enterprise where userId=?", FromUserName);
        enterprise.set("sex", user.getGender())
                .set("picUrl", user.getAvatar())
                .set("state", 1)
                .update();
      }
      if (requestMap.get("Event").equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
        Enterprise enterprise = Enterprise.dao.findFirst("select * from enterprise where userId=?", FromUserName);
        enterprise.set("picUrl", "")
                .set("state", 4)
                .update();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
