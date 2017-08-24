package com.xxl.kfapp.activity.home.boss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.home.jmkd.JmkdFive2Activity;
import com.xxl.kfapp.activity.home.jmkd.JmkdFive3WebActivity;
import com.xxl.kfapp.adapter.BidShopAdapter;
import com.xxl.kfapp.adapter.ShopAmountAdapter;
import com.xxl.kfapp.adapter.TextAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.AmountVo;
import com.xxl.kfapp.model.response.BidShopListVo;
import com.xxl.kfapp.model.response.TextVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.TimePickerDialog;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.ListViewDecoration;
import com.xxl.kfapp.widget.SlideFromBottomPopup;
import com.xxl.kfapp.widget.TitleBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShopAmountActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.tv_amount)
    TextView tvAmount;
    @Bind(R.id.tv_ali)
    TextView tvAli;
    @Bind(R.id.tv_wx)
    TextView tvWx;
    @Bind(R.id.tv_starttime)
    TextView tvStarttime;
    @Bind(R.id.tv_endtime)
    TextView tvEndtime;
    @Bind(R.id.btn_search)
    Button btnSearch;
    @Bind(R.id.btn_type)
    Button btnType;
    @Bind(R.id.rv_shop_amount)
    RecyclerView rvShopAmount;

    private TimePickerDialog dialog;
    private SlideFromBottomPopup mSlidePopup;
    private ShopAmountAdapter adapter;
    private String beginDate, endDate;
    private boolean isToday;

    @Override
    protected void initArgs(Intent intent) {
        isToday = intent.getBooleanExtra("isToday", false);
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_shop_amount);
        ButterKnife.bind(this);
        mTitleBar.setTitle("营业额");
        mTitleBar.setBackOnclickListener(this);
        tvStarttime.setOnClickListener(this);
        tvEndtime.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnType.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (isToday) getTodayData();
        else doGetMemberTotalAmountInfo("", "");
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
                    doGetMemberTotalAmountInfo(beginDate, endDate);
                }
                break;
            case R.id.btn_type:
                showGenderPopup();
                break;
        }
    }

    /**
     * 设置性别弹出窗
     */
    private void showGenderPopup() {
        mSlidePopup = new SlideFromBottomPopup(this);
        mSlidePopup.setTexts(new String[]{"今日营业额", "累计营业额", "取消"});
        mSlidePopup.setOnItemClickListener(new SlideFromBottomPopup.OnItemClickListener() {
            @Override
            public void onItemClick(View v) {
                switch (v.getId()) {
                    case R.id.tx_1:
                        getTodayData();
                        mSlidePopup.dismiss();
                        break;
                    case R.id.tx_2:
                        doGetMemberTotalAmountInfo("", "");
                        mSlidePopup.dismiss();
                        break;
                    case R.id.tx_3:
                        mSlidePopup.dismiss();
                        break;
                }
            }
        });
        mSlidePopup.showPopupWindow();
    }

    private void getTodayData() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(Calendar.getInstance().getTime());
        endDate = beginDate = today;
        tvStarttime.setText(today);
        tvEndtime.setText(today);
        doGetMemberTotalAmountInfo(today, today);
    }

    private void initAmountList(final AmountVo vo) {
        adapter = new ShopAmountAdapter(vo.getCountlst());
        adapter.openLoadAnimation();
        adapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter
                .OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = getIntent().setClass(ShopAmountActivity.this, IncomeListActivity.class);
                intent.putExtra("shopid", vo.getCountlst().get(i).getShopid());
                startActivity(intent);
            }
        });
        rvShopAmount.setAdapter(adapter);
        rvShopAmount.addItemDecoration(new ListViewDecoration(R.drawable.divider_recycler));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        rvShopAmount.setLayoutManager(layoutManager);

    }

    private void doGetMemberTotalAmountInfo(String beginDate, String endDate) {
        String token = PreferenceUtils.getPrefString(getApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getMemberTotalAmountInfo)
                .tag(this)
                .params("token", token)
                .params("begindate", beginDate)
                .params("enddate", endDate)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                AmountVo vo = mGson.fromJson(json.getString("data"), AmountVo.class);
                                if (vo != null) {
                                    tvAli.setText((int) (Float.valueOf(vo.getZfbbl()) * 100) + "%");
                                    tvWx.setText((int) (Float.valueOf(vo.getWxbl()) * 100) + "%");
                                    if (TextUtils.isEmpty(vo.getTotal())) {
                                        tvAmount.setText("¥ 0");
                                    } else {
                                        tvAmount.setText("¥ " + vo.getTotal());
                                    }
                                    initAmountList(vo);
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
