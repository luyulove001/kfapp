package com.xxl.kfapp.adapter;

import android.graphics.Color;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxl.kfapp.R;
import com.xxl.kfapp.model.response.BalanceListVo;
import com.xxl.kfapp.model.response.IncomeVo;

import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */

public class IncomeDetailAdapter extends BaseQuickAdapter<IncomeVo.Income> {
    public IncomeDetailAdapter(List<IncomeVo.Income> data) {
        super(R.layout.item_income_balance, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, IncomeVo.Income vo) {
        baseViewHolder.setText(R.id.tv_time, vo.getIncomedate());
        baseViewHolder.setText(R.id.tv_balance, "余额：" + vo.getBalance());
        TextView tvRmb = baseViewHolder.getView(R.id.tv_rmb);
        if ("1".equals(vo.getIncometype())) {
            baseViewHolder.setText(R.id.tv_rmb, "+" + vo.getIncome());
            tvRmb.setTextColor(Color.argb(255, 177, 79, 69));
            baseViewHolder.setText(R.id.tv_type, "购票");
        } else if ("2".equals(vo.getIncometype())) {
            baseViewHolder.setText(R.id.tv_rmb, "-" + vo.getIncome());
            baseViewHolder.setText(R.id.tv_type, "退票");
            tvRmb.setTextColor(Color.argb(255, 59, 192, 195));
        } else if ("3".equals(vo.getIncometype())) {
            baseViewHolder.setText(R.id.tv_rmb, "-" + vo.getIncome());
            baseViewHolder.setText(R.id.tv_type, "提现");
            tvRmb.setTextColor(Color.argb(255, 59, 192, 195));
        }
    }
}
