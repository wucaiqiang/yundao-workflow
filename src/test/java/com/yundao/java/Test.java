package com.yundao.java;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.yundao.core.utils.UUIDUtils;

import java.io.IOException;

/**
 * Created by gjl on 2017/6/29.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        System.out.println(UUIDUtils.getUUID());
    }

}
