package com.xxl.kfapp.utils;

public class Constant {
    public static final String WX_APPID = "wxed2ce4e7ea0609b0";
    public static final String ACTION_PAY_SUCCESS = "0";
    public static final String ACTION_PAY_FAIL = "1";
    public static final String ACTION_PAY_CANCEL = "2";

    /**
     * 在数字型字符串千分位加逗号
     * @param str
     * @return
     */
    public static String addComma(String str){
        boolean neg = false;
        if (str.startsWith("-")){  //处理负数
            str = str.substring(1);
            neg = true;
        }
        String tail = null;
        if (str.indexOf('.') != -1){ //处理小数点
            tail = str.substring(str.indexOf('.'));
            str = str.substring(0, str.indexOf('.'));
        }
        StringBuilder sb = new StringBuilder(str);
        sb.reverse();
        for (int i = 3; i < sb.length(); i += 4){
            sb.insert(i, ',');
        }
        sb.reverse();
        if (neg){
            sb.insert(0, '-');
        }
        if (tail != null){
            sb.append(tail);
        }
        return sb.toString();
    }
}
