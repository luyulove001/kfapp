package com.xxl.kfapp.activity.home.boss;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.adapter.IncomeAdapter;
import com.xxl.kfapp.adapter.OrderAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.IncomeListVo;
import com.xxl.kfapp.model.response.OrderListVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.ListViewDecoration;
import com.xxl.kfapp.widget.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class IncomeListActivity extends BaseActivity implements SwipeRefreshLayout
        .OnRefreshListener {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.rv_income)
    RecyclerView rvIncome;
    @Bind(R.id.mRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void initArgs(Intent intent) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_income_list);
        ButterKnife.bind(this);
        mTitleBar.setBackOnclickListener(this);
        mTitleBar.setTitle("营业额明细");
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        getMemberIncomeDetailList(getIntent().getStringExtra("shopid"));
    }

    private void getMemberIncomeDetailList(String shopid) {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getMemberIncomeDetailList)
                .tag(this)
                .params("token", token)
                .params("shopid", shopid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            mRefreshLayout.setRefreshing(false);
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                IncomeListVo vo = mGson.fromJson(json.getString("data"), IncomeListVo.class);
                                initRvOrder(vo);
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
        IncomeAdapter adapter = new IncomeAdapter(vo.getRows());
        adapter.openLoadAnimation();
        rvIncome.setAdapter(adapter);
        rvIncome.addItemDecoration(new ListViewDecoration(R.drawable.divider_recycler));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        rvIncome.setLayoutManager(layoutManager);
    }

    @Override
    public void onRefresh() {
        getMemberIncomeDetailList(getIntent().getStringExtra("shopid"));
    }
}
