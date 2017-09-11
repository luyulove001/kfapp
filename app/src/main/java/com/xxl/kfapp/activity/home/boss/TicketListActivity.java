package com.xxl.kfapp.activity.home.boss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.baidu.mobstat.StatService;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.adapter.TicketAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.TicketListVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.TimePickerDialog;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.ListViewDecoration;
import com.xxl.kfapp.widget.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.util.klog.KLog;

public class TicketListActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.rv_ticket)
    RecyclerView rvTicket;
    @Bind(R.id.tv_starttime)
    TextView tvStarttime;
    @Bind(R.id.tv_endtime)
    TextView tvEndtime;
    @Bind(R.id.tv_cut_count)
    TextView tvCutCount;
    @Bind(R.id.btn_search)
    Button btnSearch;
    @Bind(R.id.btn_type)
    Button btnType;
    @Bind(R.id.mRefreshLayout)
    EasyRefreshLayout mRefreshLayout;

    private TimePickerDialog dialog;
    private String beginDate, endDate, barberid;
    private TicketAdapter adapter;
    private boolean isToday, isBoss;
    private int mPage = 1;
    private boolean isRefresh = false, isLoad = false;

    @Override
    protected void initArgs(Intent intent) {
        isToday = intent.getBooleanExtra("isToday", false);
        isBoss = intent.getBooleanExtra("isBoss", false);
        barberid = intent.getStringExtra("barberid");
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_ticket_list);
        ButterKnife.bind(this);
        mTitleBar.setTitle("理发业绩");
        mTitleBar.setBackOnclickListener(this);
        btnType.setVisibility(View.GONE);
        btnSearch.setOnClickListener(this);
        tvEndtime.setOnClickListener(this);
        tvStarttime.setOnClickListener(this);
        rvTicket.addItemDecoration(new ListViewDecoration(R.drawable.divider_recycler_10px));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        rvTicket.setLayoutManager(layoutManager);
        initRefreshListener();
    }

    private void initRefreshListener() {
        mRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                mPage +=1;
                isLoad = true;
                initData();
            }

            @Override
            public void onRefreshing() {
                mPage = 1;
                isRefresh = true;
                initData();
            }
        });
    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(barberid)) {
            getShopCutRecord("", "", "");
        } else {
            if (isToday) {
                getTodayData();
            } else {
                if (isBoss) {
                    getShopCutRecord("", "", "");
                } else {
                    getBarberCutRecord("", "");
                }
            }
        }
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

    private void getTodayData() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(Calendar.getInstance().getTime());
        tvEndtime.setText(today);
        endDate = beginDate = today;
        tvStarttime.setText(today);
        if (isBoss) {
            getShopCutRecord(today, today, "");
        } else {
            getBarberCutRecord(today, today);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_starttime:
                dialog = new TimePickerDialog(this);
                dialog.setTimePickerDialogInterface(new TimePickerDialog.TimePickerDialogInterface() {
                    @Override
                    public void positiveListener() {
                        String year = String.valueOf(dialog.getYear());
                        String month = String.valueOf(dialog.getMonth());
                        String day = String.valueOf(dialog.getDay());
                        beginDate = year + "-" + month + "-" + day;
                        tvStarttime.setText(beginDate);
                    }

                    @Override
                    public void negativeListener() {

                    }
                });
                dialog.showDatePickerDialog();
                break;
            case R.id.tv_endtime:
                dialog = new TimePickerDialog(this);
                dialog.setTimePickerDialogInterface(new TimePickerDialog.TimePickerDialogInterface() {
                    @Override
                    public void positiveListener() {
                        String year = String.valueOf(dialog.getYear());
                        String month = String.valueOf(dialog.getMonth());
                        String day = String.valueOf(dialog.getDay());
                        endDate = year + "-" + month + "-" + day;
                        tvEndtime.setText(endDate);
                    }

                    @Override
                    public void negativeListener() {

                    }
                });
                dialog.showDatePickerDialog();
                break;
            case R.id.btn_search:
                if (TextUtils.isEmpty(beginDate)) {
                    ToastShow("请先选择开始时间");
                } else if (TextUtils.isEmpty(endDate)) {
                    ToastShow("请先选择结束时间");
                } else {
                    getShopCutRecord(beginDate, endDate, "");
                }
                break;
        }
    }


    private void initTicketList(TicketListVo vo) {
        tvCutCount.setText("累计理发次数：" + vo.getTotal());
        adapter = new TicketAdapter(vo.getRows());
        adapter.openLoadAnimation();
        rvTicket.setAdapter(adapter);
    }

    private void getShopCutRecord(String beginDate, String endDate, String shopid) {
        String token = PreferenceUtils.getPrefString(getApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getShopCutRecord)
                .tag(this)
                .params("token", token)
                .params("begindate", beginDate)
                .params("enddate", endDate)
                .params("shopid", shopid)
                .params("barberid", barberid)
                .params("page", mPage)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        mRefreshLayout.setRefreshing(false);
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                TicketListVo vo = mGson.fromJson(json.getString("data"), TicketListVo.class);
                                if (vo != null) {
                                    KLog.d(vo.getRows().size());
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
                                        initTicketList(vo);
                                    }
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

    private void getBarberCutRecord(String beginDate, String endDate) {
        String token = PreferenceUtils.getPrefString(getApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getBarberCutRecord)
                .tag(this)
                .params("token", token)
                .params("begindate", beginDate)
                .params("enddate", endDate)
                .params("page", mPage)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        mRefreshLayout.setRefreshing(false);
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                TicketListVo vo = mGson.fromJson(json.getString("data"), TicketListVo.class);
                                if (vo != null) {
                                    KLog.d(vo.getRows().size());
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
                                        initTicketList(vo);
                                    }
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
}
