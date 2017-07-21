package com.xxl.kfapp.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.adapter.KSNRAdapter;
import com.xxl.kfapp.adapter.KSTMAdapter;
import com.xxl.kfapp.adapter.ProgressAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.request.TestAnswerVo;
import com.xxl.kfapp.model.response.ApplyStatusVo;
import com.xxl.kfapp.model.response.KSNRVo;
import com.xxl.kfapp.model.response.KSTMVo;
import com.xxl.kfapp.model.response.ProgressVo;
import com.xxl.kfapp.model.response.QuestionListVo;
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
    private int currentPosition = 5;
    private int currentQuestion = 0;
    private final String[] answer = {"A", "B", "C", "D"};
    private ApplyStatusVo applyStatusVo;

    @Override
    protected void initArgs(Intent intent) {
        applyStatusVo = (ApplyStatusVo) intent.getSerializableExtra("barberStatusVo");
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_registerkfs_four);
        ButterKnife.bind(this);
        next.setOnClickListener(this);
        mTitleBar.setTitle("注册快发师申请");
        mTitleBar.setBackOnclickListener(this);
        tvUp.setOnClickListener(this);
        tvNext.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        initInfoRecycleView();
        initTMRecycleView();
        initTMNRRecycleView();
        doGetBarberQuestionList();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                doCheckBarberTest();
                break;
            case R.id.tvUp:
                currentQuestion = currentQuestion - 1;
                if (currentQuestion < 0) {
                    currentQuestion = 0;
                }
                ksRecyclerView.smoothScrollToPosition(currentQuestion);
                setCurrentQuestion(currentQuestion);
                break;
            case R.id.tvNext:
                currentQuestion = currentQuestion + 1;
                if (currentQuestion > 9) {
                    currentQuestion = 9;
                }
                setCurrentQuestion(currentQuestion);
                ksRecyclerView.smoothScrollToPosition(currentQuestion);
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
    private void setKSData(QuestionListVo vos) {
        kstmVoList = new ArrayList<>();
        for (int i = 0; i < vos.getqList().size(); i++) {
            KSTMVo vo = new KSTMVo();
            vo.setNum(i + 1);
            vo.setTitle(i + 1 + "第" + i + 1 + "题");
            ksnrVoList = new ArrayList<>();
            List<QuestionListVo.Answer> answers = vos.getqList().get(i).getAlist();
            for (int j = 0; j < answers.size(); j++) {
                KSNRVo nrvo = new KSNRVo();
                nrvo.setInfo(j + 1 + "." + answers.get(j).getContent());
                nrvo.setId(answers.get(j).getAnswer());
                ksnrVoList.add(nrvo);
            }
            vo.setInfo(ksnrVoList);
            vo.setQid(vos.getqList().get(i).getQid());
            vo.setQuestion(vos.getqList().get(i).getQuestion());
            kstmVoList.add(vo);
        }

        kstmAdapter.setNewData(kstmVoList);
        ksnrAdapter.setNewData(ksnrVoList);
        setCurrentQuestion(0);
    }

    /**
     * 初始化快速选择题目列表
     */
    private void initTMRecycleView() {


        kstmAdapter = new KSTMAdapter(new ArrayList<KSTMVo>());
        kstmAdapter.openLoadAnimation();
        kstmAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener
                () {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (i > currentPosition - 2) {
                    ksRecyclerView.smoothScrollToPosition(i + 2);
                    currentPosition = i + 2;
                } else if (i >= 2 && i < currentPosition - 2) {
                    ksRecyclerView.smoothScrollToPosition(i - 2);
                    currentPosition = i - 2;
                } else {
                    ksRecyclerView.smoothScrollToPosition(0);
                }
                currentQuestion = i;
                setCurrentQuestion(i);
            }
        });
        ksRecyclerView.setAdapter(kstmAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        ksRecyclerView.setLayoutManager(layoutManager);

        //        ksRecyclerView.smoothScrollToPosition();

    }

    private void setCurrentQuestion(int i) {
        ksnrVoList = kstmVoList.get(i).getInfo();
        ksnrAdapter.setNewData(ksnrVoList);
        ksTitle.setText(i + 1 + "." + kstmVoList.get(i).getQuestion());
    }

    /**
     * 初始化题目内容列表
     */
    private void initTMNRRecycleView() {


        ksnrAdapter = new KSNRAdapter(new ArrayList<KSNRVo>());
        ksnrAdapter.openLoadAnimation();
        ksnrAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener
                () {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                for (KSNRVo vo : ksnrVoList)
                    vo.setXz(false);
                ksnrVoList.get(i).setXz(true);
                kstmVoList.get(currentQuestion).setWc(true);
                kstmAdapter.setNewData(kstmVoList);
                ksnrAdapter.setNewData(ksnrVoList);
            }
        });
        tmRecyclerView.setAdapter(ksnrAdapter);

        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(false);
        layoutManager.setAutoMeasureEnabled(true);
        tmRecyclerView.setLayoutManager(layoutManager);
        //        ksRecyclerView.smoothScrollToPosition();

    }

    private List<TestAnswerVo> getTestAnswer() {
        List<TestAnswerVo> answerVos = new ArrayList<>();
        TestAnswerVo answerVo;
        for (KSTMVo kstmVo : kstmVoList) {
            answerVo = new TestAnswerVo();
            for (KSNRVo ksnrVo : kstmVo.getInfo()) {
                if (ksnrVo.isXz()) {
                    answerVo.setAnswer(answer[Integer.valueOf(ksnrVo.getId()) - 1]);
                }
            }
            answerVo.setQid(kstmVo.getQid());
            answerVos.add(answerVo);
        }
        KLog.i(mGson.toJson(answerVos));
        return answerVos;
    }

    private boolean isFinish() {
        for (KSTMVo kstmVo : kstmVoList) {
            if (!kstmVo.isWc()) {
                sweetDialog("您还有题目未完成，请检查后再提交", 1, false);
                return false;
            }
        }
        return true;
    }

    private void doGetBarberQuestionList() {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getBarberQuestionList)
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
                                QuestionListVo questionListVo = mGson.fromJson(json.getString("data"), QuestionListVo.class);
                                setKSData(questionListVo);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void doCheckBarberTest() {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        if (isFinish()) {
            OkGo.<String>get(Urls.baseUrl + Urls.checkBarberTest)
                    .tag(this)
                    .params("token", token)
                    .params("examno", "1")
                    .params("testlist", mGson.toJson(getTestAnswer()))
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                            try {
                                JSONObject json = new JSONObject(response.body());
                                String code = json.getString("code");
                                if (!code.equals("100000")) {
                                    sweetDialog(json.getString("msg"), 1, false);
                                } else {
                                    //TODO 测试结果
                                    startActivity(new Intent(RegisterKfsFourActivity.this, RegisterKfsFiveActivity.class));
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

}
