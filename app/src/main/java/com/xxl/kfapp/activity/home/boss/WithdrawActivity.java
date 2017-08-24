package com.xxl.kfapp.activity.home.boss;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.common.WebViewActivity;
import com.xxl.kfapp.adapter.CashApplyAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.base.BaseApplication;
import com.xxl.kfapp.model.response.AppConfigVo;
import com.xxl.kfapp.model.response.CashApplyVo;
import com.xxl.kfapp.model.response.CashRecordVo;
import com.xxl.kfapp.model.response.MemberInfoVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.ListViewDecoration;
import com.xxl.kfapp.widget.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.widget.CircleImageView;

public class WithdrawActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.civ_head)
    CircleImageView civHead;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.tv_balance)
    TextView tvBalance;
    @Bind(R.id.btn_apply)
    Button btnApply;
    @Bind(R.id.tv_total)
    TextView tvTotal;
    @Bind(R.id.tv_tixian)
    TextView tvTixian;
    @Bind(R.id.rv_cash_apply)
    RecyclerView rvCashApply;
    @Bind(R.id.lyt_wx_info)
    LinearLayout lytWxInfo;

    private String shopid, openid, wxNickname;
    private AppConfigVo appConfigVo;
    private boolean isWxBind;

    @Override
    protected void initArgs(Intent intent) {
        shopid = intent.getStringExtra("shopid");
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_withdrawal);
        ButterKnife.bind(this);
        mTitleBar.setTitle("余额详情");
        mTitleBar.setBackOnclickListener(this);
        mTitleBar.setRightTV("收支明细", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getIntent().setClass(WithdrawActivity.this, BalanceDetailActivity.class));
            }
        });
        tvTixian.setOnClickListener(this);
        btnApply.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        MemberInfoVo vo = (MemberInfoVo) mACache.getAsObject("memberInfoVo");
        if (TextUtils.isEmpty(vo.getOpenid())) {
            lytWxInfo.setVisibility(View.GONE);
            isWxBind = false;
        } else {
            Glide.with(getApplicationContext()).load(vo.getWxheadpic()).into(civHead);
            tvNickname.setText(vo.getWxnickname());
            openid = vo.getOpenid();
            wxNickname = vo.getWxnickname();
            isWxBind = true;
        }
        appConfigVo = (AppConfigVo) mACache.getAsObject("appConfig");
        getShopCashApplyInfo();
        getShopCashApplyRecord();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    private void getShopCashApplyRecord() {
        String token = PreferenceUtils.getPrefString(BaseApplication.getContext(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getShopCashApplyRecord)
                .tag(this)
                .params("token", token)
                .params("shopid", shopid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                CashRecordVo vo = mGson.fromJson(json.getString("data"), CashRecordVo.class);
                                if (vo != null && vo.getRows().size() != 0) {
                                    initRv(vo);
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

    private void initRv(final CashRecordVo vo) {
        CashApplyAdapter adapter = new CashApplyAdapter(vo.getRows());
        adapter.openLoadAnimation();
        rvCashApply.setAdapter(adapter);
        rvCashApply.addItemDecoration(new ListViewDecoration(R.drawable.divider_recycler));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        rvCashApply.setLayoutManager(layoutManager);
    }

    private void getShopCashApplyInfo() {
        String token = PreferenceUtils.getPrefString(BaseApplication.getContext(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getShopCashApplyInfo)
                .tag(this)
                .params("token", token)
                .params("shopid", shopid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                CashApplyVo vo = mGson.fromJson(json.getString("data"), CashApplyVo.class);
                                tvBalance.setText(vo.getBalance());
                                tvTotal.setText("¥" + vo.getTotal());
                            } else {
                                sweetDialog(json.getString("msg"), 1, false);
                            }
                        } catch (com.alibaba.fastjson.JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tixian:
                Intent intent = new Intent(WithdrawActivity.this, WebViewActivity.class);
                intent.putExtra("title", "提现说明");
                intent.putExtra("url", appConfigVo.getCashdesc());
                startActivity(intent);
                break;
            case R.id.btn_apply:
                if (isWxBind) {
                    createDialog();
                } else {
                    ToastShow("请先参考提现说明绑定微信");
                }
                break;
        }
    }

    private void createDialog() {
        final Dialog dialog = new Dialog(WithdrawActivity.this, R.style.Dialog);
        //设置它的ContentView
        dialog.setContentView(R.layout.dialog_cash);
        TextView confirm = (TextView) dialog.findViewById(R.id.dialog_button_ok);
        final EditText num = (EditText) dialog.findViewById(R.id.et_balance);
        num.setHint("本次最多提现" + appConfigVo.getMaxcashamount() + "元");
        TextView cashrate = (TextView) dialog.findViewById(R.id.tv_cashrate);
        cashrate.setText("微信等第三方平台手续费：" + appConfigVo.getCashrate() + "%");
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int input = Integer.parseInt(num.getText().toString());
                if (input > Float.parseFloat(tvBalance.getText().toString())) {
                    ToastShow("您输入的金额大于当前最大余额");
                } else if (input <= Integer.parseInt(appConfigVo.getMincashamount())) {
                    ToastShow("最少提现" + appConfigVo.getMincashamount() + "元");
                } else if (input > Integer.parseInt(appConfigVo.getMaxcashamount())) {
                    ToastShow("最大提现" + appConfigVo.getMaxcashamount() + "元");
                } else {
                    ToastShow("记得做提现啊/(ㄒoㄒ)/~~    insertShopCashApply");
                    insertShopCashApply(num.getText().toString());
                }
            }
        });
        TextView cancel = (TextView) dialog.findViewById(R.id.dialog_button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void insertShopCashApply(String amount) {
        String token = PreferenceUtils.getPrefString(BaseApplication.getContext(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.insertShopCashApply)
                .tag(this)
                .params("token", token)
                .params("shopid", shopid)
                .params("openid", openid)
                .params("wxnickname", wxNickname)
                .params("amount", amount)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                ToastShow(json.getString("msg"));
                                getShopCashApplyInfo();
                                getShopCashApplyRecord();
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
