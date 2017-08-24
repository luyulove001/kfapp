package com.xxl.kfapp.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxl.kfapp.R;
import com.xxl.kfapp.model.response.BalanceListVo;

import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */

public class BalanceAdapter extends BaseQuickAdapter<BalanceListVo.BalanceVo> {
    public BalanceAdapter(List<BalanceListVo.BalanceVo> data) {
        super(R.layout.item_withdraw, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BalanceListVo.BalanceVo vo) {
        baseViewHolder.setOnClickListener(R.id.lyt_item, new OnItemChildClickListener());
        baseViewHolder.setText(R.id.tv_shopname, vo.getShopname());
        baseViewHolder.setText(R.id.tv_withdraw, "¥" + vo.getBalance());
    }
}
