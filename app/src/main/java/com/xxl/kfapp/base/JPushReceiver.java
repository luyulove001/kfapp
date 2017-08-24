package com.xxl.kfapp.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.xxl.kfapp.activity.home.boss.RefundActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdFive3WebActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdSevenActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdSixActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdTwoActivity;
import com.xxl.kfapp.activity.home.register.RegisterKfsTwoActivity;
import com.xxl.kfapp.activity.person.NotificationCenterActivity;
import com.xxl.kfapp.utils.Constant;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
import talex.zsw.baselibrary.util.klog.KLog;

public class JPushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        KLog.d("[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + bundle.toString());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            KLog.d("[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            KLog.d("[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            KLog.d("[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            KLog.d("[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            String msg = bundle.getString(JPushInterface.EXTRA_ALERT) + "----";
            KLog.d("[MyReceiver] 接收到推送下来的通知的alert: " + msg);
            String params = bundle.getString(JPushInterface.EXTRA_EXTRA);
            KLog.d("[MyReceiver] 接收到推送下来的通知的  Extra: " + params);
            String TYPE = bundle.getString(JPushInterface.EXTRA_NOTI_TYPE);
            KLog.d("[MyReceiver] 接收到推送下来的通知的  TYPE: " + TYPE);
            //            String TYPE = bundle.getString(JPushInterface.EXTRA_);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            KLog.d("[MyReceiver] 用户点击打开了通知");
            //
            //          //打开自定义的Activity
            //          Intent i = new Intent(context, TestActivity.class);
            //          i.putExtras(bundle);
            //          //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //          i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            //          context.startActivity(i);
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            KLog.d("[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            KLog.w("[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            KLog.d("[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }


    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        JSONObject json = JSON.parseObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
        String type = json.getString("type");
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        switch (type) {
            case Constant.PUSH_TYPE_TICKET_BACK:
                i.setClass(context, RefundActivity.class);
                context.startActivity(i);
                break;
            case Constant.PUSH_TYPE_BOND_REFUND:
                break;
            case Constant.PUSH_TYPE_SHOP_APPLY:
                i.setClass(context, JmkdTwoActivity.class);
                context.startActivity(i);
                break;
            case Constant.PUSH_TYPE_SHOP_JINGPAI:
                i.setClass(context, JmkdFive3WebActivity.class);
                context.startActivity(i);
                break;
            case Constant.PUSH_TYPE_YUFUFEI_CHECK:
                i.setClass(context, JmkdSixActivity.class);
                context.startActivity(i);
                break;
            case Constant.PUSH_TYPE_SHEBEIFEI_CHECK:
                i.setClass(context, JmkdSevenActivity.class);
                context.startActivity(i);
                break;
            case Constant.PUSH_TYPE_CASH_APPLY:
                break;
            case Constant.PUSH_TYPE_ADD_STAFF:
                break;
            case Constant.PUSH_TYPE_SHOP_CLOSE:
                break;
            case Constant.PUSH_TYPE_SHOP_CHECK:
                break;
            case Constant.PUSH_TYPE_SHOP_TRANS:
                break;
            case Constant.PUSH_TYPE_BARBER_APPLY:
                i.setClass(context, RegisterKfsTwoActivity.class);
                context.startActivity(i);
                break;
        }
    }
}
