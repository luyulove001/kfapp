package com.xxl.kfapp.activity.person;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.home.boss.RefundActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdFive3WebActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdSevenActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdSixActivity;
import com.xxl.kfapp.adapter.MessageAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.MessageListVo;
import com.xxl.kfapp.utils.Constant;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.ListViewDecoration;
import com.xxl.kfapp.widget.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.util.klog.KLog;

public class NotificationCenterActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.rv_message)
    RecyclerView rvMessage;
    @Bind(R.id.mRefreshLayout)
    EasyRefreshLayout mRefreshLayout;
    private MessageListVo messageListVo;
    private MessageAdapter adapter;
    private int mPage = 1;
    private boolean isRefresh = false, isLoad = false;

    @Override
    protected void initArgs(Intent intent) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_notification_center);
        ButterKnife.bind(NotificationCenterActivity.this);
        mTitleBar.setBackOnclickListener(NotificationCenterActivity.this);
        mTitleBar.setTitle("通知中心");
        rvMessage.addItemDecoration(new ListViewDecoration(R.drawable.divider_recycler));
        LinearLayoutManager layoutManager = new LinearLayoutManager(NotificationCenterActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        rvMessage.setLayoutManager(layoutManager);
        initRefreshListener();
    }

    private void initRefreshListener() {
        mRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                mPage +=1;
                isLoad = true;
                getUserNoticeList();
            }

            @Override
            public void onRefreshing() {
                mPage = 1;
                isRefresh = true;
                getUserNoticeList();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserNoticeList();
        StatService.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    private void getUserNoticeList() {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getUserNoticeList)
                .tag(NotificationCenterActivity.this)
                .params("token", token)
                .params("page", mPage)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                messageListVo = mGson.fromJson(json.getString("data"), MessageListVo.class);
                                if (isRefresh) {
                                    mRefreshLayout.refreshComplete();
                                    adapter.setNewData(messageListVo.getRows());
                                    isRefresh = false;
                                } else if (isLoad) {
                                    mRefreshLayout.closeLoadView();
                                    int postion = adapter.getData().size();
                                    adapter.getData().addAll(messageListVo.getRows());
                                    adapter.notifyDataSetChanged();
                                    rvMessage.scrollToPosition(postion);
                                    isLoad = false;
                                } else {
                                    initRvOrder(messageListVo);
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

    private void updateUserNoticeReadSts(final String msgid) {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.updateUserNoticeReadSts)
                .tag(NotificationCenterActivity.this)
                .params("token", token)
                .params("msgid", msgid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                KLog.i(json.getString("msg"));
                                for (int i = 0; i < messageListVo.getRows().size(); i++) {
                                    if (msgid.equals(messageListVo.getRows().get(i).getMsgid())) {
                                        messageListVo.getRows().get(i).setReadsts("1");
                                        adapter.notifyDataSetChanged();
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

    private void initRvOrder(final MessageListVo vo) {
        adapter = new MessageAdapter(vo.getRows());
        adapter.openLoadAnimation();
        adapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter
                .OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (vo.getRows().get(i) != null) {
                    updateUserNoticeReadSts(vo.getRows().get(i).getMsgid());
                    switch (vo.getRows().get(i).getMsgtype()) {
                        case Constant.PUSH_TYPE_TICKET_BACK:
                            Intent intent = getIntent().setClass(NotificationCenterActivity.this, RefundActivity.class);
                            intent.putExtra("msgid", vo.getRows().get(i).getMsgid());
                            startActivity(intent);
                            break;
                        case Constant.PUSH_TYPE_BOND_REFUND:
                            break;
                        case Constant.PUSH_TYPE_SHOP_APPLY:
                            break;
                        case Constant.PUSH_TYPE_SHOP_JINGPAI:
                            startActivity(new Intent(NotificationCenterActivity.this, JmkdFive3WebActivity.class));
                            break;
                        case Constant.PUSH_TYPE_YUFUFEI_CHECK:
                            startActivity(new Intent(NotificationCenterActivity.this, JmkdSixActivity.class));
                            break;
                        case Constant.PUSH_TYPE_SHEBEIFEI_CHECK:
                            startActivity(new Intent(NotificationCenterActivity.this, JmkdSevenActivity.class));
                            break;
                        case Constant.PUSH_TYPE_CASH_APPLY:
                            break;
                        case Constant.PUSH_TYPE_ADD_STAFF:
                            break;
                        case Constant.PUSH_TYPE_SHOP_CLOSE:
                            break;
                        case Constant.PUSH_TYPE_SHOP_CHECK:
                            break;
                        case Constant.PUSH_TYPE_SHOP_TRANS:
                            break;
                        case Constant.PUSH_TYPE_BARBER_APPLY:
                            break;
                    }
                }

            }
        });
        rvMessage.setAdapter(adapter);
    }

}
