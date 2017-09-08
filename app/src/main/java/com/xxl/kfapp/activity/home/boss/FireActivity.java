package com.xxl.kfapp.activity.home.boss;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.util.klog.KLog;

public class FireActivity extends BaseActivity {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.et_fire)
    EditText etFire;

    private String shopid, barberid;
    @Override
    protected void initArgs(Intent intent) {
        shopid = intent.getStringExtra("shopid");
        barberid = intent.getStringExtra("barberid");
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_fire);
        ButterKnife.bind(this);
        mTitleBar.setTitle("解雇员工");
        mTitleBar.setBackOnclickListener(this);
        mTitleBar.setRightTV("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etFire.getText().toString())) {
                    ToastShow("解雇原因不能为空");
                } else {
                    updateShopStaffJobsts();
                }
            }
        });
        mTitleBar.getvTvRight().setTextColor(Color.parseColor("#fff"));
    }

    @Override
    protected void initData() {

    }

    private void updateShopStaffJobsts() {
        String token = PreferenceUtils.getPrefString(getApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.updateShopStaffJobsts)
                .tag(this)
                .params("token", token)
                .params("shopid", shopid)
                .params("barberid", barberid)
                .params("leavereason", etFire.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                KLog.i(response.body());
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
