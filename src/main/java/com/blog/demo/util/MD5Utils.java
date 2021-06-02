package com.blog.demo.util;

import org.springframework.util.DigestUtils;

import java.security.MessageDigest;

public class MD5Utils {

    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            resultString = DigestUtils.md5DigestAsHex(origin.getBytes("utf-8"));

        }catch (Exception e){
            e.printStackTrace();
        }
        return resultString;
    }
}
