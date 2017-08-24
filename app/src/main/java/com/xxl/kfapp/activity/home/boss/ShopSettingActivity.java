package com.xxl.kfapp.activity.home.boss;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.person.RenameActivity;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.widget.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShopSettingActivity extends BaseActivity {

    @Bind(R.id.tv_shopname)
    TextView tvShopName;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.lyt_shopname)
    LinearLayout llShopName;
    @Bind(R.id.lyt_price)
    LinearLayout llPrice;
    @Bind(R.id.lyt_open_time)
    LinearLayout llTime;
    @Bind(R.id.lyt_unbind_shop)
    LinearLayout llUnbindShop;

    private String startTime, endTime, shopName, price, shopid;

    private static final int ShopRename = 1;
    private static final int ShopPrice = 2;
    private static final int ShopBusiness = 3;

    @Override
    protected void initArgs(Intent intent) {
        intent = getIntent();
        startTime = intent.getStringExtra("startTime");
        endTime = intent.getStringExtra("endTime");
        shopName = intent.getStringExtra("shopName");
        price = intent.getStringExtra("price");
        shopid = intent.getStringExtra("shopid");
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_shop_setting);
        ButterKnife.bind(this);
        mTitleBar.setTitle("店铺设置");
        mTitleBar.setBackOnclickListener(this);

        tvShopName.setText(shopName);
        tvPrice.setText(price);
        tvTime.setText(startTime + "-" + endTime);

        llShopName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShopSettingActivity.this, RenameActivity.class);
                i.putExtra("shopName", shopName);
                i.putExtra("shopid", shopid);
                startActivityForResult(i, ShopRename);
            }
        });

        llPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShopSettingActivity.this, ShopPriceActivity.class);
                i.putExtra("price", price);
                i.putExtra("shopid", shopid);
                startActivityForResult(i, ShopPrice);
            }
        });

        llTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShopSettingActivity.this, ShopTimeSetActivity.class);
                i.putExtra("shopid", shopid);
                i.putExtra("starttime", startTime);
                i.putExtra("endtime", endTime);
                startActivityForResult(i, ShopBusiness);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ShopRename:
                    tvShopName.setText(data.getStringExtra("shopName"));
                    break;
                case ShopPrice:
                    tvPrice.setText(data.getStringExtra("price"));
                    break;
                case ShopBusiness:
                    tvTime.setText(data.getStringExtra("business"));
                    break;
            }
        }
    }
}
