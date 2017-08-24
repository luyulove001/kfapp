package com.xxl.kfapp.activity.home.jmkd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.adapter.BidShopAdapter;
import com.xxl.kfapp.adapter.ProgressAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.BidShopListVo;
import com.xxl.kfapp.model.response.ProgressVo;
import com.xxl.kfapp.utils.AddressPickTask;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.ListViewDecoration;
import com.xxl.kfapp.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import talex.zsw.baselibrary.util.klog.KLog;

/**
 * 作用：加盟开店第5步 2 无地址竞拍地址
 */
public class JmkdFive2Activity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.pRecyclerView)
    RecyclerView pRecyclerView;
    @Bind(R.id.rv_bid_shop)
    RecyclerView rvBidShop;
    @Bind(R.id.mScrollView)
    ScrollView mScrollView;
    @Bind(R.id.next)
    Button next;
    @Bind(R.id.btn_addr_select)
    Button btnSelectAddr;
    @Bind(R.id.btn_all)
    Button btnAll;
    @Bind(R.id.tv_shop_num)
    TextView tvShopNum;

    private ProgressAdapter progressAdapter;
    private BidShopAdapter bidShopAdapter;
    private List<ProgressVo> progressVos;

    @Override
    protected void initArgs(Intent intent) {
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_jmkd_five2);
        ButterKnife.bind(this);
        next.setOnClickListener(this);
        mTitleBar.setTitle("开店申请");
        mTitleBar.setBackOnclickListener(this);
        btnSelectAddr.setOnClickListener(this);
        btnAll.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        initInfoRecycleView();
        doGetShopApplyInfo(PreferenceUtils.getPrefString(getApplication(), "applyid", ""));
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                break;
            case R.id.btn_addr_select:
                onAddressPicker();
                break;
            case R.id.btn_all:
                doGetAucteShopList("", "", "");
                break;
        }
    }

    public void onAddressPicker() {
        AddressPickTask task = new AddressPickTask(this);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                ToastShow("数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                if (county == null) {
                    ToastShow(province.getAreaName() + city.getAreaName());
                    doGetAucteShopList(province.getAreaId(), city.getAreaId(), "");
                } else {
                    ToastShow(province.getAreaName() + city.getAreaName() + county.getAreaName());
                    doGetAucteShopList(province.getAreaId(), city.getAreaId(), county.getAreaId());
                }
            }
        });
        task.execute("浙江", "杭州", "滨江");
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

    private void initShopList(final BidShopListVo vo) {
        bidShopAdapter = new BidShopAdapter(vo.getShoplst());
        bidShopAdapter.openLoadAnimation();
        bidShopAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter
                .OnRecyclerViewItemChildClickListener() {

            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = getIntent().setClass(JmkdFive2Activity.this, JmkdFive3WebActivity.class);
                intent.putExtra("shopid", vo.getShoplst().get(i).getShopid());
                startActivity(intent);
            }
        });
        rvBidShop.setAdapter(bidShopAdapter);
        rvBidShop.addItemDecoration(new ListViewDecoration(R.drawable.divider_recycler_10px));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        rvBidShop.setLayoutManager(layoutManager);

    }

    private void setData() {
        progressVos = new ArrayList<>();
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

    private void doGetShopApplyInfo(String applyid) {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getShopApplyInfo)
                .tag(this)
                .params("token", token)
                .params("applyid", applyid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                KLog.i(response.body());
                                json = json.getJSONObject("data");
                                doGetAucteShopList(json.getString("addprovincecode"),
                                        json.getString("addcitycode"), json.getString("addareacode"));
                            } else {
                                sweetDialog(json.getString("msg"), 1, false);
                            }
                        } catch (com.alibaba.fastjson.JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void doGetAucteShopList(String province, String city, String area) {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getAucteShopList)
                .tag(this)
                .params("token", token)
                .params("province", province)
                .params("city", city)
                .params("area", area)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                KLog.i(response.body());
                                BidShopListVo bidShopListVo = mGson.fromJson(json.getString("data"), BidShopListVo
                                        .class);
                                KLog.i(bidShopListVo.getShoplst().size());
                                tvShopNum.setText("共有" +
                                        bidShopListVo.getShoplst().size() + "家店铺供您选择");
                                initShopList(bidShopListVo);
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
