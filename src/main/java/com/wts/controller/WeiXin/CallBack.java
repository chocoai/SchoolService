//package com.wts.controller.WeiXin;
//
//import com.foxinmy.weixin4j.qy.model.User;
//import com.jfinal.core.Controller;
//import com.wts.entity.WP;
//import com.wts.entity.model.Parent;
//import com.wts.entity.model.Teacher;
//import com.wts.util.aes.AesException;
//import com.wts.util.ParamesAPI;
//import com.wts.util.aes.WXBizMsgCrypt;
//import com.wts.util.msg.Util.MessageUtil;
//import org.apache.commons.io.IOUtils;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.PrintWriter;
//import java.util.Map;
//
//public class CallBack extends Controller {
//
//  public void callBack() throws IOException {
//
//    if (getRequest().getMethod() == "GET") {
//      // 微信加密签名
//      String msg_signature = getPara("msg_signature");
//      // 时间戳
//      String timestamp = getPara("timestamp");
//      // 随机数
//      String nonce = getPara("nonce");
//      // 随机字符串
//      String echostr = getPara("echostr");
//
//      PrintWriter out = this.getResponse().getWriter();
//      // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
//      String result = null;
//      try {
//        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(ParamesAPI.token, ParamesAPI.encodingAESKey, ParamesAPI.corpId);
//        result = wxcpt.VerifyURL(msg_signature, timestamp, nonce, echostr);
//      } catch (AesException e) {
//        e.printStackTrace();
//      }
//      if (result == null) {
//        result = ParamesAPI.token;
//      }
//      String str = msg_signature + " " + timestamp + " " + nonce + " " + echostr;
//      out.print(result);
//      out.close();
//      out = null;
//    } else if (getRequest().getMethod() == "POST") {
//
//      this.getRequest().setCharacterEncoding("UTF-8");
//      this.getResponse().setCharacterEncoding("UTF-8");
//      // 微信加密签名
//      String msg_signature = getPara("msg_signature");
//      // 时间戳
//      String timestamp = getPara("timestamp");
//      // 随机数
//      String nonce = getPara("nonce");
//      //从请求中读取整个post数据
//      InputStream inputStream = this.getRequest().getInputStream();
//      //commons.io.jar 方法
//      String Post = IOUtils.toString(inputStream, "UTF-8");
//      String Msg = "";
//      WXBizMsgCrypt wxcpt = null;
//      try {
//        wxcpt = new WXBizMsgCrypt(ParamesAPI.token,ParamesAPI.encodingAESKey,ParamesAPI.corpId);
//        //解密消息
//        Msg = wxcpt.DecryptMsg(msg_signature, timestamp, nonce, Post);
//      } catch (AesException e) {
//        e.printStackTrace();
//      }
//      // 调用核心业务类接收消息、处理消息
//      String respMessage = CoreService.processRequest(Msg);
//      subscribe(Msg);
//      // respMessage打印结果
//      String encryptMsg = "";
//      try {
//        //加密回复消息
//        encryptMsg = wxcpt.EncryptMsg(respMessage, timestamp, nonce);
//      } catch (AesException e) {
//        e.printStackTrace();
//      }
//      // 响应消息
//      PrintWriter out = this.getResponse().getWriter();
//      // out.print(encryptMsg);
//      out.close();
//    } else {
//
//    }
//  }
//  /**
//   * 处理微信发来的关注
//   *账号状态1关注2已冻结3取消关注4未关注
//   * @param request
//   */
//  public static void subscribe(String request) {
//    try {
//      // 调用parseXml方法解析请求消息
//      Map<String, String> requestMap = MessageUtil.parseXml(request);
//      // 发送方帐号
//      String FromUserName = requestMap.get("FromUserName");
//      Teacher Teacher = Teacher.dao.findFirst("SELECT * FROM Teacher WHERE userId=?", FromUserName);
//      Parent Parent = Parent.dao.findFirst("SELECT * FROM Parent WHERE userId=?", FromUserName);
//      if (requestMap.get("Event").equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
//        User user = WP.me.getUser(FromUserName);
//        if (Teacher != null) {
//          Teacher.set("sex", user.getGender())
//                  .set("picUrl", user.getAvatar())
//                  .set("state", 1)
//                  .update();
//        }
//        if (Parent != null) {
//          Parent.set("sex", user.getGender())
//                  .set("picUrl", user.getAvatar())
//                  .set("state", 1)
//                  .update();
//        }
//      }
//      if (requestMap.get("Event").equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
//        if (Teacher != null) {
//          Teacher.set("picUrl", "")
//                  .set("state", 3)
//                  .update();
//        }
//        if (Parent != null) {
//          Parent.set("picUrl", "")
//                  .set("state", 3)
//                  .update();
//        }
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//}
