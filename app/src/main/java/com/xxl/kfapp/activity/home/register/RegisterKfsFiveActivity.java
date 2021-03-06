package com.xxl.kfapp.activity.home.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.baidu.mobstat.StatService;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.common.MainActivity;
import com.xxl.kfapp.activity.home.jmkd.JmkdOneActivity;
import com.xxl.kfapp.adapter.ProgressAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.fragment.HomeFragmentKfs;
import com.xxl.kfapp.model.response.MemberInfoVo;
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
 * 作用：注册快发师第五步  注册成功
 */

public class RegisterKfsFiveActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.pRecyclerView)
    RecyclerView pRecyclerView;
    @Bind(R.id.mScrollView)
    ScrollView mScrollView;
    @Bind(R.id.btn_kd)
    Button btnKd;
    @Bind(R.id.btn_qz)
    Button btnQz;
    private ProgressAdapter progressAdapter;
    private List<ProgressVo> progressVos;

    @Override
    protected void initArgs(Intent intent) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_registerkfs_five);
        ButterKnife.bind(this);
        btnKd.setOnClickListener(this);
        btnQz.setOnClickListener(this);
        mTitleBar.setTitle("注册快发师申请");
        mTitleBar.setBackOnclickListener(this);
    }

    @Override
    protected void initData() {
        initInfoRecycleView();
        doGetMemberInfo();
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
            case R.id.btn_kd:
                startActivity(new Intent(this, JmkdOneActivity.class));
                finish();
                break;
            case R.id.btn_qz:
//                startActivity(new Intent(this, MainActivity.class));
                finish();
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
        pRecyclerView.smoothScrollToPosition(4);

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
                vo.setTag(2);
            } else if (i == 2) {
                vo.setName("阅读协议");
                vo.setTag(2);
            } else if (i == 3) {
                vo.setName("考试");
                vo.setTag(2);
            } else if (i == 4) {
                vo.setName("申请成功");
                vo.setTag(2);
            }

            progressVos.add(vo);
        }
        progressAdapter.setNewData(progressVos);
    }

    private void doGetMemberInfo() {
        String token = PreferenceUtils.getPrefString(getApplicationContext(), "token", "1234567890");
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
                                if (!TextUtils.isEmpty(json.getString("data"))) {
                                    MemberInfoVo vo = mGson.fromJson(json.getString("data"), MemberInfoVo.class);
                                    mACache.put("memberInfoVo", vo);//保存个人信息
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
