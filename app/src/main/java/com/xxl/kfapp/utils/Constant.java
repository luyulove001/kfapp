package com.xxl.kfapp.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class Constant {
    public static final String WX_APPID = "wxed2ce4e7ea0609b0";
    public static final String ACTION_PAY_SUCCESS = "0";
    public static final String ACTION_PAY_FAIL = "1";
    public static final String ACTION_PAY_CANCEL = "2";
    /**
     * 推送类型
     */
    public static final String PUSH_TYPE_CASH_APPLY = "1000";   // 提现申请
    public static final String PUSH_TYPE_BOND_REFUND = "1001";  // 保证金退款
    public static final String PUSH_TYPE_TICKET_BACK = "2001";  // 申请退票
    public static final String PUSH_TYPE_ADD_STAFF = "3001";    // 添加工号

    public static final String PUSH_TYPE_BARBER_APPLY = "11";   // 快发师申请

    public static final String PUSH_TYPE_SHOP_APPLY = "21";      // 开店申请
    public static final String PUSH_TYPE_SHOP_JINGPAI = "24";    // 门店竞拍
    public static final String PUSH_TYPE_YUFUFEI_CHECK = "25";   // 开店预付费审核
    public static final String PUSH_TYPE_SHEBEIFEI_CHECK = "26"; // 开店设备费审核

    public static final String PUSH_TYPE_SHOP_CLOSE = "901";  // 关店申请
    public static final String PUSH_TYPE_SHOP_CHECK = "902";  // 转让申请
    public static final String PUSH_TYPE_SHOP_TRANS = "903";  // 转让接受

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

    // 取得版本号
    public static String GetVersion(Context context) {
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return manager.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "Unknown";
        }
    }
}
