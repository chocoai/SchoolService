package com.wts.common;

import java.util.Date;

public class MyTask implements Runnable{

    public void run() {
        System.out.println(new Date());
    }
}
