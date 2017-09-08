package com.xxl.kfapp.activity.home.boss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.adapter.BarberAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.StaffVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.ListViewDecoration;
import com.xxl.kfapp.widget.TitleBar;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BarberListActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.tv_num)
    TextView tvNum;
    @Bind(R.id.et_search_content)
    EditText etSearch;
    @Bind(R.id.btn_all)
    Button btnAll;
    @Bind(R.id.rv_barber)
    SwipeMenuRecyclerView rvBarber;

    private BarberAdapter adapter;

    @Override
    protected void initArgs(Intent intent) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_barber_list);
        ButterKnife.bind(this);
        mTitleBar.setTitle("理发师");
        mTitleBar.setBackOnclickListener(this);
        mTitleBar.setRightIV(R.mipmap.qc_fast_add_write, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BarberListActivity.this, AddBarberActivity.class));
            }
        });
        btnAll.setOnClickListener(this);
        rvBarber.addItemDecoration(new ListViewDecoration(R.drawable.divider_recycler_10px));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        rvBarber.setLayoutManager(layoutManager);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_all:
                doGetBossShopStaffList(etSearch.getText().toString());
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        doGetBossShopStaffList("");
        StatService.onResume(this);
    }

    private void doGetBossShopStaffList(String realname) {
        String token = PreferenceUtils.getPrefString(getApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getBossShopStaffList)
                .tag(this)
                .params("token", token)
                .params("realname", realname)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                StaffVo vo = mGson.fromJson(json.getString("data"), StaffVo.class);
                                if (vo != null) {
                                    tvNum.setText("您共有 " + vo.getStaffcnt() + " 位理发师");
                                    initBarberList(vo);
                                }
                            } else {
                                sweetDialog(json.getString("msg"), 1, false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    private void initBarberList(final StaffVo vo) {
        adapter = new BarberAdapter(vo.getStafflst(), this);
        adapter.openLoadAnimation();
        adapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter
                .OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent();
                switch (view.getId()) {
                    case R.id.tv_checking_in:
                        intent.setClass(BarberListActivity.this, CheckInActivity.class);
                        intent.putExtra("barberid", vo.getStafflst().get(i).getStaffno());
                        BarberListActivity.this.startActivity(intent);
                        break;
                    case R.id.tv_performance:
                        intent.setClass(BarberListActivity.this, TicketListActivity.class);
                        intent.putExtra("isToday", false);
                        intent.putExtra("isBoss", false);
                        intent.putExtra("barberid", vo.getStafflst().get(i).getStaffno());
                        BarberListActivity.this.startActivity(intent);
                        break;
                    case R.id.lyt_barber_info:
                        intent.setClass(BarberListActivity.this, BarberInfoActivity.class);
                        intent.putExtra("barberid", vo.getStafflst().get(i).getStaffno());
                        intent.putExtra("shopid", vo.getStafflst().get(i).getShopid());
                        BarberListActivity.this.startActivity(intent);
                        break;
                    case R.id.tv_fire:
                        intent.setClass(BarberListActivity.this, FireActivity.class);
                        intent.putExtra("barberid", vo.getStafflst().get(i).getStaffno());
                        intent.putExtra("shopid", vo.getStafflst().get(i).getShopid());
                        BarberListActivity.this.startActivity(intent);
                        break;
                }
            }
        });
        rvBarber.setAdapter(adapter);
    }
}
