package com.xxl.kfapp.activity.person;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.home.boss.ShopDetailActivity;
import com.xxl.kfapp.activity.home.boss.ShopListActivity;
import com.xxl.kfapp.adapter.OrderAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.OrderListVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.ListViewDecoration;
import com.xxl.kfapp.widget.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderListActivity extends BaseActivity {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.rv_order)
    RecyclerView rvOrder;

    @Override
    protected void initArgs(Intent intent) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        mTitleBar.setBackOnclickListener(this);
        mTitleBar.setTitle("交易记录");

    }

    @Override
    protected void initData() {
        getUserOrderList();
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

    private void getUserOrderList() {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getUserOrderList)
                .tag(this)
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                OrderListVo vo = mGson.fromJson(json.getString("data"), OrderListVo.class);
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

    private void initRvOrder(OrderListVo vo) {
        OrderAdapter adapter = new OrderAdapter(vo.getOrderlst());
        adapter.openLoadAnimation();
        rvOrder.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        rvOrder.setLayoutManager(layoutManager);
    }
}
