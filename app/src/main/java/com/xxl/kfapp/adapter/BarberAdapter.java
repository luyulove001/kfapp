package com.xxl.kfapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.home.boss.CheckInActivity;
import com.xxl.kfapp.activity.home.boss.TicketListActivity;
import com.xxl.kfapp.base.BaseApplication;
import com.xxl.kfapp.model.response.StaffVo;

import java.util.List;

public class BarberAdapter extends BaseQuickAdapter<StaffVo.Staff> {
    private Context mContext;
    private List<StaffVo.Staff> mStaffs;

    public BarberAdapter(List<StaffVo.Staff> data, Context context) {
        super(R.layout.item_barber, data);
        mContext = context;
        mStaffs = data;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final StaffVo.Staff staff) {
        baseViewHolder.setText(R.id.tv_shopname, staff.getShopname());
        baseViewHolder.setText(R.id.tv_name, staff.getRealname());
        baseViewHolder.setText(R.id.tv_phone, staff.getPhone());
        baseViewHolder.setText(R.id.tv_staffno, "工号：" + staff.getStaffno());
        ImageView ivShop = baseViewHolder.getView(R.id.iv_headpic);
        Glide.with(BaseApplication.getContext()).load(staff.getHeadpic()).into(ivShop);
        baseViewHolder.setOnClickListener(R.id.lyt_barber_info, new OnItemChildClickListener());
        baseViewHolder.setOnClickListener(R.id.tv_checking_in, new OnItemChildClickListener());
        baseViewHolder.setOnClickListener(R.id.tv_performance, new OnItemChildClickListener());
        baseViewHolder.setOnClickListener(R.id.tv_fire, new OnItemChildClickListener());
    }

}
