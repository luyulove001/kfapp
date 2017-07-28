package com.xxl.kfapp.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.base.BaseApplication;
import com.xxl.kfapp.base.BaseFragment;
import com.xxl.kfapp.model.response.BossCountInfoVo;
import com.xxl.kfapp.model.response.MemberInfoVo;
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


    private Drawable male, female;

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
    }

    @Override
    protected void initData() {
        initDrawables();
        doGetBossCountInfo();
        setMemberInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_shopcnt:
                break;
            case R.id.tv_online:
                break;
            case R.id.tv_offline:
                break;
            case R.id.lyt_totalamount:
                break;
            case R.id.lyt_totalbalance:
                break;
            case R.id.lyt_totalbbcnt:
                break;
            case R.id.lyt_totalcnt:
                break;
            case R.id.lyt_totaldayamount:
                break;
            case R.id.lyt_totaldaycnt:
                break;
            case R.id.lyt_totalworkcnt:
                break;
            case R.id.lyt_unnormal:
                break;
        }
    }

    private void setMemberInfo() {
        MemberInfoVo infoVo = (MemberInfoVo) mACache.getAsObject("memberInfoVo");
        Glide.with(BaseApplication.getContext()).load(infoVo.getHeadpic()).into(ivHeadpic);
        tvNickname.setText(infoVo.getNickname());
        if ("0".equals(infoVo.getSex())) {
            tvNickname.setCompoundDrawables(null, null, male, null);
        } else if ("1".equals(infoVo.getSex())) {
            tvNickname.setCompoundDrawables(null, null, female, null);
        }
    }

    private void initDrawables() {
        male = getActivity().getDrawable(R.mipmap.main_icon_boy);
        female = getActivity().getDrawable(R.mipmap.main_icon_girl);
        male.setBounds(0, 0, male.getMinimumWidth(), male.getMinimumHeight());
        female.setBounds(0, 0, female.getMinimumWidth(), female.getMinimumHeight());
    }

    private void doGetBossCountInfo() {
        String token = PreferenceUtils.getPrefString(BaseApplication.getContext(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getBossCountInfo)
                .tag(this)
                .params("token", "1234567890")
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
                                tvOffline.setText("今日离线：" + vo.getOffline());
                                tvOnline.setText("今日正常：" + vo.getOnline());
                                tvShopcnt.setText("我的店铺：" + vo.getShopcnt());
                                tvTotalcnt.setText("累计理发：" + vo.getTotalcnt());
                                tvWorkcdCnt.setText("迟到：" + vo.getTotalworkcdcnt());
                                tvWorkCnt.setText("今日上班：" + vo.getTotalworkcnt());
                                tvWorkztCnt.setText("早退：" + vo.getTotalworkztcnt());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
