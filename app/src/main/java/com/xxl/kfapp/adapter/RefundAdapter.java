package com.xxl.kfapp.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxl.kfapp.R;
import com.xxl.kfapp.model.response.RefundListVo;
import com.xxl.kfapp.model.response.TicketListVo;

import java.util.List;

/**
 * Created by Administrator on 2017/8/5.
 */

public class RefundAdapter extends BaseQuickAdapter<RefundListVo.TicketVo> {

    public RefundAdapter(List<RefundListVo.TicketVo> data) {
        super(R.layout.item_refund_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, RefundListVo.TicketVo vo) {
        baseViewHolder.setOnClickListener(R.id.lyt_item, new OnItemChildClickListener());
        baseViewHolder.setText(R.id.tv_name, "顾客：" + vo.getNickname());
        baseViewHolder.setText(R.id.tv_shopname, vo.getShopname());
        baseViewHolder.setText(R.id.tv_create, "购买时间：" + vo.getBuytime());
        baseViewHolder.setText(R.id.tv_ticketno, "票号：" + vo.getTicketno());
        baseViewHolder.setText(R.id.tv_price, "票价：" +vo.getPrice());
        baseViewHolder.setText(R.id.tv_apply_time, "申请时间：" + vo.getApplytime());
        TextView tvApplySts = baseViewHolder.getView(R.id.tv_applysts);
        if ("1".equals(vo.getApplysts())) {
            tvApplySts.setText("等待退票");
            tvApplySts.setTextColor(Color.argb(255, 255, 153, 0));
        } else if ("2".equals(vo.getApplysts())) {
            tvApplySts.setText("同意退票");
            tvApplySts.setTextColor(Color.argb(255, 50, 205, 50));
        } else if ("3".equals(vo.getApplysts())) {
            tvApplySts.setText("拒绝退票");
            tvApplySts.setTextColor(Color.argb(255, 177, 79, 69));
        }
    }
}
