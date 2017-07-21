package com.xxl.kfapp.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxl.kfapp.R;
import com.xxl.kfapp.model.response.KSTMVo;

import java.util.List;


/**
 * 作者：XNN
 * 日期：2017/3/31
 * 作用：进度条适配器
 */
public class KSTMAdapter extends BaseQuickAdapter<KSTMVo> {

    public KSTMAdapter(List<KSTMVo> data) {
        super(R.layout.item_wc, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, KSTMVo vo) {
        baseViewHolder.setOnClickListener(R.id.ly_item, new OnItemChildClickListener());
        baseViewHolder.setText(R.id.tvNum, "第" + vo.getNum() + "题");
        if (vo.isWc()) {
            baseViewHolder.setVisible(R.id.imgWC, true);
        } else {
            baseViewHolder.setVisible(R.id.imgWC, false);
        }
//        ImageUtil.loadImgFitCenter(imageView, vo.getPic());

    }

}
