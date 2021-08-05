package com.zhixun.javacode.proxy;

import com.zhixun.javacode.annotation.ActionListenerFor;

public class Worker implements People {
    @ActionListenerFor(source = "工人")
    @Override
    public String introduce() {
        return "工作、工作";
    }
}
