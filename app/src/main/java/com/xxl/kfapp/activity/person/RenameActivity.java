package com.xxl.kfapp.activity.person;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.home.ShopDetailActivity;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RenameActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.input_nickname)
    EditText etNickname;
    @Bind(R.id.clear_nickname)
    ImageView ivClear;
    String shopName;
    private String token;

    private TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() > 0) {
                ivClear.setVisibility(View.VISIBLE);
            } else {
                ivClear.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void initArgs(Intent intent) {
        intent = getIntent();
        shopName = intent.getStringExtra("shopName");
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.ac_rename);
        ButterKnife.bind(this);
        ivClear.setOnClickListener(this);
        mTitleBar.setTitle("修改昵称");
        mTitleBar.setBackOnclickListener(this);
        mTitleBar.setRightTV("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reName();
                Intent i = new Intent();
                i.putExtra("nickname", etNickname.getText().toString());
                setResult(RESULT_OK, i);
                finish();
            }
        });
        mTitleBar.getvTvRight().setTextColor(getResources().getColor(R.color.white));
        etNickname.addTextChangedListener(mTextWatcher);
    }

    @Override
    protected void initData() {
        token = PreferenceUtils.getPrefString(this.getApplicationContext(), "token", "1234567890");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_nickname:
                etNickname.setText("");
                break;
        }
    }

    /**
     * 修改店名
     */
    private void reName(){
        OkGo.<String>get(Urls.baseUrl + Urls.updateShopInfo)
                .tag(this)
                .params("token", token)
                .params("shopid", "1")
                .params("oidname",shopName)
                .params("newname",etNickname.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                ToastShow("店名修改成功");
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

    /**
     * 获取店名修改记录
     */
    private void getShopModifyRecord(){
        OkGo.<String>get(Urls.baseUrl + Urls.getShopModifyRecord)
                .tag(this)
                .params("token", token)
                .params("shopid", "6")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                JSONObject rdlist = json.getJSONObject("data").getJSONObject("rdlist");//TODO 列表数据未绑定


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
