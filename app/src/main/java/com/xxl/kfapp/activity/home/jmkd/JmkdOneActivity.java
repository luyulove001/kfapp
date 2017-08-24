package com.xxl.kfapp.activity.home.jmkd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mobstat.StatService;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.GetRequest;
import com.xxl.kfapp.R;
import com.xxl.kfapp.adapter.ProgressAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.ProgressVo;
import com.xxl.kfapp.model.response.ShopApplyInfoVo;
import com.xxl.kfapp.model.response.ShopApplyStatusVo;
import com.xxl.kfapp.utils.AddressPickTask;
import com.xxl.kfapp.utils.AddressPickTaskAll;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import talex.zsw.baselibrary.util.klog.KLog;

/**
 * 作者：XNN
 * 日期：2017/6/7
 * 作用：加盟开店第一步..
 */

public class JmkdOneActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.pRecyclerView)
    RecyclerView pRecyclerView;
    @Bind(R.id.mScrollView)
    ScrollView mScrollView;
    @Bind(R.id.next)
    Button next;
    @Bind(R.id.lyt_want_address)
    LinearLayout lytWantAddress;
    @Bind(R.id.tv_want_address)
    TextView tvWantAddress;

    private ProgressAdapter progressAdapter;
    private List<ProgressVo> progressVos;
    private ShopApplyInfoVo shopInfoVo;
    private String applyid;

    @Override
    protected void initArgs(Intent intent) {
        applyid = intent.getStringExtra("applyid");
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_jmkd_one);
        ButterKnife.bind(this);
        next.setOnClickListener(this);
        mTitleBar.setTitle("开店申请");
        mTitleBar.setBackOnclickListener(this);
        lytWantAddress.setOnClickListener(this);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        shopInfoVo = new ShopApplyInfoVo();
        initInfoRecycleView();
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
                doUpdateShopApply();
                break;
            case R.id.lyt_want_address:
                onAddressPicker();
                break;
        }
    }

    public void onAddressPicker() {
        AddressPickTaskAll task = new AddressPickTaskAll(this);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(new AddressPickTaskAll.Callback() {
            @Override
            public void onAddressInitFailed() {
                ToastShow("数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                if (county == null) {
                    ToastShow(province.getAreaName() + city.getAreaName());
                    tvWantAddress.setText(province.getAreaName() + city.getAreaName());
                    shopInfoVo.setAddprovincecode(province.getAreaId());
                    shopInfoVo.setAddprovincename(province.getAreaName());
                    shopInfoVo.setAddcitycode(province.getAreaId());
                    shopInfoVo.setAddcityname(province.getAreaName());
                    shopInfoVo.setAddareacode(city.getAreaId());
                    shopInfoVo.setAddareaname(city.getAreaName());
                } else {
                    ToastShow(province.getAreaName() + city.getAreaName() + county.getAreaName());
                    tvWantAddress.setText(province.getAreaName() + city.getAreaName() + county.getAreaName());
                    shopInfoVo.setAddprovincecode(province.getAreaId());
                    shopInfoVo.setAddprovincename(province.getAreaName());
                    shopInfoVo.setAddcitycode(city.getAreaId());
                    shopInfoVo.setAddcityname(city.getAreaName());
                    shopInfoVo.setAddareacode(county.getAreaId());
                    shopInfoVo.setAddareaname(county.getAreaName());
                }
            }
        });
        task.execute("浙江", "杭州", "滨江");
    }

    /**
     * 初始化progress列表
     */
    private void initInfoRecycleView() {
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;

        progressAdapter = new ProgressAdapter(new ArrayList<ProgressVo>(), width / 4);
        progressAdapter.openLoadAnimation();
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
        progressVos = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            ProgressVo vo = new ProgressVo();
            if (i == 0) {
                vo.setName("申请加盟");
                vo.setTag(1);
            } else if (i == 1) {
                vo.setName("审核");
            } else if (i == 2) {
                vo.setName("阅读协议");
            } else if (i == 3) {
                vo.setName("品牌保证金");
            } else if (i == 4) {
                vo.setName("选址");
            } else if (i == 5) {
                vo.setName("装修设备");
            } else if (i == 6) {
                vo.setName("加盟成功");
            }

            progressVos.add(vo);
        }
        progressAdapter.setNewData(progressVos);
    }

    private void doUpdateShopApply() {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        GetRequest<String> request = OkGo.<String>get(Urls.baseUrl + Urls.updateShopApply)
                .tag(this)
                .params("token", token)
                .params("addprovincecode", shopInfoVo.getAddprovincecode())
                .params("addcitycode", shopInfoVo.getAddcitycode())
                .params("addareacode", shopInfoVo.getAddareacode())
                .params("applysts", "21");
        if (!TextUtils.isEmpty(applyid)) {
            request.params("applyid", applyid);
        }
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                try {
                    JSONObject json = JSON.parseObject(response.body());
                    String code = json.getString("code");
                    if (code.equals("100000")) {
                        KLog.i(response.body());
                        startActivity(new Intent(JmkdOneActivity.this, JmkdTwoActivity.class));
                        finish();
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
