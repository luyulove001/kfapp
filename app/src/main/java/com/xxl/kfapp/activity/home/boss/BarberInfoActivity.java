package com.xxl.kfapp.activity.home.boss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.common.ImageShower;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.TicketListVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.util.klog.KLog;
import talex.zsw.baselibrary.widget.CircleImageView;

public class BarberInfoActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.civ_head)
    CircleImageView civHead;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.tv_staffno)
    TextView tvStaffno;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.tv_entrytime)
    TextView tvEntrytime;
    @Bind(R.id.tv_idcard)
    TextView tvIdcard;
    @Bind(R.id.iv_idcard_pos)
    ImageView ivIdCardPos;
    @Bind(R.id.iv_idcard_neg)
    ImageView ivIdCardNeg;
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;

    private String cardPos, cardNeg, shopid, barberid;

    @Override
    protected void initArgs(Intent intent) {
        shopid = intent.getStringExtra("shopid");
        barberid = intent.getStringExtra("barberid");
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_barber_info);
        ButterKnife.bind(this);
        mTitleBar.setTitle("理发师详情");
        mTitleBar.setBackOnclickListener(this);
        ivIdCardNeg.setOnClickListener(this);
        ivIdCardPos.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        getStaffDetailInfo();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_idcard_pos:
                showImage(cardPos);
                break;
            case R.id.iv_idcard_neg:
                showImage(cardNeg);
                break;
        }
    }

    private void showImage(String image) {
        Intent i = new Intent(this, ImageShower.class);
        i.putExtra("image", image);
        startActivity(i);
    }

    private void getStaffDetailInfo() {
        String token = PreferenceUtils.getPrefString(getApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getStaffDetailInfo)
                .tag(this)
                .params("token", token)
                .params("shopid", shopid)
                .params("barberid", barberid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                if (!TextUtils.isEmpty(json.getString("data"))) {
                                    json = json.getJSONObject("data");
                                    tvEntrytime.setText("入职时间:  " + json.getString("workdate"));
                                    tvStaffno.setText("工号:  " + json.getString("staffno"));
                                    tvNickname.setText("姓名:  " + json.getString("realname"));
                                    tvIdcard.setText("身份证号码:  " + json.getString("cardid"));
                                    tvPhone.setText("手机:  " + json.getString("formatphone"));
                                    cardPos = json.getString("cardpos");
                                    cardNeg = json.getString("cardneg");
                                    Glide.with(getApplicationContext()).load(json.getString("headpic")).into(civHead);
                                    Glide.with(getApplicationContext()).load(cardPos).into(ivIdCardPos);
                                    Glide.with(getApplicationContext()).load(cardNeg).into(ivIdCardNeg);
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
}
