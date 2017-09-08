package com.xxl.kfapp.activity.home.jmkd;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.person.AddAddrActivity;
import com.xxl.kfapp.adapter.ProgressAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.AddrVo;
import com.xxl.kfapp.model.response.AppConfigVo;
import com.xxl.kfapp.model.response.ProgressVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.util.klog.KLog;

/**
 * 作者：XNN
 * 日期：2017/6/7
 * 作用：加盟开店第五步 选址
 */
@SuppressWarnings("deprecation")
public class JmkdFiveActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.pRecyclerView)
    RecyclerView pRecyclerView;
    @Bind(R.id.mScrollView)
    ScrollView mScrollView;
    @Bind(R.id.next)
    Button next;
    @Bind(R.id.iv_select_addr)
    ImageView ivSelectAddr;
    @Bind(R.id.tv_no_address)
    TextView tvNoAddress;
    @Bind(R.id.tv_have_address)
    TextView tvHaveAddress;
    @Bind(R.id.et_shopname)
    EditText etShopname;
    @Bind(R.id.ll_have_address)
    LinearLayout llHaveAddress;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.tv_addrgood)
    TextView tvAddrGood;

    private ProgressAdapter progressAdapter;
    private List<ProgressVo> progressVos;
    private Drawable selected, unselected;
    private String applyid;
    private boolean hasAddress;
    private AddrVo addrVo;

    @Override
    protected void initArgs(Intent intent) {
        applyid = intent.getStringExtra("applyid");
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_jmkd_five);
        ButterKnife.bind(this);
        next.setOnClickListener(this);
        mTitleBar.setTitle("开店申请");
        mTitleBar.setBackOnclickListener(this);
        tvNoAddress.setOnClickListener(this);
        tvHaveAddress.setOnClickListener(this);
        tvAddress.setOnClickListener(this);
        hasAddress = false;
//        setBtnNext(false, R.drawable.bg_corner_gray, getResources().getColor(R.color.gray));
        initDrawables();
    }

    private void initDrawables() {
        selected = getResources().getDrawable(R.mipmap.qq_red);
        unselected = getResources().getDrawable(R.mipmap.qq_grey);
        selected.setBounds(0, 0, selected.getMinimumWidth(), selected.getMinimumHeight());
        unselected.setBounds(0, 0, unselected.getMinimumWidth(), unselected.getMinimumHeight());
    }

    @Override
    protected void initData() {
        initInfoRecycleView();
        if (TextUtils.isEmpty(applyid)) {
            applyid = PreferenceUtils.getPrefString(getApplication(), "applyid", "");
        }
        //doGetSelectAddrGoodPic();
        AppConfigVo vo = (AppConfigVo) mACache.getAsObject("appConfig");
        if (vo != null) tvAddrGood.setText(vo.getAddgood());
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
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                if (hasAddress) {
                    if (TextUtils.isEmpty(etShopname.getText().toString())) {
                        ToastShow("店名不能为空");
                    } else {
                        doUpdateShopApplyInfo(addrVo);
                    }
                } else {
                    startActivity(getIntent().setClass(this, JmkdFive2Activity.class));
                    finish();
                }
                break;
            case R.id.tv_no_address:
                llHaveAddress.setVisibility(View.GONE);
                tvNoAddress.setCompoundDrawables(null, null, selected, null);
                tvHaveAddress.setCompoundDrawables(null, null, unselected, null);
                setBtnNext(true, R.drawable.bg_corner_red, getResources().getColor(R.color.main_red));
                hasAddress = false;
                break;
            case R.id.tv_have_address:
                llHaveAddress.setVisibility(View.VISIBLE);
                tvHaveAddress.setCompoundDrawables(null, null, selected, null);
                tvNoAddress.setCompoundDrawables(null, null, unselected, null);
                hasAddress = true;
                setBtnNext(false, R.drawable.bg_corner_gray, getResources().getColor(R.color.gray));
                break;
            case R.id.tv_address:
                Intent i = new Intent(this, AddAddrActivity.class);
                i.putExtra("hasAddress", "1");
                startActivityForResult(i, 1);
                break;
        }

    }

    private void setBtnNext(boolean clickable, int bg_corner, int color) {
        next.setClickable(clickable);
        next.setBackgroundResource(bg_corner);
        next.setTextColor(color);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    addrVo = (AddrVo) data.getSerializableExtra("addrVo");
                    tvAddress.setText(addrVo.getAddprovincename() + addrVo.getAddcityname()
                            + addrVo.getAddareaname() + addrVo.getAddress());
                    setBtnNext(true, R.drawable.bg_corner_red, getResources().getColor(R.color.main_red));
                    //                    doUpdateApplyStatus();
                    break;
            }
        }
    }

    /**
     * 初始化progress列表
     */
    private void initInfoRecycleView() {

        progressAdapter = new ProgressAdapter(new ArrayList<ProgressVo>(), getScrnWeight() / 4);
        pRecyclerView.setAdapter(progressAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        pRecyclerView.setLayoutManager(layoutManager);
        setData();
        pRecyclerView.smoothScrollToPosition(4);

    }

    private void setData() {
        progressVos = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            ProgressVo vo = new ProgressVo();
            if (i == 0) {
                vo.setName("申请加盟");
                vo.setTag(2);
            } else if (i == 1) {
                vo.setName("审核");
                vo.setTag(2);
            } else if (i == 2) {
                vo.setName("阅读协议");
                vo.setTag(2);
            } else if (i == 3) {
                vo.setName("品牌保证金");
                vo.setTag(2);
            } else if (i == 4) {
                vo.setName("选址");
                vo.setTag(1);
            } else if (i == 5) {
                vo.setName("装修设备");
            } else if (i == 6) {
                vo.setName("加盟成功");
            }

            progressVos.add(vo);
        }
        progressAdapter.setNewData(progressVos);
    }

    private void doGetSelectAddrGoodPic() {
        String token = PreferenceUtils.getPrefString(getApplicationContext(), "token", "1234567890");
        OkGo.<String>post(Urls.baseUrl + Urls.getSelectAddrGoodPic)
                .tag(this)
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (!code.equals("100000")) {
                                sweetDialog(json.getString("msg"), 1, false);
                            } else {
                                Glide.with(getApplicationContext()).load(json.getJSONObject("data").getString("yspic"))
                                        .into(ivSelectAddr);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void doUpdateShopApplyInfo(AddrVo vo) {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.updateShopApplyInfo)
                .tag(this)
                .params("token", token)
                .params("applysts", "25")
                .params("addrflag", "1")
                .params("selprovincecode", vo.getAddprovincecode())
                .params("selcitycode", vo.getAddcitycode())
                .params("selareacode", vo.getAddareacode())
                .params("address", vo.getAddress())
                .params("shopname", etShopname.getText().toString())
                .params("applyid", applyid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (!code.equals("100000")) {
                                sweetDialog(json.getString("msg"), 1, false);
                            } else {
                                KLog.d(json.getString("data"));
                                startActivity(getIntent().setClass(JmkdFiveActivity.this, JmkdSixActivity.class));
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
