package com.xxl.kfapp.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxl.kfapp.R;
import com.xxl.kfapp.base.BaseApplication;
import com.xxl.kfapp.model.response.ShopGoodListVo;
import com.xxl.kfapp.utils.Constant;

import java.util.List;

public class GoodsMoreAdapter extends BaseQuickAdapter<ShopGoodListVo.GoodsInfo> {

    public GoodsMoreAdapter(List<ShopGoodListVo.GoodsInfo> data) {
        super(R.layout.item_shop_goods, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ShopGoodListVo.GoodsInfo goodsInfo) {
        baseViewHolder.setOnClickListener(R.id.lyt_item, new OnItemChildClickListener());
        baseViewHolder.setText(R.id.tv_goods_name, goodsInfo.getGoodsname());
        baseViewHolder.setText(R.id.tv_goods_disc, goodsInfo.getMemo());
        baseViewHolder.setText(R.id.tv_goods_price, Constant.addComma(goodsInfo.getPrice() * goodsInfo.getNum() + ""));
        baseViewHolder.setText(R.id.tv_goods_num, goodsInfo.getNum() + "");
        baseViewHolder.setText(R.id.tv_csname, goodsInfo.getCsname());
        baseViewHolder.getView(R.id.line).setVisibility(View.GONE);
        ImageView ivGoods = baseViewHolder.getView(R.id.iv_goods_pic);
        Glide.with(BaseApplication.getContext()).load(goodsInfo.getPic()).into(ivGoods);
    }

}
