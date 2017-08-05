package com.xxl.kfapp.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxl.kfapp.R;
import com.xxl.kfapp.model.response.TextVo;

import java.util.List;

public class TextAdapter extends BaseQuickAdapter<TextVo> {
    public TextAdapter(List<TextVo> data) {
        super(R.layout.item_text, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, TextVo textVo) {
        baseViewHolder.setText(R.id.tv_txt1, textVo.getTxt1());
        baseViewHolder.setText(R.id.tv_txt2, textVo.getTxt2());
    }
}
