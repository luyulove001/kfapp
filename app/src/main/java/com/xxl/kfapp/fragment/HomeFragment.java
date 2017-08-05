package com.xxl.kfapp.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.home.jmkd.JmkdFive2Activity;
import com.xxl.kfapp.activity.home.jmkd.JmkdFive3WebActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdFiveActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdFivePrepayActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdFourActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdOneActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdSixActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdThreeActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdTwoActivity;
import com.xxl.kfapp.activity.home.register.RegisterKfsFiveActivity;
import com.xxl.kfapp.activity.home.register.RegisterKfsFourActivity;
import com.xxl.kfapp.activity.home.register.RegisterKfsOneActivity;
import com.xxl.kfapp.activity.home.register.RegisterKfsThreeActivity;
import com.xxl.kfapp.activity.home.register.RegisterKfsTwoActivity;
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
import talex.zsw.baselibrary.util.klog.KLog;
import talex.zsw.baselibrary.widget.CircleImageView;

/**
 * 作者：XNN
 * 日期：2017/6/1
 * 作用：首页
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.btn_begin)
    ImageView btnBegin;
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
    }

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
        doGetBarberGoodPic();
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
                //                startActivity(new Intent(getActivity(), ShopDetailActivity.class));
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
                switch (applyStatus) {
                    case "20":
                        Intent i20 = new Intent(getActivity(), JmkdOneActivity.class);
                        i20.putExtra("shopStatusVo", shopStatusVo);
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
                        } else if (TextUtils.isEmpty(shopid)) {
                            startActivity(new Intent(getActivity(), JmkdFiveActivity.class));
                        } else {
                            startActivity(new Intent(getActivity(), JmkdFive3WebActivity.class));
                        }
                        break;
                    case "25":
                        startActivity(new Intent(getActivity(), JmkdSixActivity.class));
                        break;
                    case "26":
                        break;
                }
                break;
            case R.id.btn_get_job:
                ToastShow("该功能正在开发中，尽情期待！");
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setMemberInfo();
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
        if ("0".equals(vo.getSex())) {
            nickname.setCompoundDrawables(null, null, male, null);
        } else if ("1".equals(vo.getSex())) {
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
                                ToastShow(response.body());
                                barberStatusVo = gson.fromJson(json.getString("data"), ApplyStatusVo.class);
                                applyStatus = barberStatusVo.getApplysts();
                                switch (barberStatusVo.getApplysts()) {
                                    //todo 设置进度显示
                                    case "10":
                                        break;
                                    case "11":
                                        break;
                                    case "12":
                                        break;
                                    case "13":
                                        break;
                                    case "14":
                                        break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
                                applyStatus = shopStatusVo.getApplysts();
                                shopid = shopStatusVo.getShopid();
                                prepaychecksts = shopStatusVo.getPrepaychecksts();
                                devicechecksts = shopStatusVo.getDevicechecksts();
                                switch (shopStatusVo.getApplysts()) {
                                    //todo 设置进度显示
                                    case "20":
                                        break;
                                    case "21":
                                        break;
                                    case "22":
                                        break;
                                    case "23":
                                        break;
                                    case "24":
                                        break;
                                    case "25":
                                        break;
                                    case "26":
                                        break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
