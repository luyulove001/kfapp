package com.xxl.kfapp.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xxl.kfapp.R;
import com.xxl.kfapp.adapter.KSNRAdapter;
import com.xxl.kfapp.adapter.KSTMAdapter;
import com.xxl.kfapp.adapter.ProgressAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.KSNRVo;
import com.xxl.kfapp.model.response.KSTMVo;
import com.xxl.kfapp.model.response.ProgressVo;
import com.xxl.kfapp.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.view.RecyleView.FullyLinearLayoutManager;

/**
 * 作者：XNN
 * 日期：2017/6/7
 * 作用：注册快发师第四步 考试
 */

public class RegisterKfsFourActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.pRecyclerView)
    RecyclerView pRecyclerView;
    @Bind(R.id.tvTotal)
    TextView tvTotal;
    @Bind(R.id.ksRecyclerView)
    RecyclerView ksRecyclerView;
    @Bind(R.id.ksTitle)
    TextView ksTitle;
    @Bind(R.id.tmRecyclerView)
    RecyclerView tmRecyclerView;
    @Bind(R.id.tvUp)
    TextView tvUp;
    @Bind(R.id.tvNext)
    TextView tvNext;
    @Bind(R.id.mScrollView)
    ScrollView mScrollView;
    @Bind(R.id.next)
    Button next;
    private ProgressAdapter progressAdapter;
    private KSTMAdapter kstmAdapter;
    private KSNRAdapter ksnrAdapter;
    private List<ProgressVo> progressVos;
    private List<KSTMVo> kstmVoList;
    private List<KSNRVo> ksnrVoList;

    @Override
    protected void initArgs(Intent intent) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_registerkfs_four);
        ButterKnife.bind(this);
        next.setOnClickListener(this);
        mTitleBar.setTitle("注册快发师申请");
        mTitleBar.setBackOnclickListener(this);
    }

    @Override
    protected void initData() {
        initInfoRecycleView();
        initTMRecycleView();
        initTMNRRecycleView();

        setKSData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                startActivity(new Intent(this, RegisterKfsFiveActivity.class));
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
        setLCData();
//        pRecyclerView.smoothScrollToPosition();

    }

    /*设置流程数据*/
    private void setLCData() {
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
                vo.setTag(1);
            } else if (i == 4) {
                vo.setName("申请成功");
            }

            progressVos.add(vo);
        }
        progressAdapter.setNewData(progressVos);
    }


    /*设置快速选择考试数据*/
    private void setKSData() {
        kstmVoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            KSTMVo vo = new KSTMVo();
            vo.setNum(i + 1);
            vo.setTitle(i + 1 + "第" + i + 1 + "题");
            ksnrVoList = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                KSNRVo nrvo = new KSNRVo();
                nrvo.setInfo(j + 1 + "第" + j + 1 + "个选项");
                ksnrVoList.add(nrvo);
            }
            vo.setInfo(ksnrVoList);
            kstmVoList.add(vo);
        }

        kstmAdapter.setNewData(kstmVoList);
        ksnrAdapter.setNewData(ksnrVoList);

    }

    /**
     * 初始化快速选择题目列表
     */
    private void initTMRecycleView() {


        kstmAdapter = new KSTMAdapter(new ArrayList<KSTMVo>());
        kstmAdapter.openLoadAnimation();
        ksRecyclerView.setAdapter(kstmAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        ksRecyclerView.setLayoutManager(layoutManager);

//        ksRecyclerView.smoothScrollToPosition();

    }

    /**
     * 初始化题目内容列表
     */
    private void initTMNRRecycleView() {


        ksnrAdapter = new KSNRAdapter(new ArrayList<KSNRVo>());
        ksnrAdapter.openLoadAnimation();
        tmRecyclerView.setAdapter(ksnrAdapter);

        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(false);
        layoutManager.setAutoMeasureEnabled(true);
        tmRecyclerView.setLayoutManager(layoutManager);
//        ksRecyclerView.smoothScrollToPosition();

    }


}
