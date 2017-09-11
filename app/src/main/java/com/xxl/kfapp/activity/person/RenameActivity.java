package com.xxl.kfapp.activity.person;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.adapter.ShopRenameAdapter;
import com.xxl.kfapp.adapter.TicketAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.AddrVo;
import com.xxl.kfapp.model.response.MemberInfoVo;
import com.xxl.kfapp.model.response.ShopRenameRecordVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.ListViewDecoration;
import com.xxl.kfapp.widget.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.util.klog.KLog;

public class RenameActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.input_nickname)
    EditText etNickname;
    @Bind(R.id.clear_nickname)
    ImageView ivClear;
    @Bind(R.id.ll_operate_record)
    LinearLayout llOperateRecord;
    @Bind(R.id.rv_rename_record)
    RecyclerView rvRename;

    private String shopName, shopid;
    private String token;
    private boolean isShopRename;

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
        shopName = intent.getStringExtra("shopName");
        shopid = intent.getStringExtra("shopid");
        isShopRename = !TextUtils.isEmpty(shopName);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.ac_rename);
        ButterKnife.bind(this);
        ivClear.setOnClickListener(this);
        if (isShopRename) {
            mTitleBar.setTitle("店名修改");
        } else {
            mTitleBar.setTitle("修改昵称");
            rvRename.setVisibility(View.GONE);
            llOperateRecord.setVisibility(View.GONE);
        }
        mTitleBar.setBackOnclickListener(this);
        mTitleBar.setRightTV("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etNickname.getText().toString())) {
                    ToastShow("输入内容不能为空");
                    return;
                }
                if (isShopRename) {
                    reName();
                    Intent i = new Intent();
                    i.putExtra("shopName", etNickname.getText().toString());
                    setResult(RESULT_OK, i);
                    finish();
                } else {
                    updateMemberInfo(etNickname.getText().toString());
                }
            }
        });
        mTitleBar.getvTvRight().setTextColor(getResources().getColor(R.color.white));
        etNickname.addTextChangedListener(mTextWatcher);
    }

    @Override
    protected void initData() {
        token = PreferenceUtils.getPrefString(this.getApplicationContext(), "token", "1234567890");
        if (isShopRename) {
            llOperateRecord.setVisibility(View.VISIBLE);
            getShopModifyRecord();
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
    private void reName() {
        OkGo.<String>get(Urls.baseUrl + Urls.updateShopInfo)
                .tag(this)
                .params("token", token)
                .params("shopid", shopid)
                .params("oidname", shopName)
                .params("newname", etNickname.getText().toString())
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

    private void updateMemberInfo(final String nickname) {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.updateMemberInfo)
                .tag(this)
                .params("token", token)
                .params("nickname", nickname)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                ToastShow("修改成功");
                                MemberInfoVo vo = (MemberInfoVo) mACache.getAsObject("memberInfoVo");
                                vo.setNickname(nickname);
                                mACache.put("memberInfoVo", vo);//保存个人信息
                                Intent i = new Intent();
                                i.putExtra("nickname", etNickname.getText().toString());
                                setResult(RESULT_OK, i);
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


    /**
     * 获取店名修改记录
     */
    private void getShopModifyRecord() {
        OkGo.<String>get(Urls.baseUrl + Urls.getShopModifyRecord)
                .tag(this)
                .params("token", token)
                .params("shopid", shopid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                ShopRenameRecordVo vo = mGson.fromJson(json.getString("data"), ShopRenameRecordVo
                                        .class);
                                initRV(vo);
                            } else {
                                sweetDialog(json.getString("msg"), 1, false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initRV(ShopRenameRecordVo vo) {
        ShopRenameAdapter adapter = new ShopRenameAdapter(vo.getRdlst());
        adapter.openLoadAnimation();
        rvRename.setAdapter(adapter);
        rvRename.addItemDecoration(new ListViewDecoration(R.drawable.divider_recycler));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        rvRename.setLayoutManager(layoutManager);
    }
}
