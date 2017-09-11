package com.xxl.kfapp.activity.home.boss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ajguan.library.EasyRefreshLayout;
import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.adapter.IncomeAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.IncomeListVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.ListViewDecoration;
import com.xxl.kfapp.widget.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class IncomeListActivity extends BaseActivity {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.rv_income)
    RecyclerView rvIncome;
    @Bind(R.id.mRefreshLayout)
    EasyRefreshLayout mRefreshLayout;

    private int mPage = 1;
    private String shopid;
    private IncomeAdapter adapter;
    private boolean isRefresh = false, isLoad = false;

    @Override
    protected void initArgs(Intent intent) {
        shopid = intent.getStringExtra("shopid");
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_income_list);
        ButterKnife.bind(this);
        mTitleBar.setBackOnclickListener(this);
        mTitleBar.setTitle("营业额明细");
        rvIncome.addItemDecoration(new ListViewDecoration(R.drawable.divider_recycler));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        rvIncome.setLayoutManager(layoutManager);
        initRefreshListener();
    }

    private void initRefreshListener() {
        mRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                mPage +=1;
                isLoad = true;
                getMemberIncomeDetailList(shopid);
            }

            @Override
            public void onRefreshing() {
                mPage = 1;
                isRefresh = true;
                getMemberIncomeDetailList(shopid);
            }
        });
    }

    @Override
    protected void initData() {
        getMemberIncomeDetailList(shopid);
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

    private void getMemberIncomeDetailList(String shopid) {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getMemberIncomeDetailList)
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
                                IncomeListVo vo = mGson.fromJson(json.getString("data"), IncomeListVo.class);
                                if (isRefresh) {
                                    mRefreshLayout.refreshComplete();
                                    adapter.setNewData(vo.getRows());
                                    isRefresh = false;
                                } else if (isLoad) {
                                    mRefreshLayout.closeLoadView();
                                    int postion = adapter.getData().size();
                                    adapter.getData().addAll(vo.getRows());
                                    adapter.notifyDataSetChanged();
                                    rvIncome.scrollToPosition(postion);
                                    isLoad = false;
                                } else {
                                    initRvOrder(vo);
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

    private void initRvOrder(IncomeListVo vo) {
        adapter = new IncomeAdapter(vo.getRows());
        adapter.openLoadAnimation();
        rvIncome.setAdapter(adapter);
    }

}
