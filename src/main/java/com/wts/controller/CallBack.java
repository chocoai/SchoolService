package com.wts.controller;

import com.jfinal.core.Controller;
import com.wts.util.aes.AesException;
import com.wts.util.ParamesAPI;
import com.wts.util.aes.WXBizMsgCrypt;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class CallBack extends Controller {

  public void callBack() throws IOException {
    if (getRequest().getMethod() == "GET") {

      // 微信加密签名
      String msg_signature = getPara("msg_signature");
      // 时间戳
      String timestamp = getPara("timestamp");
      // 随机数
      String nonce = getPara("nonce");
      // 随机字符串
      String echostr = getPara("echostr");

      PrintWriter out = this.getResponse().getWriter();
      // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
      String result = null;
      try {
        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(ParamesAPI.token, ParamesAPI.encodingAESKey, ParamesAPI.corpId);
        result = wxcpt.VerifyURL(msg_signature, timestamp, nonce, echostr);
      } catch (AesException e) {
        e.printStackTrace();
      }
      if (result == null) {
        result = ParamesAPI.token;
      }
      String str = msg_signature + " " + timestamp + " " + nonce + " " + echostr;
      out.print(result);
      out.close();
      out = null;
    } else if (getRequest().getMethod() == "POST") {

      this.getRequest().setCharacterEncoding("UTF-8");
      this.getResponse().setCharacterEncoding("UTF-8");
      // 微信加密签名
      String msg_signature = getPara("msg_signature");
      // 时间戳
      String timestamp = getPara("timestamp");
      // 随机数
      String nonce = getPara("nonce");
      //从请求中读取整个post数据
      InputStream inputStream = this.getRequest().getInputStream();
      //commons.io.jar 方法
      String Post = IOUtils.toString(inputStream, "UTF-8");
      String Msg = "";
      WXBizMsgCrypt wxcpt = null;
      try {
        wxcpt = new WXBizMsgCrypt(ParamesAPI.token,ParamesAPI.encodingAESKey,ParamesAPI.corpId);
        //解密消息
        Msg = wxcpt.DecryptMsg(msg_signature, timestamp, nonce, Post);
      } catch (AesException e) {
        e.printStackTrace();
      }
      // 调用核心业务类接收消息、处理消息
      String respMessage = CoreService.processRequest(Msg);
      EnterpriseController.subscribe(Msg);
      // respMessage打印结果
      String encryptMsg = "";
      try {
        //加密回复消息
        encryptMsg = wxcpt.EncryptMsg(respMessage, timestamp, nonce);
      } catch (AesException e) {
        e.printStackTrace();
      }
      // 响应消息
      PrintWriter out = this.getResponse().getWriter();
      // out.print(encryptMsg);
      out.close();
    } else {

    }
  }

}
