package com.wts.util;

public class ParamesAPI {
  // token
  public static String token = "weixin4j";
  // 随机戳
  public static String encodingAESKey = "vUiM1GBGtPzce8Jh3UikdsHvn3NMghUT9l9Fr8UarrT";
  //你的企业号ID
  public static String corpId = "wx947e7da41f395af7";
  // 管理组的凭证密钥，每个secret代表了对应用、通讯录、接口的不同权限；不同的管理组拥有不同的secret
  public static String secret = "";
  // OAuth2 回调地址
  public static String REDIRECT_URI = "";
  private static String C = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx947e7da41f395af7&redirect_uri=http%3a%2f%2fwts.s1.758kongbao.com%2fbind%2f&response_type=code&scope=snsapi_base&state=1#wechat_redirect";

}