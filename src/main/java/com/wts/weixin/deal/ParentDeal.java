package com.wts.weixin.deal;


import me.chanjar.weixin.cp.bean.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutMessage;


public class ParentDeal {
    public static WxCpXmlOutMessage getOutMessage(WxCpXmlMessage inMessage) {
        if (inMessage.getMsgType().equals("text")) {
            return WxCpXmlOutMessage
                    .TEXT()
                    .content("")
                    .fromUser(inMessage.getToUserName())
                    .toUser(inMessage.getFromUserName())
                    .build();
        } else if (inMessage.getEvent().equals("subscribe")) {
            System.out.println("关注了");
            return null;
        } else if (inMessage.getEvent().equals("unsubscribe")) {
            System.out.println("取消关注了");
            return null;
        } else if (inMessage.getEvent().equals("enter_agent")) {
            System.out.println("进入应用了");
            return null;
        } else if (inMessage.getEvent().equals("click")) {
            if(inMessage.getEventKey().equals("A1")){
                return WxCpXmlOutMessage
                        .TEXT()
                        .content("")
                        .fromUser(inMessage.getToUserName())
                        .toUser(inMessage.getFromUserName())
                        .build();
            }else{
                System.out.println("点击了按钮");
            }
            return null;
        } else  {
            return WxCpXmlOutMessage
                    .TEXT()
                    .content("测试加密消息")
                    .fromUser(inMessage.getToUserName())
                    .toUser(inMessage.getFromUserName())
                    .build();
        }
    }
}
