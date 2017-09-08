package com.xxl.kfapp.activity.common;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.base.BaseApplication;
import com.xxl.kfapp.model.response.AppConfigVo;
import com.xxl.kfapp.utils.Constant;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FeedbackActivity extends BaseActivity {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.et_feedback)
    EditText etFeedback;

    @Override
    protected void initArgs(Intent intent) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        mTitleBar.setTitle("意见反馈");
        mTitleBar.setBackOnclickListener(this);
        mTitleBar.setRightTV("发送", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etFeedback.getText().toString())) {
                    ToastShow("反馈内容不能为空");
                } else {
                    feedback();
                }
            }
        });
        mTitleBar.getvTvRight().setTextColor(Color.parseColor("#fff"));
    }

    @Override
    protected void initData() {

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

    private void feedback() {
        String token = PreferenceUtils.getPrefString(BaseApplication.getContext(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.feedback)
                .tag(this)
                .params("token", token)
                .params("feedbackflg", "1")
                .params("content", etFeedback.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                ToastShow("发送成功");
                                finish();
                            } else {
                                sweetDialog(json.getString("msg"), 1, false);
                            }
                        } catch (com.alibaba.fastjson.JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
