package com.xxl.kfapp.activity.home.jmkd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.baidu.mobstat.StatService;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xxl.kfapp.R;
import com.xxl.kfapp.adapter.ProgressAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.base.BaseApplication;
import com.xxl.kfapp.base.PayHandler;
import com.xxl.kfapp.model.response.AppConfigVo;
import com.xxl.kfapp.model.response.FeeListVo;
import com.xxl.kfapp.model.response.MemberInfoVo;
import com.xxl.kfapp.model.response.ProgressVo;
import com.xxl.kfapp.model.response.SelectAddrstsInfoVo;
import com.xxl.kfapp.model.response.ShopApplyStatusVo;
import com.xxl.kfapp.utils.Constant;
import com.xxl.kfapp.utils.Md5Algorithm;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.SlideFromBottomPopup;
import com.xxl.kfapp.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;
import talex.zsw.baselibrary.util.klog.KLog;

/**
 * 竞拍webview
 */
public class JmkdFive3WebActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.pRecyclerView)
    RecyclerView pRecyclerView;
    @Bind(R.id.next)
    Button next;
    @Bind(R.id.web_bid)
    WebView webView;
    @Bind(R.id.tv_checking)
    TextView tvChecking;
    @Bind(R.id.tv_tips)
    TextView tvTips;
    @Bind(R.id.ll_btn)
    LinearLayout llBtn;
    @Bind(R.id.tv_fixedreason)
    TextView tvFixedReason;
    @Bind(R.id.tv_customreason)
    TextView tvCustomReason;
    @Bind(R.id.lyt_reason_shsb)
    LinearLayout lytReasonShsb;
    @Bind(R.id.ll_checking)
    LinearLayout llChecking;
    @Bind(R.id.btn_refresh)
    Button btnRefresh;
    private SlideFromBottomPopup mSlidePopup;

    private ProgressAdapter progressAdapter;
    private SelectAddrstsInfoVo infoVo;
    private String applyid, shopid;
    private int nowprice, amount;
    private ShopApplyStatusVo applyStatusVo;
    private FeeListVo feeListVo;
    //支付相关
    private PayHandler payHandler;
    private IWXAPI api;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constant.ACTION_PAY_SUCCESS:
                    doGetSelectAddrStsInfo(applyid, shopid);
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
        shopid = intent.getStringExtra("shopid");
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_jmkd_five3_web);
        ButterKnife.bind(this);
        next.setOnClickListener(this);
        mTitleBar.setTitle("开店申请");
        mTitleBar.setBackOnclickListener(this);
        initDrawables();
    }

    @Override
    protected void initData() {
        initInfoRecycleView();
        initWebView();
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
                    doGetSelectAddrStsInfo(applyid, shopid);
                } else {
                    ToastShow("支付宝支付失败, 请重新支付");
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
        getShopApplyStatus();
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true); // 设置支持javascript脚本
        webView.getSettings().setAllowFileAccess(true); // 允许访问文件
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");//设置默认为utf-8
        webView.getSettings().setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                if (infoVo != null) {
                    if ("3".equals(infoVo.getSalests())) {
                        if ("1".equals(infoVo.getBidsts())) {
                            //doUpdateApplyStatus(applyid);//到竞拍支付页面
                            //showBidSuccessPopup();
                            if ("1".equals(infoVo.getPrepaysts())) {
                                doUpdateApplyStatus();
                            } else {
                                if ("2".equals(infoVo.getPrepaychecksts())) {
                                    webView.setVisibility(View.VISIBLE);
                                    llChecking.setVisibility(View.GONE);
                                    webView.loadUrl(Urls.baseBidUrl + Urls.bidH5 + shopid);
                                    infoVo.setPrepaychecksts("0");
                                    tvTips.setVisibility(View.GONE);
                                    next.setText("竞拍成功，请付款");
                                    doUpdateShopViewCount(shopid);
                                } else {
                                    doGetShopFeeList();
                                }
                            }
                        } else {
                            startActivity(getIntent().setClass(JmkdFive3WebActivity.this, JmkdFive2Activity.class));
                            finish();
                        }
                    } else if ("2".equals(infoVo.getSalests())) {
                        if ("1".equals(infoVo.getBidmoneysts())) {
                            onOptionPicker(getBidStrs(nowprice));
                        } else {
                            showHeadPopup();
                        }
                    } else if ("1".equals(infoVo.getSalests())) {
                        if (!"1".equals(infoVo.getBidmoneysts())) {
                            showHeadPopup();
                        }
                    }
                }
                break;
            case R.id.btn_refresh:
                doGetSelectAddrStsInfo(applyid, shopid);
                break;
        }
    }

    /**
     * 初始化progress列表
     */
    private void initInfoRecycleView() {
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;

        progressAdapter = new ProgressAdapter(new ArrayList<ProgressVo>(), width / 4);
        progressAdapter.openLoadAnimation();
        pRecyclerView.setAdapter(progressAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        pRecyclerView.setLayoutManager(layoutManager);
        setData();
        pRecyclerView.smoothScrollToPosition(5);
    }

    private void setData() {
        List<ProgressVo> progressVos = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            ProgressVo vo = new ProgressVo();
            if (i == 0) {
                vo.setName("申请加盟");
                vo.setTag(2);
            } else if (i == 1) {
                vo.setName("审核");
                vo.setTag(2);
            } else if (i == 2) {
                vo.setName("阅读协议");
                vo.setTag(2);
            } else if (i == 3) {
                vo.setName("品牌保证金");
                vo.setTag(2);
            } else if (i == 4) {
                vo.setName("选址");
                vo.setTag(1);
            } else if (i == 5) {
                vo.setName("装修设备");
            } else if (i == 6) {
                vo.setName("加盟成功");
            }

            progressVos.add(vo);
        }
        progressAdapter.setNewData(progressVos);
    }

    /**
     * 设置支付方式弹出窗
     */
    private void showHeadPopup() {
        mSlidePopup = new SlideFromBottomPopup(this);
        mSlidePopup.setTexts(new String[]{"支付宝支付", "微信支付", "取消"});
        mSlidePopup.setOnItemClickListener(new SlideFromBottomPopup.OnItemClickListener() {
            @Override
            public void onItemClick(View v) {
                switch (v.getId()) {
                    case R.id.tx_1:
                        doCreateUserOrder("1");
                        mSlidePopup.dismiss();
                        break;
                    case R.id.tx_2:
                        doCreateUserOrder("2");
                        mSlidePopup.dismiss();
                        break;
                    case R.id.tx_3:
                        mSlidePopup.dismiss();
                        //                        Intent i = new Intent(JmkdFive3WebActivity.this,
                        // JmkdFivePrepayActivity.class);
                        //                        i.putExtra("shopid", shopid);
                        //                        startActivity(i);
                        break;
                }
            }
        });
        mSlidePopup.showPopupWindow();
    }

    /**
     * 设置支付方式弹出窗
     */
    private void showBidSuccessPopup() {
        mSlidePopup = new SlideFromBottomPopup(this);
        mSlidePopup.setTexts(new String[]{"支付宝支付", "微信支付", "线下转账"});
        mSlidePopup.setOnItemClickListener(new SlideFromBottomPopup.OnItemClickListener() {
            @Override
            public void onItemClick(View v) {
                switch (v.getId()) {
                    case R.id.tx_1:
                        doCreateUserOrder3("1");
                        mSlidePopup.dismiss();
                        break;
                    case R.id.tx_2:
                        doCreateUserOrder3("2");
                        mSlidePopup.dismiss();
                        break;
                    case R.id.tx_3:
                        mSlidePopup.dismiss();
                        Intent i = new Intent(JmkdFive3WebActivity.this, JmkdFivePrepayActivity.class);
                        i.putExtra("shopStatusVo", applyStatusVo);
                        startActivity(i);
                        break;
                }
            }
        });
        mSlidePopup.showPopupWindow();
    }

    public void onOptionPicker(String[] strs) {
        OptionPicker picker = new OptionPicker(this, strs);
        picker.setCanceledOnTouchOutside(false);
        picker.setDividerRatio(WheelView.DividerConfig.FILL);
        picker.setShadowColor(Color.RED, 40);
        picker.setSelectedIndex(0);
        picker.setCycleDisable(true);
        picker.setTextSize(14);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                doInsertPriceRecord(item);
            }
        });
        picker.show();
    }

    private String[] getBidStrs(int nowprice) {
        int addrange = Integer.valueOf(infoVo.getAddrange());
        String[] strs = new String[10];
        for (int i = 0; i < 10; i++) {
            nowprice += addrange;
            strs[i] = nowprice + "";
        }
        return strs;
    }

    private void doGetSelectAddrStsInfo(String applyid, final String shopid) {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getSelectAddrStsInfo)
                .tag(this)
                .params("token", token)
                .params("shopid", shopid)
                .params("applyid", applyid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                KLog.i(response.body());
                                infoVo = mGson.fromJson(json.getString("data"), SelectAddrstsInfoVo.class);
                                nowprice = Integer.valueOf(infoVo.getNowprice());
                                initButtonView(infoVo);
                                if ("1".equals(infoVo.getPrepaysts())) {
                                    selectAddrSuccess();
                                    webView.setVisibility(View.GONE);
                                } else {
                                    if (TextUtils.isEmpty(infoVo.getPrepaychecksts())) {
                                        tvChecking.setVisibility(View.GONE);
                                        webView.loadUrl(Urls.baseBidUrl + Urls.bidH5 + shopid);
                                        doUpdateShopViewCount(shopid);
                                    } else {
                                        webView.setVisibility(View.GONE);
                                        if ("0".equals(infoVo.getPrepaychecksts())) {
                                            AppConfigVo appConfig = (AppConfigVo) mACache.getAsObject("appConfig");
                                            tvChecking.setText("预付费凭证审核中，一般" + appConfig.getTranscheckdays()
                                                    + "个工作日，请耐心等待");
                                            llBtn.setVisibility(View.GONE);
                                            lytReasonShsb.setVisibility(View.GONE);
                                            tvChecking.setCompoundDrawablesRelative(ing, null, null, null);
                                            llChecking.setVisibility(View.VISIBLE);
                                        } else if ("2".equals(infoVo.getPrepaychecksts())) {
                                            llBtn.setVisibility(View.VISIBLE);
                                            tvChecking.setText("真遗憾，您的审核未通过");
                                            tvChecking.setCompoundDrawablesRelative(fair, null, null, null);
                                            lytReasonShsb.setVisibility(View.VISIBLE);
                                            if (!TextUtils.isEmpty(applyStatusVo.getPrepayreason())) {
                                                tvFixedReason.setText(applyStatusVo.getPrepayreason());
                                                tvFixedReason.setVisibility(View.VISIBLE);
                                            }
                                            //if (!TextUtils.isEmpty(applyStatusVo.getCustomreason()))
                                            //tvCustomReason.setText(applyStatusVo.getCustomreason());
                                            next.setText("重新上传");
                                            tvTips.setVisibility(View.VISIBLE);
                                        }
                                    }
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

    private void initButtonView(SelectAddrstsInfoVo infoVo) {
        if ("3".equals(infoVo.getSalests())) {//竞拍状态
            if ("1".equals(infoVo.getBidsts())) {
                if ("1".equals(infoVo.getPrepaysts())) {
                    next.setText("下一步");
                } else {
                    if ("2".equals(infoVo.getPrepaychecksts())) {
                        next.setText("重新上传");
                    } else {
                        next.setText("竞拍成功，请付款");
                    }
                }
            } else {
                next.setText("竞拍已结束");
            }
        } else if ("2".equals(infoVo.getSalests())) {
            if ("1".equals(infoVo.getBidmoneysts())) {//投标保证金缴纳状态
                next.setText("出价");
                btnRefresh.setVisibility(View.VISIBLE);
            } else {
                next.setText("交保证金报名");
            }
        } else if ("1".equals(infoVo.getSalests())) {
            if ("1".equals(infoVo.getBidmoneysts())) {//投标保证金缴纳状态
                next.setText("等待出价");
            } else {
                next.setText("交保证金报名");
            }
            next.setTextColor(Color.parseColor("#666666"));
            next.setBackgroundResource(R.drawable.bg_corner_gray);
            next.setClickable(false);
        }
    }

    private void selectAddrSuccess() {
        tvChecking.setText("选址成功，您离开店只有一步之遥了");
        tvChecking.setCompoundDrawablesRelative(pass, null, null, null);
    }

    private void doUpdateApplyStatus() {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        String uuid = PreferenceUtils.getPrefString(getAppApplication(), "uuid", "1");
        OkGo.<String>get(Urls.baseUrl + Urls.updateShopApplyStatus)
                .tag(this)
                .params("token", token)
                .params("applysts", "25")
                .params("applyid", applyid)
                .params("mid", uuid)
                .params("sign", System.currentTimeMillis() / 1000 + "")
                .params("signdata", Md5Algorithm.signMD5("mid=" + uuid + "&sign="
                        + System.currentTimeMillis() / 1000 + ""))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                KLog.i(response.body());
                                startActivity(getIntent().setClass(JmkdFive3WebActivity.this, JmkdSixActivity.class));
                            } else {
                                sweetDialog(json.getString("msg"), 1, false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //竞拍保证金
    private void doCreateUserOrder(final String paytype) {
        String token = PreferenceUtils.getPrefString(getApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.createUserOrder)
                .params("token", token)
                .params("applyid", applyid)
                .params("quantity", "1")
                .params("price", infoVo.getBond())
                .params("amount", infoVo.getBond())
                .params("paytype", paytype)
                .params("ordertype", "2")
                .params("shopid", shopid)
                .params("testflag", "1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                KLog.i(response.body());
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

    //开店预付费
    private void doCreateUserOrder3(final String paytype) {
        String token = PreferenceUtils.getPrefString(getApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.createUserOrder)
                .params("token", token)
                .params("applyid", applyid)
                .params("quantity", "1")
                .params("price", amount)
                .params("amount", amount)
                .params("paytype", paytype)
                .params("ordertype", "3")
                .params("fees", mGson.toJson(feeListVo.getFeelst()))
                .params("testflag", "1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                KLog.i(response.body());
                                if ("1".equals(paytype)) {
                                    alipay(json.getJSONObject("data").getString("orderstring"));
                                } else if ("2".equals(paytype)) {
                                    wxPay(json.getJSONObject("data"));
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

    private void doGetShopFeeList() {
        String token = PreferenceUtils.getPrefString(getApplicationContext(), "token", "1234567890");
        OkGo.<String>post(Urls.baseUrl + Urls.getShopFeeList)
                .tag(this)
                .params("token", token)
                .params("shopid", shopid)
                .params("applyid", applyid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            org.json.JSONObject json = new org.json.JSONObject(response.body());
                            String code = json.getString("code");
                            if (!code.equals("100000")) {
                                sweetDialog(json.getString("msg"), 1, false);
                            } else {
                                amount = 0;
                                feeListVo = mGson.fromJson(json.getString("data"), FeeListVo.class);
                                for (FeeListVo.Fee fee : feeListVo.getFeelst()) {
                                    amount += Integer.valueOf(fee.getAmount());
                                }
                                showBidSuccessPopup();
                            }
                        } catch (org.json.JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void alipay(String info) {
        final String orderInfo = info;// 订单信息
        KLog.d("alipay", info);
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(JmkdFive3WebActivity.this);
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

    private void doInsertPriceRecord(final String saleprice) {
        String token = PreferenceUtils.getPrefString(getApplication(), "token", "1234567890");
        MemberInfoVo vo = (MemberInfoVo) mACache.getAsObject("memberInfoVo");
        OkGo.<String>get(Urls.baseUrl + Urls.insertPriceRecord)
                .params("token", token)
                .params("nickname", vo.getNickname())
                .params("saleprice", saleprice)
                .params("shopid", shopid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                KLog.i(response.body());
                                json = json.getJSONObject("data");
                                if ("3".equals(json.getString("salests"))) {
                                    if ("1".equals(json.getString("bidsts"))) {
                                        ToastShow("恭喜竞拍成功， 请前往支付");
                                    } else if ("0".equals(json.getString("bidsts"))) {
                                        ToastShow("很遗憾竞拍失败，请重新选择竞拍");
                                    }
                                    //                                } else if ("2".equals(json.getString("salests")
                                    // )) {
                                    //                                    ToastShow("出价成功");
                                    //                                    nowprice = Integer.valueOf(saleprice);
                                } else {
                                    //                                    ToastShow("竞价还未开始，请稍后");
                                    ToastShow("出价成功");
                                    nowprice = Integer.valueOf(saleprice);
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

    /**
     * 线下转账，预付费
     */
    private void updateShopApplyInfo() {
        String token = PreferenceUtils.getPrefString(getApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.updateShopApplyInfo)
                .params("token", token)
                .params("applyid", applyid)
                .params("prepayway", "3")
                .params("prepay", amount)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                KLog.i(response.body());
                            } else {
                                sweetDialog(json.getString("msg"), 1, false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void doUpdateShopViewCount(String shopid) {
        String token = PreferenceUtils.getPrefString(getApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.updateShopViewCount)
                .params("token", token)
                .params("shopid", shopid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                KLog.i(response.body());
                            } else {
                                sweetDialog(json.getString("msg"), 1, false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getShopApplyStatus() {
        String token = PreferenceUtils.getPrefString(BaseApplication.getContext(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getShopApplyStatus)
                .tag(this)
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            org.json.JSONObject json = new org.json.JSONObject(response.body());
                            String code = json.getString("code");
                            if (!code.equals("100000")) {
                                sweetDialog(json.getString("msg"), 1, false);
                            } else {
                                KLog.i(response.body());
                                Gson gson = new Gson();
                                applyStatusVo = gson.fromJson(json.getString("data"), ShopApplyStatusVo.class);
                                if (!TextUtils.isEmpty(applyStatusVo.getShopid())) {
                                    shopid = applyStatusVo.getShopid();
                                }
                                applyid = applyStatusVo.getApplyid();
                                PreferenceUtils.setPrefString(BaseApplication.getContext(), "applyid", applyStatusVo
                                        .getApplyid());
                                doGetSelectAddrStsInfo(applyid, shopid);

                            }
                        } catch (org.json.JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private Drawable pass, fair, ing;

    @SuppressWarnings("deprecation")
    private void initDrawables() {
        pass = getResources().getDrawable(R.mipmap.sh_cg);
        fair = getResources().getDrawable(R.mipmap.sh_sb);
        ing = getResources().getDrawable(R.mipmap.sh_ing);
        pass.setBounds(0, 0, pass.getMinimumWidth(), pass.getMinimumHeight());
        fair.setBounds(0, 0, fair.getMinimumWidth(), fair.getMinimumHeight());
        ing.setBounds(0, 0, ing.getMinimumWidth(), ing.getMinimumHeight());
    }
}
