package com.xxl.kfapp.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.xxl.kfapp.R;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.base.BaseApplication;
import com.xxl.kfapp.model.response.AppConfigVo;
import com.xxl.kfapp.utils.Constant;
import com.xxl.kfapp.widget.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutUsActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.rl_connect)
    RelativeLayout rlConnect;
    @Bind(R.id.rl_user)
    RelativeLayout rlUser;
    @Bind(R.id.rl_feedback)
    RelativeLayout rlFeedback;
    @Bind(R.id.tv_version)
    TextView tvVersion;
    @Bind(R.id.tv_about)
    TextView tvAbout;

    private AppConfigVo appConfigVo;

    @Override
    protected void initArgs(Intent intent) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        mTitleBar.setTitle("关于我们");
        mTitleBar.setBackOnclickListener(this);
        rlConnect.setOnClickListener(this);
        rlFeedback.setOnClickListener(this);
        rlUser.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        appConfigVo = (AppConfigVo) mACache.getAsObject("appConfig");
        tvAbout.setText(appConfigVo.getAboutus());
        tvVersion.setText("QC family " + Constant.GetVersion(BaseApplication.getContext()));
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent();
        switch (v.getId()) {
            case R.id.rl_connect:
                i.setClass(this, WebViewActivity.class);
                i.putExtra("url", appConfigVo.getContactus());
                i.putExtra("title", "联系我们");
                startActivity(i);
                break;
            case R.id.rl_user:
                i.setClass(this, WebViewActivity.class);
                i.putExtra("url", appConfigVo.getRegprotocol());
                i.putExtra("title", "用户协议");
                startActivity(i);
                break;
            case R.id.rl_feedback:
                i.setClass(this, FeedbackActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }
}
