package com.xxl.kfapp.activity.home.register;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.adapter.ProgressAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.AppConfigVo;
import com.xxl.kfapp.model.response.ApplyStatusVo;
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
 * 作用：注册快发师第二步 审核
 */

public class RegisterKfsTwoActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.next)
    Button next;
    @Bind(R.id.pRecyclerView)
    RecyclerView pRecyclerView;
    @Bind(R.id.tv_checking)
    TextView tvChecking;
    @Bind(R.id.tv_fixedreason)
    TextView tvFixedReason;
    @Bind(R.id.tv_customreason)
    TextView tvCustomReason;
    @Bind(R.id.lyt_reason_shsb)
    LinearLayout lytReasonShsb;
    @Bind(R.id.lyt_next)
    LinearLayout lytNext;
    @Bind(R.id.tv_tips)
    TextView tvTips;
    @Bind(R.id.tv_checking_date)
    TextView tvCheckingDate;

    private ProgressAdapter progressAdapter;
    private List<ProgressVo> progressVos;
    private ApplyStatusVo statusVo;
    private Drawable pass, fair;

    @Override
    protected void initArgs(Intent intent) {
        statusVo = (ApplyStatusVo) intent.getSerializableExtra("");
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_registerkfs_two);
        ButterKnife.bind(this);
        next.setOnClickListener(this);
        mTitleBar.setTitle("注册快发师申请");
        mTitleBar.setBackOnclickListener(this);
        initDrawables();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        initInfoRecycleView();
        doGetBarberApplyStatus();
        StatService.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @SuppressWarnings("deprecation")
    private void initDrawables() {
        pass = getResources().getDrawable(R.mipmap.sh_cg);
        fair = getResources().getDrawable(R.mipmap.sh_sb);
        pass.setBounds(0, 0, pass.getMinimumWidth(), pass.getMinimumHeight());
        fair.setBounds(0, 0, fair.getMinimumWidth(), fair.getMinimumHeight());
    }

    private void doUpdateApplyStatus() {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.updateApplyStatus)
                .tag(this)
                .params("token", token)
                .params("applysts", "12")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (!code.equals("100000")) {
                                sweetDialog(json.getString("msg"), 1, false);
                            } else {
                                KLog.i(response.body());
                                startActivity(new Intent(RegisterKfsTwoActivity.this, RegisterKfsThreeActivity.class));
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
                            Intent intent = new Intent(RegisterKfsTwoActivity.this, RegisterKfsOneActivity.class);
                            intent.putExtra("isRepeat", true);
                            startActivity(intent);
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
        for (int i = 0; i < 5; i++) {
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
                vo.setName("考试");
            } else if (i == 4) {
                vo.setName("申请成功");
            }

            progressVos.add(vo);
        }
        progressAdapter.setNewData(progressVos);
    }

    @SuppressWarnings("deprecation")
    private void doGetBarberApplyStatus() {
        String token = PreferenceUtils.getPrefString(this.getApplicationContext(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getBarberApplyStatus)
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
                                KLog.d(response.body());
                                statusVo = mGson.fromJson(json.getString("data"), ApplyStatusVo.class);
                                if (TextUtils.isEmpty(statusVo.getChecksts())) {
                                    statusVo.setChecksts("0");
                                }
                                switch (statusVo.getChecksts()) {
                                    case "0":
                                        next.setClickable(false);
                                        next.setBackgroundResource(R.drawable.bg_corner_gray);
                                        next.setTextColor(getResources().getColor(R.color.gray));
                                        lytNext.setVisibility(View.GONE);
                                        tvCheckingDate.setVisibility(View.VISIBLE);
                                        AppConfigVo vo = (AppConfigVo) mACache.getAsObject("appConfig");
                                        tvCheckingDate.setText("一般" + vo.getBarbercheckdays() + "个工作日，请耐心等待");
                                        break;
                                    case "1":
                                        tvChecking.setText("恭喜您，您的初审已通过");
                                        tvCheckingDate.setVisibility(View.GONE);
                                        tvChecking.setCompoundDrawables(pass, null, null, null);
                                        next.setText("下一步");
                                        next.setBackgroundResource(R.drawable.bg_corner_red);
                                        next.setTextColor(getResources().getColor(R.color.main_red));
                                        next.setClickable(true);
                                        lytNext.setVisibility(View.VISIBLE);
                                        break;
                                    case "2":
                                        tvChecking.setText("真遗憾，您的初审未通过");
                                        tvChecking.setCompoundDrawables(fair, null, null, null);
                                        lytReasonShsb.setVisibility(View.VISIBLE);
                                        tvCheckingDate.setVisibility(View.GONE);
                                        if (statusVo.getFixedreason().contains(",")) {
                                            tvFixedReason.setText(statusVo.getFixedreason().replaceAll(",", "\n"));
                                            tvFixedReason.setCompoundDrawables(null, null, null, null);
                                        } else
                                            tvFixedReason.setText(statusVo.getFixedreason());
                                        if (!TextUtils.isEmpty(statusVo.getCustomreason())) {
                                            tvCustomReason.setText(statusVo.getCustomreason());
                                        } else {
                                            tvCustomReason.setVisibility(View.GONE);
                                        }
                                        next.setText("重新填写");
                                        next.setClickable(true);
                                        next.setBackgroundResource(R.drawable.bg_corner_red);
                                        next.setTextColor(getResources().getColor(R.color.main_red));
                                        tvTips.setVisibility(View.VISIBLE);
                                        lytNext.setVisibility(View.VISIBLE);
                                        break;
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
