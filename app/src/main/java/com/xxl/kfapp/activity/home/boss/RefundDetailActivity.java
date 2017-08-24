package com.xxl.kfapp.activity.home.boss;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.util.klog.KLog;

public class RefundDetailActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.tv_ticketno)
    TextView tvTicketno;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_buytime)
    TextView tvBuytime;
    @Bind(R.id.tv_apply_time)
    TextView tvApplytime;
    @Bind(R.id.tv_reason)
    TextView tvReason;
    @Bind(R.id.tv_applysts)
    TextView tvApplySts;
    @Bind(R.id.btn_refuse)
    Button btnRefuse;
    @Bind(R.id.btn_agree)
    Button btnAgree;

    @Override
    protected void initArgs(Intent intent) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_refund_detail);
        ButterKnife.bind(this);
        mTitleBar.setBackOnclickListener(this);
        mTitleBar.setTitle("退票详情");
        btnAgree.setOnClickListener(this);
        btnRefuse.setOnClickListener(this);
        btnAgree.setVisibility(View.GONE);
        btnRefuse.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        getUserBackTicketDetailInfo(getIntent().getStringExtra("ticketno"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_refuse:
                updateTicketBackInfo(getIntent().getStringExtra("ticketno"), "3");
                finish();
                break;
            case R.id.btn_agree:
                updateTicketBackInfo(getIntent().getStringExtra("ticketno"), "2");
                finish();
                break;
        }
    }

    private void updateTicketBackInfo(String ticketno, String applysts) {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.updateTicketBackInfo)
                .tag(this)
                .params("token", token)
                .params("ticketno", ticketno)
                .params("applysts", applysts)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            //sweetDialog(json.getString("msg"), 1, false);
                            ToastShow(json.getString("msg"));
                        } catch (com.alibaba.fastjson.JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getUserBackTicketDetailInfo(String ticketno) {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getUserBackTicketDetailInfo)
                .tag(this)
                .params("token", token)
                .params("ticketno", ticketno)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                KLog.i(json.getString("msg"));
                                if (!TextUtils.isEmpty(json.getString("data"))) {
                                    json = json.getJSONObject("data");
                                    tvApplytime.setText("申请时间：" + json.getString("applytime"));
                                    tvBuytime.setText("购票时间：" + json.getString("buytime"));
                                    tvTicketno.setText("票号：" + json.getString("ticketno"));
                                    tvShopname.setText("店铺：" + json.getString("shopname"));
                                    tvPrice.setText("票价：" + json.getString("price"));
                                    tvNickname.setText("顾客：" + json.getString("nickname"));
                                    if (!TextUtils.isEmpty(json.getString("otherreason")))
                                        tvReason.setText(json.getString("backreason") + "，" + json.getString
                                                ("otherreason"));
                                    else tvReason.setText(json.getString("backreason"));
                                    if ("1".equals(json.getString("applysts"))) {
                                        tvApplySts.setText("等待退票");
                                        tvApplySts.setTextColor(Color.argb(255, 255, 153, 0));
                                        btnAgree.setVisibility(View.VISIBLE);
                                        btnRefuse.setVisibility(View.VISIBLE);
                                    } else if ("2".equals(json.getString("applysts"))) {
                                        tvApplySts.setText("同意退票");
                                        tvApplySts.setTextColor(Color.argb(255, 50, 205, 50));
                                    } else if ("3".equals(json.getString("applysts"))) {
                                        tvApplySts.setText("拒绝退票");
                                        tvApplySts.setTextColor(Color.argb(255, 177, 79, 69));
                                    }
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
}
