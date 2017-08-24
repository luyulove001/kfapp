package com.xxl.kfapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.common.LoginActivity;
import com.xxl.kfapp.activity.home.boss.RefundActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdFive3WebActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdFiveActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdFivePrepayActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdFourActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdOneActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdSevenActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdSixActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdThreeActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdTwoActivity;
import com.xxl.kfapp.activity.home.register.RegisterKfsFiveActivity;
import com.xxl.kfapp.activity.home.register.RegisterKfsFourActivity;
import com.xxl.kfapp.activity.home.register.RegisterKfsOneActivity;
import com.xxl.kfapp.activity.home.register.RegisterKfsThreeActivity;
import com.xxl.kfapp.activity.home.register.RegisterKfsTwoActivity;
import com.xxl.kfapp.activity.person.ModifyAddrActivity;
import com.xxl.kfapp.activity.person.NotificationCenterActivity;
import com.xxl.kfapp.activity.person.OrderListActivity;
import com.xxl.kfapp.activity.person.PersonInfoActivity;
import com.xxl.kfapp.base.BaseApplication;
import com.xxl.kfapp.base.BaseFragment;
import com.xxl.kfapp.model.response.ApplyListVo;
import com.xxl.kfapp.model.response.MemberInfoVo;
import com.xxl.kfapp.model.response.ShopApplyStatusVo;
import com.xxl.kfapp.utils.Constant;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.util.klog.KLog;
import talex.zsw.baselibrary.view.SweetAlertDialog.SweetAlertDialog;
import talex.zsw.baselibrary.widget.CircleImageView;

public class PersonFragment1 extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.civ_head)
    CircleImageView civHead;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.lyt_head)
    RelativeLayout lytHead;
    @Bind(R.id.lyt_address)
    LinearLayout lytAddress;
    @Bind(R.id.address)
    TextView tvAddress;
    @Bind(R.id.about)
    TextView tvAbout;
    @Bind(R.id.lyt_apply)
    LinearLayout lytApply;
    @Bind(R.id.lyt_order)
    RelativeLayout lytOrder;
    @Bind(R.id.lyt_tui)
    RelativeLayout lytTui;
    @Bind(R.id.lyt_notify)
    RelativeLayout lytNotify;
    @Bind(R.id.lyt_about)
    RelativeLayout lytAbout;
    @Bind(R.id.btn_logout)
    Button btnLogout;

    private ApplyListVo applyListVo;
    private ShopApplyStatusVo shopStatusVo;
    private String prepaychecksts;

    @Override
    protected void initArgs(Bundle bundle) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.fragment_person_1);
        ButterKnife.bind(this, mView);
        lytAbout.setOnClickListener(this);
        lytAddress.setOnClickListener(this);
        lytApply.setOnClickListener(this);
        lytHead.setOnClickListener(this);
        lytNotify.setOnClickListener(this);
        lytTui.setOnClickListener(this);
        lytOrder.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        MemberInfoVo memberInfoVo = (MemberInfoVo) mACache.getAsObject("memberInfoVo");
        Glide.with(BaseApplication.getContext()).load(memberInfoVo.getHeadpic()).into(civHead);
        tvNickname.setText(memberInfoVo.getNickname());
        tvAddress.setText(memberInfoVo.getDispaddress());
        tvAbout.setText(Constant.GetVersion(BaseApplication.getContext()));
        if ("2".equals(memberInfoVo.getRole())) lytTui.setVisibility(View.VISIBLE);
        getMemberShopApply();
        doGetShopApplyStatus();
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
            case R.id.lyt_head:
                startActivity(new Intent(getActivity(), PersonInfoActivity.class));
                break;
            case R.id.lyt_address:
                Intent i = new Intent(getActivity(), ModifyAddrActivity.class);
                startActivity(i);
                break;
            case R.id.lyt_apply:
                start2Apply();
                break;
            case R.id.lyt_order:
                startActivity(new Intent(getActivity(), OrderListActivity.class));
                break;
            case R.id.lyt_tui:
                startActivity(new Intent(getActivity(), RefundActivity.class));
                break;
            case R.id.lyt_notify:
                startActivity(new Intent(getActivity(), NotificationCenterActivity.class));
                break;
            case R.id.lyt_about:
                break;
            case R.id.btn_logout:
                sweetDialogCustom(0, "是否确定退出登录", "", "确定退出", "取消", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        PreferenceUtils.setPrefString(BaseApplication.getContext(), "password", "");
                        PreferenceUtils.setPrefString(BaseApplication.getContext(), "uuid", "");
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                }, new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        closeDialog();
                    }
                });
                break;
        }
    }

    private void start2Apply() {
        switch (applyListVo.getApplylst().get(0).getApplysts()) {
            case "10":
                startActivity(new Intent(getActivity(), RegisterKfsOneActivity.class));
                break;
            case "11":
                startActivity(new Intent(getActivity(), RegisterKfsTwoActivity.class));
                break;
            case "12":
                startActivity(new Intent(getActivity(), RegisterKfsThreeActivity.class));
                break;
            case "13":
                startActivity(new Intent(getActivity(), RegisterKfsFourActivity.class));
                break;
            case "14":
                startActivity(new Intent(getActivity(), RegisterKfsFiveActivity.class));
                break;
            case "20":
                Intent i20 = new Intent(getActivity(), JmkdOneActivity.class);
                i20.putExtra("applyid", applyListVo.getApplylst().get(0).getApplyid());
                startActivity(i20);
                break;
            case "21":
                startActivity(new Intent(getActivity(), JmkdTwoActivity.class));
                break;
            case "22":
                startActivity(new Intent(getActivity(), JmkdThreeActivity.class));
                break;
            case "23":
                startActivity(new Intent(getActivity(), JmkdFourActivity.class));
                break;
            case "24":
                if (!TextUtils.isEmpty(prepaychecksts)) {
                    Intent i = new Intent(getActivity(), JmkdFivePrepayActivity.class);
                    i.putExtra("shopStatusVo", shopStatusVo);
                    startActivity(i);
                } else if (TextUtils.isEmpty(applyListVo.getApplylst().get(0).getShopid())) {
                    startActivity(new Intent(getActivity(), JmkdFiveActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), JmkdFive3WebActivity.class));
                }
                break;
            case "25":
                startActivity(new Intent(getActivity(), JmkdSixActivity.class));
                break;
            case "26":
                startActivity(new Intent(getActivity(), JmkdSevenActivity.class));
                break;
        }
    }

    private void doGetShopApplyStatus() {
        String token = PreferenceUtils.getPrefString(BaseApplication.getContext(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getShopApplyStatus)
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
                                KLog.i(response.body());
                                Gson gson = new Gson();
                                shopStatusVo = gson.fromJson(json.getString("data"), ShopApplyStatusVo.class);
                                PreferenceUtils.setPrefString(BaseApplication.getContext(), "applyid", shopStatusVo.getApplyid());
                                prepaychecksts = shopStatusVo.getPrepaychecksts();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getMemberShopApply() {
        String token = PreferenceUtils.getPrefString(BaseApplication.getContext(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getMemberShopApply)
                .tag(this)
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                Gson gson = new Gson();
                                applyListVo = gson.fromJson(json.getString("data"), ApplyListVo.class);
                                if (applyListVo != null && applyListVo.getApplylst().size() != 0) {
                                    lytApply.setVisibility(View.VISIBLE);
                                }
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
