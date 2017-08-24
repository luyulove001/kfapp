package com.xxl.kfapp.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.base.BaseApplication;
import com.xxl.kfapp.model.response.AppConfigVo;
import com.xxl.kfapp.utils.Md5Algorithm;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.util.klog.KLog;
import talex.zsw.baselibrary.view.SweetAlertDialog.SweetAlertDialog;

public class SplashActivity extends BaseActivity {
    @Bind(R.id.btn_skip)
    Button btnSkip;
    private String account, password, uuid;

    @Override
    protected void initArgs(Intent intent) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        account = PreferenceUtils.getPrefString(BaseApplication.getContext(), "account", "");
        password = PreferenceUtils.getPrefString(BaseApplication.getContext(), "password", "");
        uuid = PreferenceUtils.getPrefString(BaseApplication.getContext(), "uuid", "");
        getAppConfig();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(uuid)) {
                    doLogin();
                } else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    SplashActivity.this.startActivity(i);
                    SplashActivity.this.finish();
                }
            }
        }, 3000);
        StatService.start(this);
    }

    private void getAppConfig() {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getAppConfig)
                .tag(this)
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                KLog.i(response.body());
                                if (!TextUtils.isEmpty(json.getString("data"))) {
                                    AppConfigVo vo = mGson.fromJson(json.getString("data"), AppConfigVo.class);
                                    mACache.put("appConfig", vo);
                                }
                            } else {
                                sweetDialog(json.getString("msg"), 1, false);
                            }
                        } catch (com.alibaba.fastjson.JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void doLogin() {
        OkGo.<String>get(Urls.baseUrl + Urls.login)
                .tag(this)
                .params("logintype", "1")
                .params("account", account)
                .params("password", password)
                .params("mid", uuid)
                .params("sign", System.currentTimeMillis() / 1000 + "")
                .params("signdata", Md5Algorithm.signMD5("mid=" + uuid + "&sign="
                        + System.currentTimeMillis() / 1000 + ""))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                String token = json.getJSONObject("data").getString("token");
                                PreferenceUtils.setPrefString(getApplicationContext(), "token", token);
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                finish();
                            } else {
                                sweetDialog(json.getString("msg"), 1, false, new SweetAlertDialog
                                        .OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                                        SplashActivity.this.startActivity(i);
                                        SplashActivity.this.finish();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
