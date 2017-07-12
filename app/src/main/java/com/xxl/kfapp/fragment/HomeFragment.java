package com.xxl.kfapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.home.RegisterKfsOneActivity;
import com.xxl.kfapp.base.BaseFragment;
import com.xxl.kfapp.widget.LinedEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
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
    @Bind(R.id.lineText)
    LinedEditText lineText;

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
                startActivity(new Intent(getActivity(), RegisterKfsOneActivity.class));
                break;
        }
    }

}
