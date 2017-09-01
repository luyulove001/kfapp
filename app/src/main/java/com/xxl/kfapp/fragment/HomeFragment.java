package com.xxl.kfapp.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.home.jmkd.JmkdFive3WebActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdFiveActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdFivePrepayActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdFourActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdOneActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdSixActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdSixDeviceActivity;
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
import com.xxl.kfapp.model.response.ApplyStatusVo;
import com.xxl.kfapp.model.response.MemberInfoVo;
import com.xxl.kfapp.model.response.ShopApplyStatusVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.LinedEditText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import susion.com.mediaseekbar.MediaSeekBar;
import talex.zsw.baselibrary.util.klog.KLog;
import talex.zsw.baselibrary.widget.CircleImageView;

/**
 * 作者：XNN
 * 日期：2017/6/1
 * 作用：首页
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.btn_begin)
    Button btnBegin;
    @Bind(R.id.mImage)
    CircleImageView mImage;
    @Bind(R.id.textTitle)
    TextView textTitle;
    @Bind(R.id.iv_kfsgood)
    ImageView ivKfsGood;
    @Bind(R.id.iv_kfs)
    ImageView ivKfs;
    @Bind(R.id.lineText)
    LinedEditText lineText;
    @Bind(R.id.lyt_kfs)
    LinearLayout lytKfs;
    @Bind(R.id.btn_create_shop)
    Button btnCreateShop;
    @Bind(R.id.btn_get_job)
    Button btnGetJob;
    @Bind(R.id.tv_nickname)
    TextView nickname;
    @Bind(R.id.msb)
    MediaSeekBar seekBar;
    @Bind(R.id.tv_tips)
    TextView tvTips;
    @Bind(R.id.tv_congratulation)
    TextView tvCongratulation;
    @Bind(R.id.ll_seekbar)
    RelativeLayout llSeekBar;

    private ApplyStatusVo barberStatusVo;
    private ShopApplyStatusVo shopStatusVo;
    private Gson gson;
    private String token;
    private String applyStatus, shopid, prepaychecksts, devicechecksts;
    private Drawable male, female;

    @Override
    protected void initArgs(Bundle bundle) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.fragment_home);
        ButterKnife.bind(this, mView);
        btnBegin.setOnClickListener(this);
        btnCreateShop.setOnClickListener(this);
        btnGetJob.setOnClickListener(this);
        mImage.setOnClickListener(this);
        seekBar.setCanOperator(false);
        seekBar.setMaxProgress(100);
    }

    @SuppressWarnings("deprecation")
    private void initDrawables() {
        male = getActivity().getResources().getDrawable(R.mipmap.main_icon_boy);
        female = getActivity().getResources().getDrawable(R.mipmap.main_icon_girl);
        male.setBounds(0, 0, male.getMinimumWidth(), male.getMinimumHeight());
        female.setBounds(0, 0, female.getMinimumWidth(), female.getMinimumHeight());
    }

    @Override
    protected void initData() {
        initDrawables();
        gson = new Gson();
        token = PreferenceUtils.getPrefString(getActivity().getApplicationContext(), "token", "1234567890");
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_begin:
                //startActivity(new Intent(getActivity(), ShopDetailActivity.class));
                if (TextUtils.isEmpty(applyStatus)) {
                    return;
                }
                switch (applyStatus) {
                    case "10":
                        startActivity(new Intent(getActivity(), RegisterKfsOneActivity.class));
                        break;
                    case "11":
                        Intent i1 = new Intent(getActivity(), RegisterKfsTwoActivity.class);
                        i1.putExtra("barberStatusVo", barberStatusVo);
                        startActivity(i1);
                        break;
                    case "12":
                        startActivity(new Intent(getActivity(), RegisterKfsThreeActivity.class));
                        break;
                    case "13":
                        Intent i3 = new Intent(getActivity(), RegisterKfsFourActivity.class);
                        i3.putExtra("barberStatusVo", barberStatusVo);
                        startActivity(i3);
                        break;
                    case "14":
                        startActivity(new Intent(getActivity(), RegisterKfsFiveActivity.class));
                        break;
                }
                break;
            case R.id.btn_create_shop:
                if (TextUtils.isEmpty(applyStatus)) {
                    ToastShow("数据加载中，请稍后...");
                    return;
                }
                switch (applyStatus) {
                    case "20":
                        Intent i20 = new Intent(getActivity(), JmkdOneActivity.class);
                        i20.putExtra("apply", shopStatusVo.getApplyid());
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
                            if ("0".equals(prepaychecksts)) {
                                startActivity(new Intent(getActivity(), JmkdFive3WebActivity.class));
                            } else if ("1".equals(prepaychecksts)) {
                                Intent i = new Intent(getActivity(), JmkdFivePrepayActivity.class);
                                i.putExtra("shopStatusVo", shopStatusVo);
                                startActivity(i);
                            } else if ("2".equals(prepaychecksts)) {
                                startActivity(new Intent(getActivity(), JmkdFive3WebActivity.class));
                            }
                        } else if (TextUtils.isEmpty(shopid)) {
                            startActivity(new Intent(getActivity(), JmkdFiveActivity.class));
                        } else {
                            startActivity(new Intent(getActivity(), JmkdFive3WebActivity.class));
                        }
                        break;
                    case "25":
                        if (!TextUtils.isEmpty(shopStatusVo.getDevicechecksts())
                                && !"null".equals(shopStatusVo.getDevicechecksts())) {
                            //Intent i = new Intent(getActivity(), JmkdSixDeviceActivity.class);
                            //i.putExtra("applyStatusVo", shopStatusVo);
                            startActivity(new Intent(getActivity(), JmkdSixDeviceActivity.class));
                        } else {
                            startActivity(new Intent(getActivity(), JmkdSixActivity.class));
                        }
                        break;
                    case "26":
                        break;
                }
                break;
            case R.id.btn_get_job:
                ToastShow("该功能正在开发中，尽情期待！");
                break;
            case R.id.mImage:
                startActivity(new Intent(getActivity(), PersonInfoActivity.class));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setMemberInfo();
        StatService.onResume(this);
    }

    private void doGetMemberInfo() {
        OkGo.<String>get(Urls.baseUrl + Urls.getMemberInfo)
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
                                MemberInfoVo vo = gson.fromJson(json.getString("data"), MemberInfoVo.class);
                                mACache.put("memberInfoVo", vo);//保存个人信息
                                Glide.with(BaseApplication.getContext()).load(vo.getHeadpic()).into(mImage);
                                nickname.setText(vo.getNickname());
                                if ("0".equals(vo.getRole())) {
                                    doGetBarberApplyStatus();
                                } else if ("1".equals(vo.getRole())) {
                                    doGetShopApplyStatus();
                                    lytKfs.setVisibility(View.VISIBLE);
                                    ivKfsGood.setVisibility(View.GONE);
                                    btnBegin.setVisibility(View.GONE);
                                    ivKfs.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void setMemberInfo() {
        MemberInfoVo vo = (MemberInfoVo) mACache.getAsObject("memberInfoVo");
        Glide.with(BaseApplication.getContext()).load(vo.getHeadpic()).into(mImage);
        nickname.setText(vo.getNickname());
        if ("1".equals(vo.getSex())) {
            nickname.setCompoundDrawables(null, null, male, null);
        } else if ("2".equals(vo.getSex())) {
            nickname.setCompoundDrawables(null, null, female, null);
        }
        if ("0".equals(vo.getRole())) {
            doGetBarberApplyStatus();
        } else if ("1".equals(vo.getRole())) {
            doGetShopApplyStatus();
            lytKfs.setVisibility(View.VISIBLE);
            ivKfsGood.setVisibility(View.GONE);
            btnBegin.setVisibility(View.GONE);
            ivKfs.setVisibility(View.VISIBLE);
        }
    }

    private void doGetBarberApplyStatus() {
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
                                barberStatusVo = gson.fromJson(json.getString("data"), ApplyStatusVo.class);
                                applyStatus = barberStatusVo.getApplysts();
                                int i = 0;
                                tvTips.setVisibility(View.GONE);
                                switch (barberStatusVo.getApplysts()) {
                                    case "10":
//                                        i = (int) (1 / 5.0 * 100);
                                        doGetBarberGoodPic();
                                        btnBegin.setText("我要成为快发师");
                                        btnBegin.setBackgroundResource(R.mipmap.qc_right_shine);
                                        break;
                                    case "11":
                                        i = (int) (2 / 5.0 * 100);
                                        showBarberTips();
                                        break;
                                    case "12":
                                        i = (int) (3 / 5.0 * 100);
                                        showBarberTips();
                                        break;
                                    case "13":
                                        i = (int) (4 / 5.0 * 100);
                                        showBarberTips();
                                        break;
                                    case "14":
                                        i = (int) (5 / 5.0 * 100);
                                        showBarberTips();
                                        break;
                                }
                                seekBar.setCurrentProgress(i);
                                seekBar.setHasBufferProgress(100);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showBarberTips() {
        tvTips.setText("您还差一点点就可成为快发师");
        tvTips.setVisibility(View.VISIBLE);
        llSeekBar.setVisibility(View.VISIBLE);
    }

    private void doGetShopApplyStatus() {
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
                                shopStatusVo = gson.fromJson(json.getString("data"), ShopApplyStatusVo.class);
                                PreferenceUtils.setPrefString(BaseApplication.getContext(), "applyid", shopStatusVo
                                        .getApplyid());
                                applyStatus = shopStatusVo.getApplysts();
                                shopid = shopStatusVo.getShopid();
                                prepaychecksts = shopStatusVo.getPrepaychecksts();
                                devicechecksts = shopStatusVo.getDevicechecksts();
                                int i = 0;
                                switch (shopStatusVo.getApplysts()) {
                                    case "20":
//                                        i = (int) (1 / 7.0 * 100);
                                        break;
                                    case "21":
                                        i = (int) (2 / 7.0 * 100);
                                        showShopTips();
                                        break;
                                    case "22":
                                        i = (int) (3 / 7.0 * 100);
                                        showShopTips();
                                        break;
                                    case "23":
                                        i = (int) (4 / 7.0 * 100);
                                        showShopTips();
                                        break;
                                    case "24":
                                        i = (int) (5 / 7.0 * 100);
                                        showShopTips();
                                        break;
                                    case "25":
                                        i = (int) (6 / 7.0 * 100);
                                        showShopTips();
                                        break;
                                    case "26":
                                        i = (int) (7 / 7.0 * 100);
                                        showShopTips();
                                        break;
                                }
                                seekBar.setCurrentProgress(i);
                                seekBar.setHasBufferProgress(100);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showShopTips() {
        tvCongratulation.setVisibility(View.GONE);
        btnCreateShop.setText("继续操作");
        llSeekBar.setVisibility(View.VISIBLE);
        tvTips.setVisibility(View.VISIBLE);
    }

    private void doGetBarberGoodPic() {
        OkGo.<String>get(Urls.baseUrl + Urls.getBarberGoodPic)
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
                                Glide.with(getContext()).load(json.getJSONObject("data").getString("yspic")).into
                                        (ivKfsGood);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
