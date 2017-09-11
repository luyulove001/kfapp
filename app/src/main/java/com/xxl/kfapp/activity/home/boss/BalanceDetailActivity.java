package com.xxl.kfapp.activity.home.boss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ajguan.library.EasyRefreshLayout;
import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.adapter.IncomeDetailAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.base.BaseApplication;
import com.xxl.kfapp.model.response.IncomeVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.ListViewDecoration;
import com.xxl.kfapp.widget.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BalanceDetailActivity extends BaseActivity {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.rv_balance)
    RecyclerView rvBalance;
    @Bind(R.id.mRefreshLayout)
    EasyRefreshLayout mRefreshLayout;

    private String shopid;
    private int mPage = 1;
    private boolean isRefresh = false, isLoad = false;
    private IncomeDetailAdapter adapter;

    @Override
    protected void initArgs(Intent intent) {
        shopid = intent.getStringExtra("shopid");
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_withdraw_list);
        ButterKnife.bind(this);
        mTitleBar.setTitle("收支明细");
        mTitleBar.setBackOnclickListener(this);
        rvBalance.addItemDecoration(new ListViewDecoration(R.drawable.divider_recycler));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        rvBalance.setLayoutManager(layoutManager);
        initRefreshListener();
    }

    private void initRefreshListener() {
        mRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                mPage +=1;
                isLoad = true;
                getShopIncome(shopid);
            }

            @Override
            public void onRefreshing() {
                mPage = 1;
                isRefresh = true;
                getShopIncome(shopid);
            }
        });
    }

    @Override
    protected void initData() {
        getShopIncome(shopid);
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

    private void getShopIncome(String shopid) {
        String token = PreferenceUtils.getPrefString(BaseApplication.getContext(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getShopIncome)
                .tag(this)
                .params("token", token)
                .params("shopid", shopid)
                .params("page", mPage)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            mRefreshLayout.setRefreshing(false);
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                Gson gson = new Gson();
                                IncomeVo incomeVo = gson.fromJson(json.getString("data"), IncomeVo.class);
                                if (incomeVo != null && incomeVo.getRows().size() != 0) {
                                    if (isRefresh) {
                                        mRefreshLayout.refreshComplete();
                                        adapter.setNewData(incomeVo.getRows());
                                        isRefresh = false;
                                    } else if (isLoad) {
                                        mRefreshLayout.closeLoadView();
                                        int postion = adapter.getData().size();
                                        adapter.getData().addAll(incomeVo.getRows());
                                        adapter.notifyDataSetChanged();
                                        rvBalance.scrollToPosition(postion);
                                        isLoad = false;
                                    } else {
                                        initRv(incomeVo);
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

    private void initRv(final IncomeVo vo) {
        adapter = new IncomeDetailAdapter(vo.getRows());
        adapter.openLoadAnimation();
        rvBalance.setAdapter(adapter);
    }
}
