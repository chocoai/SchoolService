package com.wts.common;

import org.apache.log4j.Logger;

public class Test {
  private static Logger logger = Logger.getLogger(Test.class);


  public static void main(String[] args) {
    // System.out.println("nelson@126.com".matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$"));
    System.out.println("王".matches("^[\\u4e00-\\u9fa5]{2,}$"));
//    // 记录debug级别的信息
//    logger.debug("This is debug message.");
//    // 记录info级别的信息
//    logger.info("This is info message.");
//    // 记录error级别的信息
//    logger.error("This is error message.?");
  }

}
