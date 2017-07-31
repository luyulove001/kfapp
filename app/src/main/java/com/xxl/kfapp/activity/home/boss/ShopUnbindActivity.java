package com.xxl.kfapp.activity.home.boss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xxl.kfapp.R;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.widget.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShopUnbindActivity extends BaseActivity {

    @Bind(R.id.btn_confirm)
    Button btnConfirm;
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.rg_way)
    RadioGroup rgWay;
    @Bind(R.id.rb_close_shop)
    RadioButton rbCloseShop;
    @Bind(R.id.rb_transfer)
    RadioButton rbTransfer;
    private String token;

    @Override
    protected void initArgs(Intent intent) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_shop_unbind);
        ButterKnife.bind(this);
        mTitleBar.setTitle("解绑店铺");
        mTitleBar.setBackOnclickListener(this);
    }

    @Override
    protected void initData() {
        token = PreferenceUtils.getPrefString(this.getApplicationContext(), "token", "1234567890");
    }


}
