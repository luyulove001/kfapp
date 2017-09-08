package com.xxl.kfapp.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.home.boss.BarberListActivity;
import com.xxl.kfapp.activity.home.boss.CheckInActivity;
import com.xxl.kfapp.activity.home.boss.ShopAmountActivity;
import com.xxl.kfapp.activity.home.boss.ShopListActivity;
import com.xxl.kfapp.activity.home.boss.TicketListActivity;
import com.xxl.kfapp.activity.home.boss.WithdrawListActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdFive3WebActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdFiveActivity;
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
import com.xxl.kfapp.activity.person.PersonInfoActivity;
import com.xxl.kfapp.base.BaseApplication;
import com.xxl.kfapp.base.BaseFragment;
import com.xxl.kfapp.model.response.ApplyListVo;
import com.xxl.kfapp.model.response.BossCountInfoVo;
import com.xxl.kfapp.model.response.MemberInfoVo;
import com.xxl.kfapp.model.response.ShopApplyStatusVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.util.klog.KLog;

public class HomeFragmentShopkeeper extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.tv_totaldayamount)
    TextView tvDayAmount;
    @Bind(R.id.tv_totalamount)
    TextView tvAmount;
    @Bind(R.id.tv_totalbalance)
    TextView tvBalance;
    @Bind(R.id.tv_shopcnt)
    TextView tvShopcnt;
    @Bind(R.id.tv_online)
    TextView tvOnline;
    @Bind(R.id.tv_offline)
    TextView tvOffline;
    @Bind(R.id.tv_totalbbcnt)
    TextView tvBarberCnt;
    @Bind(R.id.tv_totalworkcnt)
    TextView tvWorkCnt;
    @Bind(R.id.tv_totalworkcdcnt)
    TextView tvWorkcdCnt;
    @Bind(R.id.tv_totalworkztcnt)
    TextView tvWorkztCnt;
    @Bind(R.id.tv_totalcnt)
    TextView tvTotalcnt;
    @Bind(R.id.tv_totaldaycnt)
    TextView tvDaycnt;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.mImage)
    ImageView ivHeadpic;
    @Bind(R.id.lyt_totalamount)
    LinearLayout lytTotalAmount;
    @Bind(R.id.lyt_totaldayamount)
    LinearLayout lytTotalDayAmount;
    @Bind(R.id.lyt_totalbalance)
    LinearLayout lytTotalBalance;
    @Bind(R.id.lyt_totalbbcnt)
    LinearLayout lytTotalBBCnt;
    @Bind(R.id.lyt_totalworkcnt)
    LinearLayout lytTotalWorkCnt;
    @Bind(R.id.lyt_unnormal)
    LinearLayout lytUnnormal;
    @Bind(R.id.lyt_totalcnt)
    LinearLayout lytTotalCnt;
    @Bind(R.id.lyt_totaldaycnt)
    LinearLayout lytTotalDayCnt;
    @Bind(R.id.lyt_apply)
    LinearLayout lytApply;
    @Bind(R.id.iv_close)
    ImageView ivClose;
    @Bind(R.id.iv_kfs)
    ImageView ivKfs;

    private boolean isFirst = true, isShow = true;
    private Drawable male, female;
    private ApplyListVo applyListVo;

    @Override
    protected void initArgs(Bundle bundle) {
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.fragment_home_shopkeeper);
        ButterKnife.bind(this, mView);
        tvShopcnt.setOnClickListener(this);
        tvOnline.setOnClickListener(this);
        tvOffline.setOnClickListener(this);
        lytTotalAmount.setOnClickListener(this);
        lytTotalBalance.setOnClickListener(this);
        lytTotalBBCnt.setOnClickListener(this);
        lytTotalCnt.setOnClickListener(this);
        lytTotalDayAmount.setOnClickListener(this);
        lytTotalDayCnt.setOnClickListener(this);
        lytTotalWorkCnt.setOnClickListener(this);
        lytUnnormal.setOnClickListener(this);
        lytApply.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        ivKfs.setVisibility(View.GONE);
        ivHeadpic.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        initDrawables();
        setMemberInfo();
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent();
        switch (v.getId()) {
            case R.id.tv_shopcnt:
                startActivity(new Intent(getActivity(), ShopListActivity.class));
                break;
            case R.id.tv_online:
                startActivity(new Intent(getActivity(), ShopListActivity.class));
                break;
            case R.id.tv_offline:
                startActivity(new Intent(getActivity(), ShopListActivity.class));
                break;
            case R.id.lyt_totalamount:
                i.setClass(getActivity(), ShopAmountActivity.class);
                i.putExtra("isToday", false);
                startActivity(i);
                break;
            case R.id.lyt_totalbalance:
                i.setClass(getActivity(), WithdrawListActivity.class);
                startActivity(i);
                break;
            case R.id.lyt_totalbbcnt:
                i.setClass(getActivity(), BarberListActivity.class);
                startActivity(i);
                break;
            case R.id.lyt_totalcnt:
                i.setClass(getActivity(), TicketListActivity.class);
                i.putExtra("isToday", false);
                i.putExtra("isBoss", true);
                startActivity(i);
                break;
            case R.id.lyt_totaldayamount:
                i.setClass(getActivity(), ShopAmountActivity.class);
                i.putExtra("isToday", true);
                startActivity(i);
                break;
            case R.id.lyt_totaldaycnt:
                i.setClass(getActivity(), TicketListActivity.class);
                i.putExtra("isToday", true);
                i.putExtra("isBoss", true);
                startActivity(i);
                break;
            case R.id.lyt_totalworkcnt:
                i.setClass(getActivity(), CheckInActivity.class);
                i.putExtra("isNormal", true);
                startActivity(i);
                break;
            case R.id.lyt_unnormal:
                i.setClass(getActivity(), CheckInActivity.class);
                i.putExtra("isNormal", false);
                startActivity(i);
                break;
            case R.id.iv_close:
                lytApply.setVisibility(View.GONE);
                PreferenceUtils.setPrefBoolean(BaseApplication.getContext(), "isFirst", false);
                break;
            case R.id.lyt_apply:
                start2Apply(applyListVo);
                break;
            case R.id.mImage:
                startActivity(new Intent(getActivity(), PersonInfoActivity.class));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        doGetBossCountInfo();
        StatService.onResume(this);
    }

    private void setMemberInfo() {
        MemberInfoVo infoVo = (MemberInfoVo) mACache.getAsObject("memberInfoVo");
        if (TextUtils.isEmpty(infoVo.getHeadpic())) {
            ivHeadpic.setImageResource(R.mipmap.default_head);
        } else {
            Glide.with(BaseApplication.getContext()).load(infoVo.getHeadpic()).into(ivHeadpic);
        }
        tvNickname.setText(infoVo.getNickname());
        if ("1".equals(infoVo.getSex())) {
            tvNickname.setCompoundDrawables(null, null, male, null);
        } else if ("2".equals(infoVo.getSex())) {
            tvNickname.setCompoundDrawables(null, null, female, null);
        }
    }

    @SuppressWarnings("deprecation")
    private void initDrawables() {
        male = getActivity().getResources().getDrawable(R.mipmap.main_icon_boy);
        female = getActivity().getResources().getDrawable(R.mipmap.main_icon_girl);
        male.setBounds(0, 0, male.getMinimumWidth(), male.getMinimumHeight());
        female.setBounds(0, 0, female.getMinimumWidth(), female.getMinimumHeight());
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
                                if (applyListVo != null && applyListVo.getApplylst().size() != 0 && PreferenceUtils
                                        .getPrefBoolean(BaseApplication.getContext(), "isFirst", false)) {
                                    PreferenceUtils.setPrefString(BaseApplication.getContext(),
                                            "applyid", applyListVo.getApplylst().get(0).getApplyid());
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

    private void start2Apply(ApplyListVo applyListVo) {
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
                doGetShopApplyStatus();
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
                                ShopApplyStatusVo shopStatusVo = gson.fromJson(json.getString("data"),
                                        ShopApplyStatusVo.class);
                                PreferenceUtils.setPrefString(BaseApplication.getContext(), "applyid", shopStatusVo
                                        .getApplyid());
                                //                                if (!TextUtils.isEmpty(shopStatusVo
                                // .getPrepaychecksts())) {
                                //                                    Intent i = new Intent(getActivity(),
                                // JmkdFivePrepayActivity.class);
                                //                                    i.putExtra("shopStatusVo", shopStatusVo);
                                //                                    startActivity(i);
                                //                                } else
                                if (TextUtils.isEmpty(applyListVo.getApplylst().get(0).getShopid())) {
                                    startActivity(new Intent(getActivity(), JmkdFiveActivity.class));
                                } else {
                                    startActivity(new Intent(getActivity(), JmkdFive3WebActivity.class));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @SuppressWarnings("deprecation")
    private void doGetBossCountInfo() {
        String token = PreferenceUtils.getPrefString(BaseApplication.getContext(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getBossCountInfo)
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
                                BossCountInfoVo vo = gson.fromJson(json.getString("data"), BossCountInfoVo.class);
                                KLog.d(vo.toString());
                                tvAmount.setText(vo.getTotalamount() + "");
                                tvBalance.setText(vo.getTotalbalance() + "");
                                tvBarberCnt.setText("理发师：" + vo.getTotalbbcnt());
                                tvDayAmount.setText(vo.getTotaldayamount() + "");
                                tvDaycnt.setText("今日理发：" + vo.getTotaldaycnt());
                                tvOffline.setText(Html.fromHtml("今日离线：<font color='#bf4740'>"
                                        + vo.getOffline() + "</font>"));
                                tvOnline.setText(Html.fromHtml("今日正常：<font color='#34A853'>"
                                        + vo.getOnline() + "</font>"));
                                tvShopcnt.setText(Html.fromHtml("我的店铺：<font color='#34A853'>"
                                        + vo.getShopcnt() + "</font>"));//#EA923A
                                tvTotalcnt.setText("累计理发：" + vo.getTotalcnt());
                                tvWorkcdCnt.setText(Html.fromHtml("迟到：<font color='#EA923A'>"
                                        + vo.getTotalworkcdcnt() + "</font>"));
                                tvWorkCnt.setText("今日上班：" + vo.getTotalworkcnt());
                                tvWorkztCnt.setText(Html.fromHtml("早退：<font color='#EA923A'>"
                                        + vo.getTotalworkztcnt() + "</font>"));
                                isShow = "1".equals(vo.getApplyflg());
                                isFirst = PreferenceUtils.getPrefBoolean(BaseApplication.getContext(), "isFirst",
                                        false);
                                if (isFirst && isShow) {
                                    getMemberShopApply();
                                } else {
                                    lytApply.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
