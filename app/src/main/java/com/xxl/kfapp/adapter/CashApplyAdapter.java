package com.xxl.kfapp.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxl.kfapp.R;
import com.xxl.kfapp.model.response.CashRecordVo;

import java.util.List;

/**
 * Created by Administrator on 2017/8/17.
 */

public class CashApplyAdapter extends BaseQuickAdapter<CashRecordVo.CashVo> {
    public CashApplyAdapter(List<CashRecordVo.CashVo> data) {
        super(R.layout.item_cash, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CashRecordVo.CashVo vo) {
        baseViewHolder.setText(R.id.tv_date, vo.getApplytime());
        baseViewHolder.setText(R.id.tv_amount, "¥" + vo.getAmount());
        if ("1".equals(vo.getApplysts())) {
            baseViewHolder.getView(R.id.tv_applysts).setVisibility(View.VISIBLE);
        } else if ("2".equals(vo.getApplysts())) {
            baseViewHolder.getView(R.id.tv_applysts).setVisibility(View.INVISIBLE);
        }
    }
}
