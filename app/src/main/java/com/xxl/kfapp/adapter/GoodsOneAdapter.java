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

public class GoodsOneAdapter extends BaseQuickAdapter<ShopGoodListVo.GoodsInfo> {
    private boolean isSelect = false;
    private OnSelectListener onSelectListener;

    public GoodsOneAdapter(List<ShopGoodListVo.GoodsInfo> data) {
        super(R.layout.item_shop_goods, data);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ShopGoodListVo.GoodsInfo goodsInfo) {
        baseViewHolder.setOnClickListener(R.id.lyt_item, new OnItemChildClickListener());
        baseViewHolder.setText(R.id.tv_goods_name, goodsInfo.getGoodsname());
        baseViewHolder.setText(R.id.tv_goods_disc, goodsInfo.getMemo());
        baseViewHolder.setText(R.id.tv_goods_price, Constant.addComma(goodsInfo.getPrice() + ""));
        baseViewHolder.setText(R.id.tv_csname, goodsInfo.getCsname());
        baseViewHolder.getView(R.id.tv_goods_num).setVisibility(View.GONE);
        ImageView ivGoods = baseViewHolder.getView(R.id.iv_goods_pic);
        Glide.with(BaseApplication.getContext()).load(goodsInfo.getPic()).into(ivGoods);
        final ImageView ivSelect = baseViewHolder.getView(R.id.iv_goods_select);
        ivSelect.setVisibility(View.VISIBLE);
        ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelect) {
                    ivSelect.setImageResource(R.mipmap.qq_grey);
                    isSelect = false;
                } else {
                    ivSelect.setImageResource(R.mipmap.qq_red);
                    isSelect = true;
                }
                if (onSelectListener != null) {
                    onSelectListener.onSelect(isSelect);
                }
            }
        });
    }

    public interface OnSelectListener {
        void onSelect(boolean isSelect);
    }
}
