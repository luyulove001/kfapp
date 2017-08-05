package com.xxl.kfapp.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxl.kfapp.R;
import com.xxl.kfapp.model.response.CheckInVo;

import java.util.List;


public class CheckInAdapter extends BaseQuickAdapter<CheckInVo.CheckVo> {

    public CheckInAdapter(List<CheckInVo.CheckVo> data) {
        super(R.layout.item_barber_check_in, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CheckInVo.CheckVo sign) {
        ImageView ivTui = baseViewHolder.getView(R.id.iv_tui);
        ImageView ivLate = baseViewHolder.getView(R.id.iv_late);
        TextView tvTui = baseViewHolder.getView(R.id.tv_tui);
        TextView tvLate = baseViewHolder.getView(R.id.tv_late);
        if ("1".equals(sign.getEndsts())) {
            tvTui.setText("正常");
            ivTui.setVisibility(View.GONE);
            tvTui.setVisibility(View.VISIBLE);
        } else if ("3".equals(sign.getEndsts())) {
            tvTui.setVisibility(View.GONE);
            ivTui.setVisibility(View.VISIBLE);
            ivTui.setImageResource(R.mipmap.ic_tui);
        }

        if ("1".equals(sign.getFromsts())) {
            baseViewHolder.setText(R.id.tv_late, "正常");
            ivLate.setVisibility(View.GONE);
            tvLate.setVisibility(View.VISIBLE);
        } else if ("2".equals(sign.getFromsts())) {
            tvLate.setVisibility(View.GONE);
            ivLate.setVisibility(View.VISIBLE);
            ivLate.setImageResource(R.mipmap.ic_late);
        }

        baseViewHolder.setText(R.id.tv_name, sign.getNickname());
        baseViewHolder.setText(R.id.tv_date, sign.getSigndate());
    }
}
