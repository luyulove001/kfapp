package com.xxl.kfapp.activity.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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

    String startTime,endTime,shopName,price;
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

    @Override
    protected void initArgs(Intent intent) {
        intent = getIntent();
        startTime = intent.getStringExtra("startTime");
        endTime = intent.getStringExtra("endTime");
        shopName = intent.getStringExtra("shopName");
        price = intent.getStringExtra("price");
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
                i.putExtra("shopName",shopName);
                startActivity(i);
            }
        });

        llPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShopSettingActivity.this, ShopPriceActivity.class);
                i.putExtra("price",price);
                startActivity(i);
            }
        });

    }

    @Override
    protected void initData() {

    }


}
