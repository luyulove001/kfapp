package com.xxl.kfapp.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.xxl.kfapp.utils.PreferenceUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import talex.zsw.baselibrary.util.klog.KLog;


/**
 * 作者：xnn
 * 描述:
 */
public class BaseApplication extends Application {

    private List<Activity> activityList = new LinkedList<>();
    private boolean flag = false;

    private static final String TAG = "BaseApplication";
    private static Context mApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = this;
        //初始化KLog
        KLog.init(true);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        PreferenceUtils.setPrefBoolean(BaseApplication.getContext(), "isFirst", true);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public void exit() {
        exitApp();
        PreferenceUtils.setPrefBoolean(getContext(), "isFirst", true);
        System.exit(0);
    }

    public void exitApp() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        // 杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    // 获取ApplicationContext

    public static Context getContext() {
        return mApplicationContext;
    }
}
