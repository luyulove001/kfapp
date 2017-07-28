package com.xxl.kfapp.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xxl.kfapp.R;
import com.xxl.kfapp.base.BaseApplication;
import com.xxl.kfapp.base.BaseFragment;
import com.xxl.kfapp.model.response.MemberInfoVo;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeFragmentKfs extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.mImage)
    ImageView ivHeadpic;

    private Drawable male, female;

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initArgs(Bundle bundle) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.fragment_home_kfs);
        ButterKnife.bind(this, mView);
    }

    @Override
    protected void initData() {
        initDrawables();
        setMemberInfo();
    }

    private void setMemberInfo() {
        MemberInfoVo infoVo = (MemberInfoVo) mACache.getAsObject("memberInfoVo");
        Glide.with(BaseApplication.getContext()).load(infoVo.getHeadpic()).into(ivHeadpic);
        tvNickname.setText(infoVo.getNickname());
        if ("0".equals(infoVo.getSex())) {
            tvNickname.setCompoundDrawables(null, null, male, null);
        } else if ("1".equals(infoVo.getSex())) {
            tvNickname.setCompoundDrawables(null, null, female, null);
        }
    }

    private void initDrawables() {
        male = getActivity().getDrawable(R.mipmap.main_icon_boy);
        female = getActivity().getDrawable(R.mipmap.main_icon_girl);
        male.setBounds(0, 0, male.getMinimumWidth(), male.getMinimumHeight());
        female.setBounds(0, 0, female.getMinimumWidth(), female.getMinimumHeight());
    }
}
