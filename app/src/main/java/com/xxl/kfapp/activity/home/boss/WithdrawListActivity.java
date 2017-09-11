package com.xxl.kfapp.activity.home.boss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.adapter.BalanceAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.base.BaseApplication;
import com.xxl.kfapp.model.response.BalanceListVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.ListViewDecoration;
import com.xxl.kfapp.widget.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WithdrawListActivity extends BaseActivity {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.rv_balance)
    RecyclerView rvBalance;
    @Bind(R.id.mRefreshLayout)
    EasyRefreshLayout mRefreshLayout;

    private boolean isRefresh = false;
    private BalanceAdapter adapter;

    @Override
    protected void initArgs(Intent intent) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_withdraw_list);
        ButterKnife.bind(this);
        mTitleBar.setTitle("余额");
        mTitleBar.setBackOnclickListener(this);
        initRefreshListener();
        rvBalance.addItemDecoration(new ListViewDecoration(R.drawable.divider_recycler));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        rvBalance.setLayoutManager(layoutManager);
    }

    private void initRefreshListener() {
        mRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
            }

            @Override
            public void onRefreshing() {
                isRefresh = true;
                getShopBalanceList();
            }
        });
        mRefreshLayout.setLoadMoreModel(LoadModel.NONE);
    }

    @Override
    protected void initData() {
        getShopBalanceList();
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

    private void getShopBalanceList() {
        String token = PreferenceUtils.getPrefString(BaseApplication.getContext(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getShopBalanceList)
                .tag(this)
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            mRefreshLayout.setRefreshing(false);
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                BalanceListVo balanceListVo = mGson.fromJson(json.getString("data"), BalanceListVo
                                        .class);
                                if (isRefresh) mRefreshLayout.refreshComplete();
                                if (balanceListVo != null && balanceListVo.getBalanceList().size() != 0) {
                                    if (isRefresh) {
                                        adapter.setNewData(balanceListVo.getBalanceList());
                                        isRefresh = false;
                                    } else {
                                        initRv(balanceListVo);
                                    }
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

    private void initRv(final BalanceListVo balanceListVo) {
        adapter = new BalanceAdapter(balanceListVo.getBalanceList());
        adapter.openLoadAnimation();
        adapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter
                .OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = getIntent().setClass(WithdrawListActivity.this, WithdrawActivity.class);
                intent.putExtra("shopid", balanceListVo.getBalanceList().get(i).getShopid());
                startActivity(intent);
            }
        });
        rvBalance.setAdapter(adapter);
    }
}
