package com.xxl.kfapp.fragment;

import android.os.Bundle;

import com.baidu.mobstat.StatService;
import com.xxl.kfapp.R;
import com.xxl.kfapp.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * 作者：XNN
 * 日期：2017/6/1
 * 作用：购物车
 */

public class SPcartFragment extends BaseFragment {
    @Override
    protected void initArgs(Bundle bundle) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.fragment_spcart);
        ButterKnife.bind(this, mView);

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
