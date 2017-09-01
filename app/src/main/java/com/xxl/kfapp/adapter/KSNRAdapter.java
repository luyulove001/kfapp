package com.xxl.kfapp.adapter;


import android.graphics.Color;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxl.kfapp.R;
import com.xxl.kfapp.model.response.KSNRVo;
import com.xxl.kfapp.model.response.KSTMVo;

import java.util.List;


/**
 * 作者：XNN
 * 日期：2017/3/31
 * 作用：题目内容
 */
public class KSNRAdapter extends BaseQuickAdapter<KSNRVo> {

    public KSNRAdapter(List<KSNRVo> data) {
        super(R.layout.item_ksnr, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, KSNRVo vo) {
        baseViewHolder.setOnClickListener(R.id.ly_item, new OnItemChildClickListener());
//        baseViewHolder.setText(R.id.tv_info, vo.getInfo());
        TextView tvInfo = baseViewHolder.getView(R.id.tv_info);
        tvInfo.setText(vo.getInfo());
        if (vo.isXz()) {
            baseViewHolder.setImageResource(R.id.img_xz, R.mipmap.qq_red);
        } else {
            baseViewHolder.setImageResource(R.id.img_xz, R.mipmap.qq_grey);
        }
        if (vo.isAnswer()) {
            tvInfo.setTextColor(Color.parseColor("#54A530"));
        } else {
            tvInfo.setTextColor(Color.parseColor("#666666"));
        }
//        ImageUtil.loadImgFitCenter(imageView, vo.getPic());

    }

}
