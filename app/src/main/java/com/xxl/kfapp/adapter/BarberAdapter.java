package com.xxl.kfapp.adapter;


import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxl.kfapp.R;
import com.xxl.kfapp.base.BaseApplication;
import com.xxl.kfapp.model.response.StaffVo;

import java.util.List;

public class BarberAdapter extends BaseQuickAdapter<StaffVo.Staff> implements View.OnClickListener{

    public BarberAdapter(List<StaffVo.Staff> data) {
        super(R.layout.item_barber, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, StaffVo.Staff staff) {
        baseViewHolder.setText(R.id.tv_shopname, staff.getShopname());
        baseViewHolder.setText(R.id.tv_name, staff.getRealname());
        baseViewHolder.setText(R.id.tv_phone, staff.getPhone());
        baseViewHolder.setText(R.id.tv_staffno, "工号：" + staff.getStaffno());
        ImageView ivShop = baseViewHolder.getView(R.id.iv_headpic);
        Glide.with(BaseApplication.getContext()).load(staff.getHeadpic()).into(ivShop);

        baseViewHolder.setOnClickListener(R.id.lyt_barber_info, this);
        baseViewHolder.setOnClickListener(R.id.tv_checking_in, this);
        baseViewHolder.setOnClickListener(R.id.tv_performance, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyt_barber_info:
                break;
            case R.id.tv_checking_in:
                break;
            case R.id.tv_performance:
                break;
        }
    }
}
