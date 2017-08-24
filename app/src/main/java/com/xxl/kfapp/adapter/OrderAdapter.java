package com.xxl.kfapp.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxl.kfapp.R;
import com.xxl.kfapp.model.response.OrderListVo;

import java.util.List;

public class OrderAdapter extends BaseQuickAdapter<OrderListVo.Order> {
    public OrderAdapter(List<OrderListVo.Order> data) {
        super(R.layout.item_order, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, OrderListVo.Order order) {
        baseViewHolder.setText(R.id.tv_rmb, "¥" + order.getAmount());
        baseViewHolder.setText(R.id.tv_time, order.getOrderdate());
        baseViewHolder.setText(R.id.tv_order_name, order.getSubject());
        ImageView ivPayType = baseViewHolder.getView(R.id.iv_pay_type);
        if ("1".equals(order.getPaytype()))
            ivPayType.setImageResource(R.mipmap.qc_fast_weix);
        else if ("2".equals(order.getPaytype()))
            ivPayType.setImageResource(R.mipmap.qc_fast_zhi);

        if ("0".equals(order.getRefundsts()))
            baseViewHolder.getView(R.id.tv_refund).setVisibility(View.GONE);
        else if ("1".equals(order.getRefundsts()))
            baseViewHolder.getView(R.id.tv_refund).setVisibility(View.VISIBLE);
    }
}
