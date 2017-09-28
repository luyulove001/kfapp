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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.baidu.mobstat.StatService;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.common.WebViewActivity;
import com.xxl.kfapp.activity.person.ModifyAddrActivity;
import com.xxl.kfapp.adapter.GoodsMoreAdapter;
import com.xxl.kfapp.adapter.GoodsOneAdapter;
import com.xxl.kfapp.adapter.ProgressAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.base.PayHandler;
import com.xxl.kfapp.model.request.GoodsVo;
import com.xxl.kfapp.model.response.AddrVo;
import com.xxl.kfapp.model.response.MemberInfoVo;
import com.xxl.kfapp.model.response.ProgressVo;
import com.xxl.kfapp.model.response.ShopGoodListVo;
import com.xxl.kfapp.utils.Constant;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.ListViewDecoration;
import com.xxl.kfapp.widget.SlideFromBottomPopup;
import com.xxl.kfapp.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.util.klog.KLog;

/**
 * 作用：加盟开店第六步 购买设备
 */
public class JmkdSixActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.pRecyclerView)
    RecyclerView pRecyclerView;
    @Bind(R.id.rv_goods_more)
    RecyclerView rvGoodsMore;
    @Bind(R.id.rv_goods_one)
    RecyclerView rvGoodsOne;
    @Bind(R.id.mScrollView)
    ScrollView mScrollView;
    @Bind(R.id.next2)
    TextView next;
    @Bind(R.id.iv_less)
    ImageView ivLess;
    @Bind(R.id.iv_more)
    ImageView ivMore;
    @Bind(R.id.tv_goods_number)
    TextView tvGoodsNumber;
    @Bind(R.id.tv_goods_amount)
    TextView tvGoodsAmount;
    @Bind(R.id.tv_recipient)
    TextView tvRecipient;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.ll_receipt)
    RelativeLayout llReceipt;
    private SlideFromBottomPopup mSlidePopup;

    private ProgressAdapter progressAdapter;
    private List<ProgressVo> progressVos;
    private GoodsOneAdapter oneAdapter;
    private GoodsMoreAdapter moreAdapter;
