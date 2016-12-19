package com.wts.controller;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.handler.DebugMessageHandler;
import com.foxinmy.weixin4j.handler.MessageHandlerAdapter;
import com.foxinmy.weixin4j.handler.WeixinMessageHandler;
import com.foxinmy.weixin4j.message.TextMessage;
import com.foxinmy.weixin4j.request.WeixinRequest;
import com.foxinmy.weixin4j.response.TextResponse;
import com.foxinmy.weixin4j.response.WeixinResponse;
import com.foxinmy.weixin4j.startup.WeixinServerBootstrap;

public class MessageServerStartup {
    public static void main(String[] args) throws WeixinException {
        // 针对文本消息回复
        WeixinMessageHandler messageHandler = new MessageHandlerAdapter<TextMessage>() {
            @Override
            public WeixinResponse doHandle0(WeixinRequest request, TextMessage message) throws WeixinException {
                return new TextResponse("HelloWorld!");
            }
        };
        // 当消息类型为文本(text)时回复「HelloWorld」, 否则回复调试消息
        new WeixinServerBootstrap("appid", "token", "aesKey").addHandler(
                messageHandler, DebugMessageHandler.global).startup();
    }
}
