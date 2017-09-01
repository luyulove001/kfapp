package com.xxl.kfapp.activity.home.boss;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.common.FindOrRegisterActivity;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.BossShopListVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;
import talex.zsw.baselibrary.util.klog.KLog;

public class AddBarberActivity extends BaseActivity {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.et_staffno)
    EditText etStaffno;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.lyt_shop)
    LinearLayout lytShop;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;

    private String shopid;
    private OptionPicker picker;

    @Override
    protected void initArgs(Intent intent) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_add_barber);
        ButterKnife.bind(this);
        mTitleBar.setTitle("添加理发师");
        mTitleBar.setBackOnclickListener(this);
        mTitleBar.setRightTV("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertStaff();
            }
        });
        mTitleBar.getvTvRight().setTextColor(Color.WHITE);
        lytShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.show();
            }
        });
    }

    private void insertStaff() {
        boolean isEmpty = false;
        if (TextUtils.isEmpty(etStaffno.getText().toString())) {
            ToastShow("员工工号不能为空");
            isEmpty = true;
        }
        if (!isEmpty && TextUtils.isEmpty(etPhone.getText().toString())) {
            ToastShow("员工手机号不能为空");
            isEmpty = true;
        }
        if (!isEmpty && !FindOrRegisterActivity.checkPhoneNumber(etPhone.getText().toString())) {
            ToastShow("手机号码格式不正确");
            isEmpty = true;
        }
        if (!isEmpty && TextUtils.isEmpty(shopid)) {
            ToastShow("请选择店铺");
            isEmpty = true;
        }
        if (!isEmpty) {
            doInsertShopStaff(shopid, etStaffno.getText().toString(), etPhone.getText().toString());
        }
    }

    @Override
    protected void initData() {
        doGetBossShopList();
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

    public void initOptionPicker(final List<BossShopListVo.BossShopInfo> shopInfos) {
        String[] strs = new String[shopInfos.size()];
        for (int i = 0; i < shopInfos.size(); i++) {
            strs[i] = shopInfos.get(i).getShopname();
        }
        picker = new OptionPicker(this, strs);
        picker.setCanceledOnTouchOutside(false);
        picker.setDividerRatio(WheelView.DividerConfig.FILL);
        picker.setShadowColor(Color.RED, 40);
        picker.setSelectedIndex(1);
        picker.setCycleDisable(true);
        picker.setTextSize(14);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                KLog.i("index=" + index + ", item=" + item);
                shopid = shopInfos.get(index).getShopid();
                tvShopname.setText(item);
            }
        });
    }

    private void doInsertShopStaff(String shopid, String staffno, String phone) {
        String token = PreferenceUtils.getPrefString(getApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.insertShopStaff)
                .tag(this)
                .params("token", token)
                .params("shopid", shopid)
                .params("staffno", staffno)
                .params("phone", phone)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                ToastShow("保存成功");
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

    private void doGetBossShopList() {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getBossShopList)
                .tag(this)
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                KLog.i(response.body());
                                BossShopListVo bossShopListVo = mGson.fromJson(json.getString("data"), BossShopListVo
                                        .class);
                                initOptionPicker(bossShopListVo.getShoplst());
                            } else {
                                sweetDialog(json.getString("msg"), 1, false);
                            }
                        } catch (com.alibaba.fastjson.JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
