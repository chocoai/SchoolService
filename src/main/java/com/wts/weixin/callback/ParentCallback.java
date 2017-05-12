package com.wts.weixin.callback;

import com.jfinal.core.Controller;
import com.wts.weixin.config.TeacherConfig;
import com.wts.weixin.service.TeacherService;
import me.chanjar.weixin.cp.bean.WxCpXmlMessage;
import me.chanjar.weixin.cp.util.crypto.WxCpCryptUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import static com.wts.weixin.deal.ParentDeal.getOutMessage;

public class ParentCallback extends Controller {
  private static Logger logger = Logger.getLogger(ParentCallback.class);

  public void index() throws IOException {

    if (getRequest().getMethod() == "GET") {
      PrintWriter out = this.getResponse().getWriter();
      String result;
      if (!TeacherService.me().checkSignature(getPara("msg_signature"), getPara("timestamp"), getPara("nonce"), getPara("echostr"))) {
        result = "";
      } else {
        WxCpCryptUtil cryptUtil = new WxCpCryptUtil(TeacherConfig.me());
        result = cryptUtil.decrypt(getPara("echostr"));
      }
      out.print(result);
      out.close();
    } else if (getRequest().getMethod() == "POST") {
      this.getRequest().setCharacterEncoding("UTF-8");
      this.getResponse().setCharacterEncoding("UTF-8");
      InputStream inputStream = this.getRequest().getInputStream();
      WxCpXmlMessage inMessage = WxCpXmlMessage.fromEncryptedXml(inputStream, TeacherConfig.me(), getPara("timestamp"), getPara("nonce"), getPara("msg_signature"));
      if (getOutMessage(inMessage) != null) {
        this.getResponse().getWriter().write(getOutMessage(inMessage).toEncryptedXml(TeacherConfig.me()));
      }
    } else {
      logger.error("function:"+this.getClass().getSimpleName()+"/Index;"+"error:callback");

    }
    renderNull();
  }
}
