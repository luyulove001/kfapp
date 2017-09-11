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

import com.ajguan.library.EasyRefreshLayout;
import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.adapter.RefundAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.RefundListVo;
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
    EasyRefreshLayout mRefreshLayout;
    @Bind(R.id.et_search_content)
    EditText etSearch;
    @Bind(R.id.btn_all)
    Button btnSearch;
    @Bind(R.id.tv_num)
    TextView tvNum;

    private int mPage = 1;
    private boolean isRefresh = false, isLoad = false;
    private RefundAdapter adapter;

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
        etSearch.setHint("请输入票号");
        rvTicket.addItemDecoration(new ListViewDecoration(R.drawable.divider_recycler_10px));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(false);
        rvTicket.setLayoutManager(layoutManager);
        initRefreshListener();
    }

    private void initRefreshListener() {
        mRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                mPage +=1;
                isLoad = true;
                getUserTicketBackList("", mPage);
            }

            @Override
            public void onRefreshing() {
                mPage = 1;
                isRefresh = true;
                getUserTicketBackList("", mPage);
            }
        });
    }

    @Override
    protected void initData() {
        getUserTicketBackList("", 1);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_all:
                getUserTicketBackList(etSearch.getText().toString(), 1);
                break;
        }
    }

    @Override
    public void onRefresh() {
        getUserTicketBackList("", 1);
    }

    private void getUserTicketBackList(String ticketno, int page) {
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
                                RefundListVo vo = mGson.fromJson(json.getString("data"), RefundListVo.class);
                                if (isRefresh) {
                                    mRefreshLayout.refreshComplete();
                                    adapter.setNewData(vo.getRows());
                                    isRefresh = false;
                                } else if (isLoad) {
                                    mRefreshLayout.closeLoadView();
                                    int postion = adapter.getData().size();
                                    adapter.getData().addAll(vo.getRows());
                                    adapter.notifyDataSetChanged();
                                    rvTicket.scrollToPosition(postion);
                                    isLoad = false;
                                } else {
                                    initRefundList(vo);
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

    private void initRefundList(final RefundListVo vo) {
        adapter = new RefundAdapter(vo.getRows());
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
    }
}
