package com.xxl.kfapp.activity.home.boss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.home.jmkd.JmkdOneActivity;
import com.xxl.kfapp.adapter.BossShopAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.BossShopListVo;
import com.xxl.kfapp.utils.AddressPickTask;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.ListViewDecoration;
import com.xxl.kfapp.widget.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import talex.zsw.baselibrary.util.klog.KLog;

public class ShopListActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.rv_own_shop)
    RecyclerView rvOwnShop;
    @Bind(R.id.tv_num)
    TextView tvShopNum;
    @Bind(R.id.btn_all)
    Button btnAll;
    @Bind(R.id.btn_search_content)
    Button btnAddrSelect;

    @Override
    protected void initArgs(Intent intent) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_shop_list);
        ButterKnife.bind(this);
        mTitleBar.setTitle("门店列表");
        mTitleBar.setBackOnclickListener(this);
        mTitleBar.setRightIV(R.mipmap.qc_fast_add_write, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopListActivity.this, JmkdOneActivity.class));
            }
        });
        btnAddrSelect.setOnClickListener(this);
        btnAll.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        doGetBossShopList("", "", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search_content:
                onAddressPicker();
                break;
            case R.id.btn_all:
                doGetBossShopList("", "", "");
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
                    doGetBossShopList(province.getAreaId(), city.getAreaId(), "");
                } else {
                    ToastShow(province.getAreaName() + city.getAreaName() + county.getAreaName());
                    doGetBossShopList(province.getAreaId(), city.getAreaId(), county.getAreaId());
                }
            }
        });
        task.execute("浙江", "杭州", "滨江");
    }

    private void doGetBossShopList(String province, String city, String area) {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getBossShopList)
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
                                BossShopListVo bossShopListVo = mGson.fromJson(json.getString("data"), BossShopListVo.class);
                                tvShopNum.setText("您共有" + bossShopListVo.getShoplst().size() + "家店铺");
                                initShopList(bossShopListVo);
                            } else {
                                sweetDialog(json.getString("msg"), 1, false);
                            }
                        } catch (com.alibaba.fastjson.JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initShopList(final BossShopListVo vo) {
        BossShopAdapter bossShopAdapter = new BossShopAdapter(vo.getShoplst());
        bossShopAdapter.openLoadAnimation();
        bossShopAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter
                .OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = getIntent().setClass(ShopListActivity.this, ShopDetailActivity.class);
                intent.putExtra("shopid", vo.getShoplst().get(i).getShopid());
                startActivity(intent);
            }
        });
        rvOwnShop.setAdapter(bossShopAdapter);
        rvOwnShop.addItemDecoration(new ListViewDecoration(R.drawable.divider_recycler_10px));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        rvOwnShop.setLayoutManager(layoutManager);
    }
}
