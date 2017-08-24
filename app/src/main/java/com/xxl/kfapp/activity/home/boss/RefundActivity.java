package com.xxl.kfapp.activity.home.boss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.adapter.RefundAdapter;
import com.xxl.kfapp.adapter.TicketAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.RefundListVo;
import com.xxl.kfapp.model.response.TicketListVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.ListViewDecoration;
import com.xxl.kfapp.widget.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.util.klog.KLog;

public class RefundActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout
        .OnRefreshListener {
    @Bind(R.id.rv_ticket)
    RecyclerView rvTicket;
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.mRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    @Bind(R.id.et_search_content)
    EditText etSearch;
    @Bind(R.id.btn_all)
    Button btnSearch;
    @Bind(R.id.tv_num)
    TextView tvNum;

    @Override
    protected void initArgs(Intent intent) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_refund);
        ButterKnife.bind(this);
        tvNum.setVisibility(View.GONE);
        btnSearch.setOnClickListener(this);
        mTitleBar.setTitle("退票列表");
        mTitleBar.setBackOnclickListener(this);
        mRefreshLayout.setOnRefreshListener(this);
        etSearch.setHint("请输入票号");
    }

    @Override
    protected void initData() {
        getUserTicketBackList("", "");
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_all:
                getUserTicketBackList(etSearch.getText().toString(), "");
                break;
        }
    }

    @Override
    public void onRefresh() {
        getUserTicketBackList("", "");
    }

    private void getUserTicketBackList(String ticketno, String page) {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getUserTicketBackList)
                .tag(this)
                .params("token", token)
                .params("ticketno", ticketno)
                .params("page", page)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            mRefreshLayout.setRefreshing(false);
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                KLog.i(json.getString("msg"));
                                initRefundList(mGson.fromJson(json.getString("data"), RefundListVo.class));
                            } else {
                                sweetDialog(json.getString("msg"), 1, false);
                            }
                        } catch (com.alibaba.fastjson.JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initRefundList(final RefundListVo vo) {
        RefundAdapter adapter = new RefundAdapter(vo.getRows());
        adapter.openLoadAnimation();
        adapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter
                .OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = getIntent().setClass(RefundActivity.this, RefundDetailActivity.class);
                intent.putExtra("ticketno", vo.getRows().get(i).getTicketno());
                startActivity(intent);
            }
        });
        rvTicket.setAdapter(adapter);
        rvTicket.addItemDecoration(new ListViewDecoration(R.drawable.divider_recycler_10px));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(false);
        rvTicket.setLayoutManager(layoutManager);
    }
}
