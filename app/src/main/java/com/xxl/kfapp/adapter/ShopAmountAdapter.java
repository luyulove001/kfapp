package com.xxl.kfapp.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxl.kfapp.R;
import com.xxl.kfapp.model.response.AmountVo;

import java.util.List;

public class ShopAmountAdapter extends BaseQuickAdapter<AmountVo.Count> {
    public ShopAmountAdapter(List<AmountVo.Count> data) {
        super(R.layout.item_shop_amount, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, AmountVo.Count count) {
        baseViewHolder.setOnClickListener(R.id.lyt_item, new OnItemChildClickListener());
        baseViewHolder.setText(R.id.tv_shop_name, count.getShopname());
        baseViewHolder.setText(R.id.tv_ali, (int)(Float.valueOf(count.getZfbtotal()) * 100) + "%");
        baseViewHolder.setText(R.id.tv_wx, (int)(Float.valueOf(count.getWxtotal()) * 100) + "%");
        baseViewHolder.setText(R.id.tv_amount, "¥" + count.getTotal());
    }
}
