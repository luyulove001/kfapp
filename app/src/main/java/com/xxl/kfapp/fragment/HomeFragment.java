package com.xxl.kfapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.home.JmkdFiveActivity;
import com.xxl.kfapp.activity.home.JmkdFourActivity;
import com.xxl.kfapp.activity.home.JmkdOneActivity;
import com.xxl.kfapp.activity.home.JmkdThreeActivity;
import com.xxl.kfapp.activity.home.JmkdTwoActivity;
import com.xxl.kfapp.activity.home.RegisterKfsFiveActivity;
import com.xxl.kfapp.activity.home.RegisterKfsFourActivity;
import com.xxl.kfapp.activity.home.RegisterKfsOneActivity;
import com.xxl.kfapp.activity.home.RegisterKfsThreeActivity;
import com.xxl.kfapp.activity.home.RegisterKfsTwoActivity;
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
    @Bind(R.id.lineText)
    LinedEditText lineText;
    private ApplyStatusVo barberStatusVo;
    private ShopApplyStatusVo shopStatusVo;
    private Gson gson;
    private String token;
    private String applyStatus;

    @Override
    protected void initArgs(Bundle bundle) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.fragment_home);
        ButterKnife.bind(this, mView);
        btnBegin.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        lineText.setText("谁终将声震人间，必长久深自缄缄默；谁终将点燃闪电，必长久如云漂泊.与怪物战斗的人，应当小心自己不要成为怪物。当你远远凝视深渊时，深渊也在凝视你。");
        gson = new Gson();
        token = PreferenceUtils.getPrefString(getActivity().getApplicationContext(), "token", "1234567890");
        doGetMemberInfo();
        doGetBarberGoodPic();
//        doGetBarberApplyStatus();
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
                        startActivity(new Intent(getActivity(), JmkdFiveActivity.class));
                        break;
                    case "25":
                        break;
                    case "26":
                        break;
                }
            break;
        }
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
                                if ("0".equals(vo.getRole())) {
                                    doGetBarberApplyStatus();
                                } else if ("1".equals(vo.getRole())) {
                                    doGetShopApplyStatus();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
