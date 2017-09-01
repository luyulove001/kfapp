package com.xxl.kfapp.adapter;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxl.kfapp.R;
import com.xxl.kfapp.model.response.KSTMVo;

import java.util.List;


public class KSTMAnswerAdapter extends BaseQuickAdapter<KSTMVo> {

    public KSTMAnswerAdapter(List<KSTMVo> data) {
        super(R.layout.item_wc, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, KSTMVo vo) {
        baseViewHolder.setOnClickListener(R.id.ly_item, new OnItemChildClickListener());
        baseViewHolder.setText(R.id.tvNum, "第" + vo.getNum() + "题");
        ImageView ivAnswer = baseViewHolder.getView(R.id.imgWC);
        if (vo.isAnswer()) {
            ivAnswer.setImageResource(R.mipmap.answer_true);
        } else {
            ivAnswer.setImageResource(R.mipmap.answer_false);
        }
//        ImageUtil.loadImgFitCenter(imageView, vo.getPic());

    }

}
