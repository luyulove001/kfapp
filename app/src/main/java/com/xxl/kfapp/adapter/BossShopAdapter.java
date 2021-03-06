package com.xxl.kfapp.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxl.kfapp.R;
import com.xxl.kfapp.base.BaseApplication;
import com.xxl.kfapp.model.response.BossShopListVo;

import java.util.List;

public class BossShopAdapter extends BaseQuickAdapter<BossShopListVo.BossShopInfo> {
    public BossShopAdapter(List<BossShopListVo.BossShopInfo> data) {
        super(R.layout.item_shop, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BossShopListVo.BossShopInfo bossShopInfo) {
        baseViewHolder.setOnClickListener(R.id.lyt_item, new OnItemChildClickListener());
        baseViewHolder.setText(R.id.tv_shopname, bossShopInfo.getShopname());
        baseViewHolder.setText(R.id.tv_address, bossShopInfo.getAddress());
        baseViewHolder.setText(R.id.tv_start_business, "开业时间：" + bossShopInfo.getStarttime());
        baseViewHolder.setText(R.id.tv_haircut_num, "理发师：" + bossShopInfo.getBbcnt());
        baseViewHolder.setText(R.id.tv_shopkeeper, "店主：" + bossShopInfo.getNickname());
        ImageView ivShop = baseViewHolder.getView(R.id.iv_shop);
        if (!TextUtils.isEmpty(bossShopInfo.getShoppic()) && !"null".equals(bossShopInfo.getShoppic()))
            Glide.with(BaseApplication.getContext()).load(bossShopInfo.getShoppic()).into(ivShop);
        else ivShop.setImageResource(R.mipmap.icon_no_pic);
        TextView tvOnline = baseViewHolder.getView(R.id.tv_online);
        GradientDrawable p = (GradientDrawable) tvOnline.getBackground();
        if (bossShopInfo.getOnlinests() == 0) {
            tvOnline.setText("离线");
            tvOnline.setTextColor(Color.parseColor("#bf4740"));
            p.setStroke(1, Color.parseColor("#bf4740"));
        } else {
            tvOnline.setText("在线");
            tvOnline.setTextColor(Color.argb(255, 50, 205, 50));
            p.setStroke(1, Color.argb(255, 50, 205, 50));
        }
    }
}
