package com.wts.util.msg.Resp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文本消息
 * Autohr Engineer.Jsp
 * Date 2014.10.08*/
public class TextMessage extends BaseMessage{
  // 回复的消息内容
  private String Content;

  public String getContent() {
    return Content;
  }

  public void setContent(String content) {
    Content = content;
  }


  /**
   * 判断是否是QQ表情
   *
   * @param content
   * @return
   */
  public static boolean getQQFace(String content) {
    boolean result = false;

    // 判断QQ表情的正则表达式
    String qqfaceRegex = "/::\\)|/::~|/::B|/::\\||/:8-\\)|/::<|/::\\$|/::X|/::Z|/::'\\(|/::-\\||/::@|/::P|/::D|/::O|/::\\(|/::\\+|/:--b|/::Q|/::T|/:,@P|/:,@-D|/::d|/:,@o|/::g|/:\\|-\\)|/::!|/::L|/::>|/::,@|/:,@f|/::-S|/:\\?|/:,@x|/:,@@|/::8|/:,@!|/:!!!|/:xx|/:bye|/:wipe|/:dig|/:handclap|/:&-\\(|/:B-\\)|/:<@|/:@>|/::-O|/:>-\\||/:P-\\(|/::'\\||/:X-\\)|/::\\*|/:@x|/:8\\*|/:pd|/:<W>|/:beer|/:basketb|/:oo|/:coffee|/:eat|/:pig|/:rose|/:fade|/:showlove|/:heart|/:break|/:cake|/:li|/:bome|/:kn|/:footb|/:ladybug|/:shit|/:moon|/:sun|/:gift|/:hug|/:strong|/:weak|/:share|/:v|/:@\\)|/:jj|/:@@|/:bad|/:lvu|/:no|/:ok|/:love|/:<L>|/:jump|/:shake|/:<O>|/:circle|/:kotow|/:turn|/:skip|/:oY|/:#-0|/:hiphot|/:kiss|/:<&|/:&>";
    Pattern p = Pattern.compile(qqfaceRegex);
    Matcher m = p.matcher(content);
    if (m.matches()) {
      result = true;
    }
    return result;
  }
  /**
   * emoji表情转换(hex -> utf-16)
   *
   * @param hexEmoji
   * @return
   * U+替换为0x（零X）
   */
  public static String emoji(int hexEmoji) {
    return String.valueOf(Character.toChars(hexEmoji));
  }

}
