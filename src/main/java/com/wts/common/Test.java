package com.wts.common;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.http.weixin.ApiResult;
import com.foxinmy.weixin4j.qy.model.User;
import com.wts.entity.WP;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Test {
  private static Logger logger = Logger.getLogger(Test.class);


  public static void main(String[] args) throws WeixinException {
    // System.out.println("nelson@126.com".matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$"));
    // System.out.println("王".matches("^[\\u4e00-\\u9fa5]{2,}$"));
//    // 记录debug级别的信息
//    logger.debug("This is debug message.");
//    // 记录info级别的信息
//    logger.info("This is info message.");
//    // 记录error级别的信息
//    logger.error("This is error message.?");

//    User user =new User("c","王王");
//    user.setPartyIds(1);
//    user.setWeixinId("chnwts");
    WP.me.inviteUser("chnwts","济南市育明小学欢迎您");
    //ApiResult ret = WP.me.createUser(user);
    //System.out.println(ret.getReturnMsg());




  }

}
