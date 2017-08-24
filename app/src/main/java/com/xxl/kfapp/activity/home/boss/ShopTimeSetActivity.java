package com.xxl.kfapp.activity.home.boss;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.base.BaseApplication;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.TimePickerDialog;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShopTimeSetActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.ll_start)
    LinearLayout llStart;
    @Bind(R.id.ll_end)
    LinearLayout llEnd;
    @Bind(R.id.tv_start)
    TextView tvStart;
    @Bind(R.id.tv_end)
    TextView tvEnd;

    private TimePickerDialog timePickerDialog;
    private String start, end, shopid;
    private int sHour, sMinute, eHour, eMinute;

    @Override
    protected void initArgs(Intent intent) {
        shopid = intent.getStringExtra("shopid");
        start = intent.getStringExtra("starttime");
        end = intent.getStringExtra("endtime");
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_shop_time);
        ButterKnife.bind(this);
        mTitleBar.setBackOnclickListener(this);
        mTitleBar.setTitle("营业时间设置");
        mTitleBar.setRightTV("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(start)) {
                    ToastShow("营业开始时间不能为空");
                } else if (TextUtils.isEmpty(end)) {
                    ToastShow("营业结束时间不能为空");
                } else {
                    String[] s = start.split(":");
                    String[] e = end.split(":");
                    if (sHour > eHour) {
                        ToastShow("开始时间不能大于结束时间");
                    } else if (sHour == eHour && sMinute > eMinute) {
                        ToastShow("开始时间不能大于结束时间");
                    } else {
                        updateShopInfo();
                    }
                }
            }
        });
        mTitleBar.getTvLeft().setTextColor(Color.WHITE);
        llEnd.setOnClickListener(this);
        llStart.setOnClickListener(this);
        tvStart.setText(start);
        tvEnd.setText(end);
    }

    @Override
    protected void initData() {
        timePickerDialog = new TimePickerDialog(this);
        tvStart.setText(start);
        tvEnd.setText(end);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_end:
                timePickerDialog.setTimePickerDialogInterface(new TimePickerDialog.TimePickerDialogInterface() {
                    @Override
                    public void positiveListener() {
                        eHour = timePickerDialog.getHour();
                        eMinute = timePickerDialog.getMinute();
                        String hour = eHour > 9 ? eHour + "" : "0" + eHour;
                        String minute = eMinute > 9 ? eMinute + "" : "0" + eMinute;
                        end = hour + ":" + minute;
                        tvEnd.setText(end);
                    }

                    @Override
                    public void negativeListener() {

                    }
                });
                timePickerDialog.showTimePickerDialog();
                break;
            case R.id.ll_start:
                timePickerDialog.setTimePickerDialogInterface(new TimePickerDialog.TimePickerDialogInterface() {
                    @Override
                    public void positiveListener() {
                        sHour = timePickerDialog.getHour();
                        sMinute = timePickerDialog.getMinute();
                        String hour = sHour > 9 ? sHour + "" : "0" + sHour;
                        String minute = sMinute > 9 ? sMinute + "" : "0" + sMinute;
                        start = hour + ":" + minute;
                        tvStart.setText(start);
                    }

                    @Override
                    public void negativeListener() {

                    }
                });
                timePickerDialog.showTimePickerDialog();
                break;
        }
    }

    /**
     * 修改营业时间
     */
    private void updateShopInfo() {
        String token = PreferenceUtils.getPrefString(BaseApplication.getContext(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.updateShopInfo)
                .tag(this)
                .params("token", token)
                .params("shopid", shopid)
                .params("begintime", start)
                .params("endtime", end)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                Intent i = new Intent();
                                i.putExtra("business", start + "-" + end);
                                setResult(RESULT_OK, i);
                                finish();
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
