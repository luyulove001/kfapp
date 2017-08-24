package com.xxl.kfapp.activity.home.boss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.adapter.TextAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.TextVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.TitleBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShopDetailActivity extends BaseActivity {

    @Bind(R.id.iv_shop)
    ImageView ivShop;
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.tv_shopname)
    TextView tvShopName;
    @Bind(R.id.tv_shopkeeper)
    TextView tvShopKeeper;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.tv_start_business)
    TextView tvStartBusiness;
    @Bind(R.id.tv_open_time)
    TextView tvOpenTime;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_sn)
    TextView tvSn;
    @Bind(R.id.tv_sn1)
    TextView tvSn1;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.tv_staffcnt)
    TextView tvStaffcnt;
    @Bind(R.id.rv_barber)
    RecyclerView rvBarber;
    private TextAdapter txtAdapter;

    private String token;
    String startTime, endTime, shopName, price;

    @Override
    protected void initArgs(Intent intent) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_shop_detail);
        ButterKnife.bind(this);
        mTitleBar.setTitle("店铺详情");
        mTitleBar.setBackOnclickListener(this);
        mTitleBar.setRightIV2(R.mipmap.qc_fast_set_write, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIntent();
            }
        });
        initTextRV();
    }

    private void setIntent() {
        Intent intent = getIntent().setClass(this, ShopSettingActivity.class);
        intent.putExtra("shopName", shopName);
        intent.putExtra("startTime", startTime);
        intent.putExtra("endTime", endTime);
        intent.putExtra("price", price);
        startActivity(intent);
    }

    @Override
    protected void initData() {
        token = PreferenceUtils.getPrefString(this.getApplicationContext(), "token", "1234567890");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDetail();
    }

    private void initTextRV() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        rvBarber.setLayoutManager(layoutManager);
    }

    /**
     * 获取详情
     */
    private void getDetail() {
        OkGo.<String>get(Urls.baseUrl + Urls.getBossShopDetailInfo)
                .tag(this)
                .params("token", token)
                .params("shopid", getIntent().getStringExtra("shopid"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                Glide.with(ShopDetailActivity.this).load(json.getJSONObject("data").getString
                                        ("shoppic")).into
                                        (ivShop);
                                shopName = json.getJSONObject("data").getString("shopname");//名称
                                String shopNo = json.getJSONObject("data").getString("shopno");//编号
                                tvShopName.setText(shopName + "  No." + shopNo);
                                tvShopKeeper.setText("店主:" + json.getJSONObject("data").getString("nickname"));
                                String address = json.getJSONObject("data").getString("address");
                                if (TextUtils.isEmpty(address) || "null".equals(address))
                                    tvAddress.setVisibility(View.GONE);
                                else tvAddress.setText(address);

                                tvStartBusiness.setText("开业时间:" + json.getJSONObject("data").getString("starttime"));
                                startTime = json.getJSONObject("data").getString("begintime");
                                endTime = json.getJSONObject("data").getString("endtime");
                                if ("null".equals(startTime)) startTime = "08:00";
                                if ("null".equals(endTime)) endTime = "20:00";
                                tvOpenTime.setText("今日营业时间：" + startTime + " -" + endTime);
                                price = json.getJSONObject("data").getString("nowprice");
                                tvPrice.setText("今日票价：" + price + "元");
                                tvSn1.setText(json.getJSONObject("data").getString("sn"));
                                tvSn.setText(json.getJSONObject("data").getString("sn"));
                                String onlinests = json.getJSONObject("data").getString("onlinests");
                                if (onlinests.equals("1")) {
                                    tvStatus.setText("在线");
                                } else {
                                    tvStatus.setText("不在线");
                                }
                                String staffcnt = json.getJSONObject("data").getString("staffcnt");
                                tvStaffcnt.setText("理发师 " + staffcnt + " 人");

                                JSONArray stafflst = json.getJSONObject("data").getJSONArray("stafflst");
                                List<TextVo> textVos = new ArrayList<>();
                                TextVo textVo = new TextVo();
                                textVo.setTxt1("工号");
                                textVo.setTxt2("姓名");
                                textVos.add(textVo);
                                for (int i = 0; i < stafflst.length(); i++) {
                                    textVo = new TextVo();
                                    textVo.setTxt1(stafflst.getJSONObject(i).getString("staffno"));
                                    textVo.setTxt2(stafflst.getJSONObject(i).getString("nickname"));
                                    textVos.add(textVo);
                                }
                                txtAdapter = new TextAdapter(textVos);
                                rvBarber.setAdapter(txtAdapter);
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
