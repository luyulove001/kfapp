package com.xxl.kfapp.activity.home.boss;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.common.FindOrRegisterActivity;
import com.xxl.kfapp.activity.common.LoginActivity;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.CashApplyVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.util.klog.KLog;
import talex.zsw.baselibrary.view.SweetAlertDialog.SweetAlertDialog;

public class ShopUnbindActivity extends BaseActivity {

    @Bind(R.id.btn_confirm)
    Button btnConfirm;
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.rg_way)
    RadioGroup rgWay;
    @Bind(R.id.rb_close_shop)
    RadioButton rbCloseShop;
    @Bind(R.id.rb_transfer)
    RadioButton rbTransfer;
    @Bind(R.id.et_close_reason)
    EditText etCloseReason;
    @Bind(R.id.ll_trans)
    LinearLayout llTrans;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_staffno)
    EditText etStaffno;

    private String token, shopid, closetype = "1";
    private int mBalance;

    @Override
    protected void initArgs(Intent intent) {
        shopid = intent.getStringExtra("shopid");
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_shop_unbind);
        ButterKnife.bind(this);
        mTitleBar.setTitle("解绑店铺");
        mTitleBar.setBackOnclickListener(this);
        rbCloseShop.setChecked(true);
        mTitleBar.setRightTV("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        mTitleBar.getvTvRight().setTextColor(Color.WHITE);
        rgWay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == rbTransfer.getId()) {
                    closetype = "2";
                    etCloseReason.setVisibility(View.GONE);
                    llTrans.setVisibility(View.VISIBLE);
                } else if (checkedId == rbCloseShop.getId()) {
                    closetype = "1";
                    etCloseReason.setVisibility(View.VISIBLE);
                    llTrans.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void initData() {
        token = PreferenceUtils.getPrefString(this.getApplicationContext(), "token", "1234567890");
        getShopCashApplyInfo();
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

    private void save() {
        if (mBalance > 0) {
            String tips = "您还有" + mBalance + "元没有取出，确定要解绑店铺吗？";
            sweetDialog(tips, 0, false, new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    close();
                }
            });
        } else {
            close();
        }
    }

    private void close() {
        if ("1".equals(closetype)) {
            if (TextUtils.isEmpty(etCloseReason.getText().toString())) {
                ToastShow("关闭原因不能为空");
            } else {
                insertShopCloseApplyClose();
            }
        } else if ("2".equals(closetype)) {
            if (!FindOrRegisterActivity.checkPhoneNumber(etPhone.getText().toString())) {
                ToastShow("手机号码格式不正确");
            } else {
                insertShopCloseApplyTrans();
            }
        }
    }

    private void insertShopCloseApplyClose() {
        OkGo.<String>get(Urls.baseUrl + Urls.insertShopCloseApply)
                .tag(this)
                .params("token", token)
                .params("shopid", shopid)
                .params("colsetype", closetype)
                .params("otherreason", etCloseReason.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                KLog.i(response.body());
                                finish();
                            } else {
                                sweetDialog(json.getString("msg"), 1, false);
                            }
                        } catch (com.alibaba.fastjson.JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void insertShopCloseApplyTrans() {
        OkGo.<String>get(Urls.baseUrl + Urls.insertShopCloseApply)
                .tag(this)
                .params("token", token)
                .params("shopid", shopid)
                .params("colsetype", closetype)
                .params("staffno", etStaffno.getText().toString())
                .params("username", etName.getText().toString())
                .params("phone", etPhone.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                KLog.i(response.body());
                                finish();
                            } else {
                                sweetDialog(json.getString("msg"), 1, false);
                            }
                        } catch (com.alibaba.fastjson.JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getShopCashApplyInfo() {
        OkGo.<String>get(Urls.baseUrl + Urls.getShopCashApplyInfo)
                .tag(this)
                .params("token", token)
                .params("shopid", shopid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                CashApplyVo vo = mGson.fromJson(json.getString("data"), CashApplyVo.class);
                                mBalance = Integer.valueOf(vo.getBalance());
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
