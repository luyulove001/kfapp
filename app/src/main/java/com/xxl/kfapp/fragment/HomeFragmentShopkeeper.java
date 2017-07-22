package com.xxl.kfapp.fragment;

import android.os.Bundle;
import android.view.View;

import com.xxl.kfapp.R;
import com.xxl.kfapp.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by admin on 2017/7/22.
 */

public class HomeFragmentShopkeeper  extends BaseFragment implements View.OnClickListener {
    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initArgs(Bundle bundle) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.fragment_home_shopkeeper);
        ButterKnife.bind(this, mView);
    }

    @Override
    protected void initData() {

    }
}
