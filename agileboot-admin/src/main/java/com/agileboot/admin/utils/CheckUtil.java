package com.agileboot.admin.utils;

import java.util.Arrays;

/**
 * 微信token验证
 *
 * @author yuliang
 */
public class CheckUtil {

    private static final String TOKEN = "weixin45yuuereterter";

    public static boolean checkSignature(Signature sg) {

        String[] arr = new String[]{TOKEN, sg.getTimestamp(), sg.getNonce()};
        // 排序
        Arrays.sort(arr);
        // 生成字符串
        StringBuffer content = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        // sha1加密
        String temp = SecurityUtil.shaHash(content.toString());
        // 比较
        return temp.equals(sg.getSignature());
    }

}