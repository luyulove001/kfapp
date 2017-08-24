package com.xxl.kfapp.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxl.kfapp.R;
import com.xxl.kfapp.model.response.ShopRenameRecordVo;

import java.util.List;

public class ShopRenameAdapter extends BaseQuickAdapter<ShopRenameRecordVo.RecordVo> {
    public ShopRenameAdapter(List<ShopRenameRecordVo.RecordVo> data) {
        super(R.layout.item_rename_record, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ShopRenameRecordVo.RecordVo vo) {
        baseViewHolder.setText(R.id.tv_shopname, vo.getNewname());
        baseViewHolder.setText(R.id.tv_nickname, vo.getCzuser());
        baseViewHolder.setText(R.id.tv_time, vo.getCzdate());
    }
}
