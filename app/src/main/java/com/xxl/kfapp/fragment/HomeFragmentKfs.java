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

import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.home.boss.TicketListActivity;
import com.xxl.kfapp.activity.home.kfs.SignInActivity;
import com.xxl.kfapp.activity.person.PersonInfoActivity;
import com.xxl.kfapp.base.BaseApplication;
import com.xxl.kfapp.base.BaseFragment;
import com.xxl.kfapp.model.response.BarberCountInfoVo;
import com.xxl.kfapp.model.response.MemberInfoVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.util.klog.KLog;
import talex.zsw.baselibrary.widget.CircleImageView;

public class HomeFragmentKfs extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.tv_integrate)
    TextView tvIntegrate;
    @Bind(R.id.tv_worktime)
    TextView tvWorktime;
    @Bind(R.id.tv_workaddr)
    TextView tvWorkaddr;
    @Bind(R.id.tv_cut_today)
    TextView tvCutToday;
    @Bind(R.id.tv_cut_count)
    TextView tvCutCount;
    @Bind(R.id.tv_sign_time)
    TextView tvSignTime;
    @Bind(R.id.tv_sign_out_time)
    TextView tvSignOutTime;
    @Bind(R.id.mImage)
    CircleImageView ivHeadpic;
    @Bind(R.id.iv_sign_in)
    ImageView ivSignIn;
    @Bind(R.id.iv_late)
    ImageView ivLate;
    @Bind(R.id.iv_sign_out)
    ImageView ivSignOut;
    @Bind(R.id.iv_tui)
    ImageView ivTui;
    @Bind(R.id.ll_daytotal)
    LinearLayout llDaytotal;
    @Bind(R.id.ll_total)
    LinearLayout llTodal;
    @Bind(R.id.ll_sign_in)
    LinearLayout llSignIn;
    @Bind(R.id.ll_sign_out)
    LinearLayout llSignOut;

    private Drawable male, female;
    private BarberCountInfoVo vo;

    @Override
    public void onClick(View v) {
        Intent i = new Intent();
        switch (v.getId()) {
            case R.id.ll_daytotal:
                i.setClass(getActivity(), TicketListActivity.class);
                i.putExtra("isToday", true);
                i.putExtra("isBoss", false);
                startActivity(i);
                break;
            case R.id.ll_total:
                i.setClass(getActivity(), TicketListActivity.class);
                i.putExtra("isToday", false);
                i.putExtra("isBoss", false);
                startActivity(i);
                break;
            case R.id.ll_sign_in:
                i.setClass(getActivity(), SignInActivity.class);
                i.putExtra("isSignIn", true);
                i.putExtra("shopid", vo.getShopid());
                startActivity(i);
                break;
            case R.id.ll_sign_out:
                i.setClass(getActivity(), SignInActivity.class);
                i.putExtra("isSignIn", false);
                i.putExtra("shopid", vo.getShopid());
                startActivity(i);
                break;
            case R.id.mImage:
                startActivity(new Intent(getActivity(), PersonInfoActivity.class));
                break;
        }
    }

    @Override
    protected void initArgs(Bundle bundle) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.fragment_home_kfs);
        ButterKnife.bind(this, mView);
        llDaytotal.setOnClickListener(this);
        llTodal.setOnClickListener(this);
        llSignIn.setOnClickListener(this);
        llSignOut.setOnClickListener(this);
        ivHeadpic.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        initDrawables();
        setMemberInfo();
        getBarberCountInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
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

    @SuppressWarnings("deprecation")
    private void getBarberCountInfo() {
        String token = PreferenceUtils.getPrefString(BaseApplication.getContext(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getBarberCountInfo)
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
                                vo = gson.fromJson(json.getString("data"), BarberCountInfoVo.class);
                                tvIntegrate.setText(vo.getIntegrate());
                                if ("null".equals(vo.getBegintime()) || TextUtils.isEmpty(vo.getBegintime()))
                                    vo.setBegintime("");
                                if ("null".equals(vo.getEndtime()) || TextUtils.isEmpty(vo.getBegintime()))
                                    vo.setEndtime("");
                                tvWorktime.setText("工作时间：" + vo.getBegintime() + "-" + vo.getEndtime());
                                tvWorkaddr.setText("工作地址：" + vo.getShopname());
                                tvCutToday.setText(Html.fromHtml("<font color='#028dd2'>"
                                        + vo.getDaytotal() + "</font>"));
                                tvCutCount.setText(Html.fromHtml("<font color='#028dd2'>"
                                        + vo.getTotal() + "</font>"));
                                tvSignTime.setText(vo.getSignfromtime());
                                tvSignOutTime.setText(vo.getSignendtime());
                                if (!TextUtils.isEmpty(vo.getFromsts()) && "2".equals(vo.getFromsts())) {
                                    ivSignIn.setVisibility(View.GONE);
                                    ivLate.setVisibility(View.VISIBLE);
                                } else if ("1".equals(vo.getFromsts())) {
                                    ivSignIn.setVisibility(View.VISIBLE);
                                    ivLate.setVisibility(View.GONE);
                                } else {
                                    ivSignIn.setVisibility(View.GONE);
                                    ivLate.setVisibility(View.GONE);
                                }
                                if (!TextUtils.isEmpty(vo.getEndsts()) && "3".equals(vo.getEndsts())) {
                                    ivSignOut.setVisibility(View.GONE);
                                    ivTui.setVisibility(View.VISIBLE);
                                } else if ("1".equals(vo.getEndsts())) {
                                    ivSignOut.setVisibility(View.VISIBLE);
                                    ivTui.setVisibility(View.GONE);
                                } else {
                                    ivSignOut.setVisibility(View.GONE);
                                    ivTui.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
