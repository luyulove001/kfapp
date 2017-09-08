package com.xxl.kfapp.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxl.kfapp.R;
import com.xxl.kfapp.model.response.IncomeListVo;

import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */

public class IncomeAdapter extends BaseQuickAdapter<IncomeListVo.IncomeVo> {
    public IncomeAdapter(List<IncomeListVo.IncomeVo> data) {
        super(R.layout.item_income, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, IncomeListVo.IncomeVo vo) {
        baseViewHolder.setText(R.id.tv_date, vo.getOrderdate().split(" ")[0]);
        baseViewHolder.setText(R.id.tv_time, vo.getOrderdate().split(" ")[1]);
        baseViewHolder.setText(R.id.tv_nickname, vo.getNickname());
        baseViewHolder.setText(R.id.tv_pay, "¥" + vo.getAmount());
        if ("1".equals(vo.getPaytype()))
            baseViewHolder.setText(R.id.tv_type, "支付宝");
        else if ("2".equals(vo.getPaytype()))
            baseViewHolder.setText(R.id.tv_type, "微信");
        else if ("3".equals(vo.getPaytype()))
            baseViewHolder.setText(R.id.tv_type, "现金");

    }
}
