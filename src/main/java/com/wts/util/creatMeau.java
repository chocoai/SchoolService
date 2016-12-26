package com.wts.util;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.model.Button;
import com.foxinmy.weixin4j.qy.WeixinProxy;
import com.foxinmy.weixin4j.type.ButtonType;

import java.util.ArrayList;
import java.util.List;

public class creatMeau {

  private static String D = "https://www.baidu.com";

  public static void main(String[] args) throws WeixinException {

    Button B1 = new Button("测试菜单1", D, ButtonType.view);
    Button B2 = new Button("测试菜单2", D, ButtonType.view);
    Button B3 = new Button("测试菜单3", D, ButtonType.view);
    Button B4 = new Button("测试菜单4", D, ButtonType.view);
    Button B5 = new Button("测试菜单5", D, ButtonType.view);

    Button A1 = new Button("测试菜单A", B1, B2, B3, B4, B5);
    Button A2 = new Button("测试菜单B", B1, B2, B3, B4, B5);
    Button A3 = new Button("测试菜单C", B1, B2, B3, B4, B5);

    List<Button> btns = new ArrayList<Button>();
    btns.add(A1);
    btns.add(A2);
    btns.add(A3);
    new WeixinProxy().createMenu(26, btns);
  }

}
