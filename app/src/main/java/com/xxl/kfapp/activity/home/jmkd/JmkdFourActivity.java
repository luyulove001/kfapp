package com.xxl.kfapp.activity.home.jmkd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xxl.kfapp.R;
import com.xxl.kfapp.adapter.ProgressAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.base.PayHandler;
import com.xxl.kfapp.model.response.ProgressVo;
import com.xxl.kfapp.utils.Constant;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.util.klog.KLog;

/**
 * 作者：XNN
 * 日期：2017/6/7
 * 作用：加盟开店第四步 品牌保证金
 */

public class JmkdFourActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.pRecyclerView)
    RecyclerView pRecyclerView;
    @Bind(R.id.next)
    TextView next;
    @Bind(R.id.tv_brandmoney)
    TextView tvBrand;
    @Bind(R.id.next2)
    Button next2;
    @Bind(R.id.lyt_wxpay)
    RelativeLayout lytWxPay;
    @Bind(R.id.lyt_alipay)
    RelativeLayout lytAlipay;
    @Bind(R.id.iv_wx_selected)
    ImageView ivWx;
    @Bind(R.id.iv_ali_selected)
    ImageView ivAli;
    @Bind(R.id.lyt_deposit)
    LinearLayout lytDeposit;
    @Bind(R.id.lyt_pay_success)
    RelativeLayout lytPaySuccess;

    private ProgressAdapter progressAdapter;
    private List<ProgressVo> progressVos;
    private String paytype, applyid;//支付方式   申请id
    private PayHandler payHandler;
    private IWXAPI api;
    private String brandmoney;


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constant.ACTION_PAY_SUCCESS:
                    paySuccess();
                    break;
                case Constant.ACTION_PAY_FAIL:
                    break;
                case Constant.ACTION_PAY_CANCEL:
                    break;
            }
        }
    };


    @Override
    protected void initArgs(Intent intent) {
        applyid = intent.getStringExtra("applyid");
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_jmkd_four);
        ButterKnife.bind(this);
        next.setOnClickListener(this);
        mTitleBar.setTitle("开店申请");
        mTitleBar.setBackOnclickListener(this);
        lytWxPay.setOnClickListener(this);
        lytAlipay.setOnClickListener(this);
        next2.setOnClickListener(this);
        next.setOnClickListener(this);
        next2.setEnabled(false);
    }

    @Override
    protected void initData() {
        initInfoRecycleView();
        brandmoney = PreferenceUtils.getPrefString(getApplication(), "brandmoney", "5000");
        if (TextUtils.isEmpty(applyid)) {
            applyid = PreferenceUtils.getPrefString(getApplication(), "applyid", "");
        }
        tvBrand.setText("保证金:" + brandmoney);
        //注册微信appid
        api = WXAPIFactory.createWXAPI(this, Constant.WX_APPID);
        api.registerApp(Constant.WX_APPID);
        //注册wxpay广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_PAY_CANCEL);
        filter.addAction(Constant.ACTION_PAY_FAIL);
        filter.addAction(Constant.ACTION_PAY_SUCCESS);
        registerReceiver(receiver, filter);
        //alipay 回调
        payHandler = new PayHandler() {
            @Override
            public void payResult(String resultStatus) {
                if (TextUtils.equals(resultStatus, "9000")) {
                    ToastShow("支付宝支付成功");
                    paySuccess();
                } else {
                    ToastShow("支付宝支付失败, 请重新支付");
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                if (TextUtils.isEmpty(paytype)) {
                    ToastShow("请选择支付方式");
                    return;
                } else {
                    doCreateUserOrder();
                }
                break;
            case R.id.lyt_wxpay:
                ivWx.setImageResource(R.mipmap.qq_red);
                ivAli.setImageResource(R.mipmap.qq_grey);
                paytype = "2";
                break;
            case R.id.lyt_alipay:
                ivAli.setImageResource(R.mipmap.qq_red);
                ivWx.setImageResource(R.mipmap.qq_grey);
                paytype = "1";
                break;
            case R.id.next2:
                startActivity(getIntent().setClass(JmkdFourActivity.this, JmkdFiveActivity.class));
                finish();
                break;
        }

    }

    /**
     * 初始化progress列表
     */
    private void initInfoRecycleView() {

        progressAdapter = new ProgressAdapter(new ArrayList<ProgressVo>(), getScrnWeight() / 4);
        pRecyclerView.setAdapter(progressAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        pRecyclerView.setLayoutManager(layoutManager);
        setData(1);
        //        pRecyclerView.smoothScrollToPosition();

    }

    private void setData(int payState) {
        progressVos = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            ProgressVo vo = new ProgressVo();
            if (i == 0) {
                vo.setName("申请开店");
                vo.setTag(2);
            } else if (i == 1) {
                vo.setName("审核");
                vo.setTag(2);
            } else if (i == 2) {
                vo.setName("阅读协议");
                vo.setTag(2);
            } else if (i == 3) {
                vo.setName("品牌保证金");
                vo.setTag(payState);
            } else if (i == 4) {
                vo.setName("选址");
            } else if (i == 5) {
                vo.setName("装修设备");
            } else if (i == 6) {
                vo.setName("加盟成功");
            }

            progressVos.add(vo);
        }
        progressAdapter.setNewData(progressVos);
    }

    private void paySuccess() {
        lytDeposit.setVisibility(View.GONE);
        lytPaySuccess.setVisibility(View.VISIBLE);
        next2.setEnabled(true);
        setData(2);
    }

    private void alipay(String info) {
        final String orderInfo = info;// 订单信息
        KLog.d("alipay", info);
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(JmkdFourActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = PayHandler.SDK_PAY_FLAG;
                msg.obj = result;
                payHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void wxPay(JSONObject json) {
        ToastShow("获取订单中...");
        try {
            if (null != json && !json.containsKey("retcode")) {
                PayReq req = new PayReq();
                req.appId = json.getString("appid");
                req.partnerId = json.getString("partnerid");
                req.prepayId = json.getString("prepayid");
                req.nonceStr = json.getString("noncestr");
                req.timeStamp = json.getString("timestamp");
                req.packageValue = json.getString("package");
                req.sign = json.getString("sign");
                req.extData = "app data"; // optional
                api.sendReq(req);
            } else if (null != json) {
                Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
                ToastShow("订单获取失败 " + json.getString("retmsg"));
            } else {
                ToastShow("请求失败,请检查您的网络");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //加盟保证金
    private void doCreateUserOrder() {
        String token = PreferenceUtils.getPrefString(getApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.createUserOrder)
                .params("token", token)
                .params("applyid", applyid)
                .params("quantity", "1")
                .params("price", brandmoney)
                .params("amount", brandmoney)
                .params("paytype", paytype)
                .params("ordertype", "1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                KLog.i(response.body());
                                // startActivity(new Intent(JmkdFourActivity.this, JmkdFiveActivity.class));
                                // finish();
                                if ("1".equals(paytype)) {
                                    alipay(json.getJSONObject("data").getString("orderstring"));
                                } else if ("2".equals(paytype)) {
                                    wxPay(json.getJSONObject("data"));
                                }
                            } else {
                                sweetDialog(json.getString("msg"), 1, false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
