package com.xxl.kfapp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxl.kfapp.R;
import com.xxl.kfapp.model.response.CheckInVo;

import java.util.List;


public class CheckInAdapter extends BaseQuickAdapter<CheckInVo.CheckVo> {

    private Context mContext;
    public CheckInAdapter(List<CheckInVo.CheckVo> data, Context context) {
        super(R.layout.item_barber_check_in, data);
        mContext = context;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void convert(BaseViewHolder baseViewHolder, CheckInVo.CheckVo sign) {
        ImageView ivTui = baseViewHolder.getView(R.id.iv_tui);
        ImageView ivLate = baseViewHolder.getView(R.id.iv_late);
        TextView tvTui = baseViewHolder.getView(R.id.tv_tui);
        TextView tvLate = baseViewHolder.getView(R.id.tv_late);
        if ("1".equals(sign.getEndsts())) {
            tvTui.setText("正常");
            tvTui.setTextColor(mContext.getResources().getColor(R.color.text_black2));
            ivTui.setVisibility(View.GONE);
            tvTui.setVisibility(View.VISIBLE);
        } else if ("3".equals(sign.getEndsts())) {
            tvTui.setVisibility(View.GONE);
            ivTui.setVisibility(View.VISIBLE);
            ivTui.setImageResource(R.mipmap.ic_tui);
        } else {
            tvTui.setText("未签退");
            tvTui.setTextColor(mContext.getResources().getColor(R.color.text_orange));
            ivTui.setVisibility(View.GONE);
            tvTui.setVisibility(View.VISIBLE);
        }

        if ("1".equals(sign.getFromsts())) {
            baseViewHolder.setText(R.id.tv_late, "正常");
            tvLate.setTextColor(mContext.getResources().getColor(R.color.text_black2));
            ivLate.setVisibility(View.GONE);
            tvLate.setVisibility(View.VISIBLE);
        } else if ("2".equals(sign.getFromsts())) {
            tvLate.setVisibility(View.GONE);
            ivLate.setVisibility(View.VISIBLE);
            ivLate.setImageResource(R.mipmap.ic_late);
        } else {
            tvLate.setText("未签退");
            tvLate.setTextColor(mContext.getResources().getColor(R.color.text_orange));
            tvLate.setVisibility(View.GONE);
            ivLate.setVisibility(View.VISIBLE);
        }

        baseViewHolder.setText(R.id.tv_name, sign.getNickname());
        baseViewHolder.setText(R.id.tv_date, sign.getSigndate());
    }
}
