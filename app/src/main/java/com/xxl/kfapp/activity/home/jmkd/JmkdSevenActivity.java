package com.xxl.kfapp.activity.home.jmkd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.baidu.mobstat.StatService;
import com.xxl.kfapp.R;
import com.xxl.kfapp.adapter.ProgressAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.fragment.HomeFragmentShopkeeper;
import com.xxl.kfapp.model.response.ProgressVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class JmkdSevenActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.pRecyclerView)
    RecyclerView pRecyclerView;
    @Bind(R.id.next)
    Button next;
    private ProgressAdapter progressAdapter;
    private String applyid;

    @Override
    protected void initArgs(Intent intent) {
        applyid = intent.getStringExtra("applyid");
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_jmkd_seven);
        ButterKnife.bind(this);
        mTitleBar.setTitle("开店申请");
        mTitleBar.setBackOnclickListener(this);
        next.setOnClickListener(this);
    }


    @Override
    protected void initData() {
        initInfoRecycleView();
        if (TextUtils.isEmpty(applyid)) {
            applyid = PreferenceUtils.getPrefString(getApplication(), "applyid", "");
        }
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
            case R.id.next:
                startActivity(new Intent(JmkdSevenActivity.this, HomeFragmentShopkeeper.class));
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
        //        pRecyclerView.smoothScrollToPosition();

    }

    private void setData() {
        List<ProgressVo> progressVos = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            ProgressVo vo = new ProgressVo();
            if (i == 0) {
                vo.setName("申请开店");
                vo.setTag(2);
            } else if (i == 1) {
                vo.setName("审核");
                vo.setTag(2);
            } else if (i == 2) {
                vo.setName("阅读协议");
                vo.setTag(2);
            } else if (i == 3) {
                vo.setName("品牌保证金");
                vo.setTag(2);
            } else if (i == 4) {
                vo.setName("选址");
                vo.setTag(2);
            } else if (i == 5) {
                vo.setName("装修设备");
                vo.setTag(2);
            } else if (i == 6) {
                vo.setName("加盟成功");
                vo.setTag(1);
            }

            progressVos.add(vo);
        }
        progressAdapter.setNewData(progressVos);
    }
}
