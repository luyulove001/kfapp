package com.xxl.kfapp.activity.home.jmkd;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.adapter.ProgressAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.ProgressVo;
import com.xxl.kfapp.model.response.ShopApplyStatusVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.util.klog.KLog;

/**
 * 作者：XNN
 * 日期：2017/6/7
 * 作用：加盟开店第二步 审核
 */

public class JmkdTwoActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.pRecyclerView)
    RecyclerView pRecyclerView;
    @Bind(R.id.mScrollView)
    ScrollView mScrollView;
    @Bind(R.id.next)
    Button next;
    @Bind(R.id.tv_checking)
    TextView tvChecking;
    @Bind(R.id.tv_fixedreason)
    TextView tvFixedReason;
    @Bind(R.id.tv_customreason)
    TextView tvCustomReason;
    @Bind(R.id.lyt_reason_shsb)
    LinearLayout lytReasonShsb;
    @Bind(R.id.tv_tips)
    TextView tvTips;
    private ProgressAdapter progressAdapter;
    private List<ProgressVo> progressVos;
    private ShopApplyStatusVo statusVo;
    private Drawable pass, fair;

    @Override
    protected void initArgs(Intent intent) {
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_jmkd_two);
        ButterKnife.bind(this);
        next.setOnClickListener(this);
        mTitleBar.setTitle("开店申请");
        mTitleBar.setBackOnclickListener(this);
        initDrawables();
    }

    @Override
    protected void initData() {
        initInfoRecycleView();
        doGetShopApplyStatus();
    }

    @SuppressWarnings("deprecation")
    private void initDrawables() {
        pass = getResources().getDrawable(R.mipmap.sh_cg);
        fair = getResources().getDrawable(R.mipmap.sh_sb);
        pass.setBounds(0, 0, pass.getMinimumWidth(), pass.getMinimumHeight());
        fair.setBounds(0, 0, fair.getMinimumWidth(), fair.getMinimumHeight());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                if (statusVo != null) {
                    switch (statusVo.getChecksts()) {
                        case "0":
                            ToastShow("请等待审核通过");
                            break;
                        case "1":
                            doUpdateApplyStatus();
                            break;
                        case "2":
                            startActivity(new Intent(this, JmkdOneActivity.class));
                            finish();
                            break;
                    }
                }
                break;
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
        //        pRecyclerView.smoothScrollToPosition();

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
                vo.setTag(1);
            } else if (i == 2) {
                vo.setName("阅读协议");
            } else if (i == 3) {
                vo.setName("品牌保证金");
            } else if (i == 4) {
                vo.setName("选址");
            } else if (i == 5) {
                vo.setName("装修设备");
            } else if (i == 6) {
                vo.setName("加盟成功");
            }

            progressVos.add(vo);
        }
        progressAdapter.setNewData(progressVos);
    }

    private void doGetShopApplyStatus() {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getShopApplyStatus)
                .tag(this)
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                KLog.i(response.body());
                                statusVo = mGson.fromJson(json.getString("data"), ShopApplyStatusVo.class);
                                PreferenceUtils.setPrefString(getApplication(), "applyid", statusVo.getApplyid());
                                PreferenceUtils.setPrefString(getApplication(), "brandmoney", statusVo.getBrandmoney());
                                switch (statusVo.getChecksts()) {
                                    case "0":
                                        next.setClickable(false);
                                        next.setBackgroundResource(R.drawable.bg_corner_gray);
                                        next.setTextColor(getResources().getColor(R.color.gray));
                                        break;
                                    case "1":
                                        tvChecking.setText("恭喜您，您的初审已通过");
                                        tvChecking.setCompoundDrawablesRelative(pass, null, null, null);
                                        break;
                                    case "2":
                                        tvChecking.setText("真遗憾，您的审核未通过");
                                        tvChecking.setCompoundDrawablesRelative(fair, null, null, null);
                                        lytReasonShsb.setVisibility(View.VISIBLE);
                                        tvFixedReason.setText(statusVo.getFixedreason());
                                        tvCustomReason.setText(statusVo.getCustomreason());
                                        next.setText("重新选择区域");
                                        tvTips.setVisibility(View.VISIBLE);
                                        break;
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

    private void doUpdateApplyStatus() {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.updateShopApplyStatus)
                .tag(this)
                .params("token", token)
                .params("applysts", statusVo.getApplysts())
                .params("applyid", statusVo.getApplyid())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                KLog.i(response.body());
                                Intent intent = new Intent(JmkdTwoActivity.this, JmkdThreeActivity.class);
                                intent.putExtra("applyid", statusVo.getApplyid());
                                intent.putExtra("brandmoney", statusVo.getBrandmoney());
                                startActivity(intent);
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