//    private boolean[] isSelects = new boolean[]{false, false};
    private int goodsNum = 1, amount = 0;
    private ShopGoodListVo vo;
    //支付相关
    private PayHandler payHandler;
    private IWXAPI api;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constant.ACTION_PAY_SUCCESS:
                    startActivity(new Intent(JmkdSixActivity.this, JmkdSevenActivity.class));
                    finish();
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

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_jmkd_six);
        ButterKnife.bind(this);
        next.setOnClickListener(this);
        mTitleBar.setTitle("开店申请");
        mTitleBar.setBackOnclickListener(this);
        ivLess.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        llReceipt.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        initInfoRecycleView();
        doGetShopGoodsList();
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
                    startActivity(new Intent(JmkdSixActivity.this, JmkdSevenActivity.class));
                    finish();
                } else {
                    ToastShow("支付宝支付失败, 请重新支付");
                }
            }
        };
        setMemberAddressInfo();
    }

    private void setMemberAddressInfo() {
        MemberInfoVo memberInfoVo = (MemberInfoVo) mACache.getAsObject("memberInfoVo");
        tvRecipient.setText("收货人：" + memberInfoVo.getNickname());
        tvAddress.setText("收货地址：" + memberInfoVo.getDispaddress());
        tvPhone.setText(memberInfoVo.getPhone());
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next2:
                showHeadPopup();
                break;
            case R.id.iv_less:
                goodsNum -= 1;
                if (goodsNum < 1) goodsNum = 1;
                tvGoodsNumber.setText(goodsNum + "");
                if (vo != null) {
                    vo = setMoreListNum(vo, goodsNum);
                    moreAdapter.notifyDataSetChanged();
                }
                setGoodsAmount();
                break;
            case R.id.iv_more:
                goodsNum += 1;
                tvGoodsNumber.setText(goodsNum + "");
                if (vo != null) {
                    vo = setMoreListNum(vo, goodsNum);
                    moreAdapter.notifyDataSetChanged();
                }
                setGoodsAmount();
                break;
            case R.id.ll_receipt:
                Intent i = new Intent(JmkdSixActivity.this, ModifyAddrActivity.class);
                i.putExtra("unset", true);
                startActivityForResult(i, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            AddrVo vo = (AddrVo) data.getSerializableExtra("address");
            tvRecipient.setText("收货人：" + vo.getUsername());
            tvAddress.setText("收货地址：" + vo.getDispaddress());
            tvPhone.setText(vo.getPhone());
        }
    }

    /**
     * 设置支付方式弹出窗
     */
    private void showHeadPopup() {
        mSlidePopup = new SlideFromBottomPopup(this);
        mSlidePopup.setTexts(new String[]{"支付宝支付", "微信支付", "线下转账"});
        mSlidePopup.setOnItemClickListener(new SlideFromBottomPopup.OnItemClickListener() {
            @Override
            public void onItemClick(View v) {
                switch (v.getId()) {
                    case R.id.tx_1:
                        doCreateUserOrder("1");
                        mSlidePopup.dismiss();
                        showDialog();
                        break;
                    case R.id.tx_2:
                        doCreateUserOrder("2");
                        mSlidePopup.dismiss();
                        showDialog();
                        break;
                    case R.id.tx_3:
                        mSlidePopup.dismiss();
                        //                        updateShopApplyInfo();
                        Intent i = new Intent(JmkdSixActivity.this, JmkdSixDeviceActivity.class);
                        //                        i.putExtra("shopid", shopid);
                        i.putExtra("amount", amount);
                        i.putExtra("phone", tvPhone.getText().toString());
                        i.putExtra("nickname", tvRecipient.getText().toString());
                        i.putExtra("address", tvAddress.getText().toString());
                        startActivity(i);
                        break;
                }
            }
        });
        mSlidePopup.showPopupWindow();
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

    private void initGoodsList() {
        oneAdapter = new GoodsOneAdapter(vo.getOnelst());
        oneAdapter.openLoadAnimation();
        oneAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter
                .OnRecyclerViewItemChildClickListener() {

            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = getIntent().setClass(JmkdSixActivity.this, WebViewActivity.class);
                intent.putExtra("url", Urls.device + vo.getOnelst().get(i).getGid());
                intent.putExtra("title", "商品详情");
                startActivity(intent);
            }
        });

        rvGoodsOne.setAdapter(oneAdapter);
        rvGoodsOne.addItemDecoration(new ListViewDecoration(R.drawable.divider_recycler_10px));

        moreAdapter = new GoodsMoreAdapter(vo.getMorelst());
        moreAdapter.openLoadAnimation();
        moreAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter
                .OnRecyclerViewItemChildClickListener() {

            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = getIntent().setClass(JmkdSixActivity.this, WebViewActivity.class);
                intent.putExtra("url", Urls.device + vo.getMorelst().get(i).getGid());
                intent.putExtra("title", "商品详情");
                startActivity(intent);
            }
        });
        rvGoodsMore.setAdapter(moreAdapter);
        rvGoodsMore.addItemDecoration(new ListViewDecoration(R.drawable.divider_recycler));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        rvGoodsOne.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager1.setSmoothScrollbarEnabled(true);
        layoutManager1.setAutoMeasureEnabled(true);
        rvGoodsMore.setLayoutManager(layoutManager1);
        setGoodsAmount();
    }

    private void setData() {
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
                vo.setTag(2);
            } else if (i == 4) {
                vo.setName("选址");
                vo.setTag(2);
            } else if (i == 5) {
                vo.setName("装修设备");
                vo.setTag(1);
            } else if (i == 6) {
                vo.setName("加盟成功");
            }

            progressVos.add(vo);
        }
        progressAdapter.setNewData(progressVos);
    }

    private void doGetShopGoodsList() {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getShopGoodsList)
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
                                vo = mGson.fromJson(json.getString("data"), ShopGoodListVo.class);
                                vo = setMoreListNum(vo, 1);
                                initGoodsList();
                            } else {
                                sweetDialog(json.getString("msg"), 1, false);
                            }
                        } catch (com.alibaba.fastjson.JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 设备购买
     *
     * @param paytype 支付类型
     */
    private void doCreateUserOrder(final String paytype) {
        String token = PreferenceUtils.getPrefString(getApplication(), "token", "1234567890");
        String applyid = PreferenceUtils.getPrefString(getApplication(), "applyid", "0");
        OkGo.<String>get(Urls.baseUrl + Urls.createUserOrder)
                .params("token", token)
                .params("applyid", applyid)
                .params("quantity", "1")
                .params("price", amount + "")
                .params("amount", amount + "")
                .params("paytype", paytype)
                .params("ordertype", "4")
                .params("goods", getGoodsList())
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

    private String getGoodsList() {
        List<GoodsVo> goodsVos = new ArrayList<>();
        GoodsVo goodsVo;
        for (int i = 0; i < vo.getMorelst().size(); i++) {
            goodsVo = new GoodsVo();
            goodsVo.setGoodsid(i + 1 + "");
            goodsVo.setQuantity(goodsNum + "");
            goodsVos.add(goodsVo);
        }
        for (int i = 0; i < vo.getOnelst().size(); i++) {
            goodsVo = new GoodsVo();
            goodsVo.setGoodsid(i + 3 + "");
//            if (isSelects[i]) {
//                goodsVo.setQuantity("1");
//            } else {
//                goodsVo.setQuantity("0");
//            }
            goodsVos.add(goodsVo);
        }
        return mGson.toJson(goodsVos);
    }

    private void alipay(String info) {
        final String orderInfo = info;// 订单信息
        KLog.d("alipay", info);
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(JmkdSixActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = PayHandler.SDK_PAY_FLAG;
                msg.obj = result;
                payHandler.sendMessage(msg);
                closeDialog();
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
            closeDialog();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ShopGoodListVo setMoreListNum(ShopGoodListVo vo, int num) {
        for (int i = 0; i < vo.getMorelst().size(); i++) {
            if ("1".equals(vo.getMorelst().get(i).getYdflag()) && num > 1) {
                vo.getMorelst().get(i).setNum(num - 1);
            } else {
                vo.getMorelst().get(i).setNum(num);
            }
        }
        return vo;
    }

    private void setGoodsAmount() {
        amount = 0;
        List<ShopGoodListVo.GoodsInfo> infos = vo.getMorelst();
        List<ShopGoodListVo.GoodsInfo> onelst = vo.getOnelst();
        for (int i = 0; i < infos.size(); i++) {
            amount += infos.get(i).getNum() * infos.get(i).getPrice();
        }
        for (int i = 0; i < onelst.size(); i++) {
            amount += onelst.get(i).getPrice();
        }
        tvGoodsAmount.setText("¥" + Constant.addComma(amount + ""));
    }
}
