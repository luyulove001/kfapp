package com.xxl.kfapp.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxl.kfapp.R;
import com.xxl.kfapp.base.BaseApplication;
import com.xxl.kfapp.model.response.BidShopListVo;

import java.util.List;

public class BidShopAdapter extends BaseQuickAdapter<BidShopListVo.BidShopVo> {

    public BidShopAdapter(List<BidShopListVo.BidShopVo> data) {
        super(R.layout.item_bid_shop, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BidShopListVo.BidShopVo bidShopVo) {
        baseViewHolder.setOnClickListener(R.id.lyt_item, new OnItemChildClickListener());
        baseViewHolder.setText(R.id.tv_shopname, bidShopVo.getShopname());
        baseViewHolder.setText(R.id.tv_address, bidShopVo.getAddress());
        baseViewHolder.setText(R.id.tv_bid_num, "出价次数：" + bidShopVo.getPcnt() + "次");
        ImageView ivShop = baseViewHolder.getView(R.id.iv_shop);
        Glide.with(BaseApplication.getContext()).load(bidShopVo.getListpic()).into(ivShop);
        TextView tvViewcouont = baseViewHolder.getView(R.id.tv_viewcouont);
        TextView tvBidNum = baseViewHolder.getView(R.id.tv_bid_num);
        TextView tvCountdown = baseViewHolder.getView(R.id.tv_countdown);
        TextView tvBidState = baseViewHolder.getView(R.id.tv_bid_state);
        long nowTime = System.currentTimeMillis() / 1000;
        long start = Long.valueOf(bidShopVo.getStarttime());
        long end = Long.valueOf(bidShopVo.getEndtime());
        if (nowTime < end) {
            if (nowTime > start) {
                tvBidState.setText("剩余时间");
                tvCountdown.setText(operateTime(nowTime, end));
                tvViewcouont.setText("围观次数：" + bidShopVo.getViewcount() + "次");
            } else {
                tvBidNum.setVisibility(View.GONE);
                tvCountdown.setText(operateTime(nowTime, start));
                tvBidState.setText("距离开拍时间");
                tvViewcouont.setText("围观次数：" + bidShopVo.getViewcount() + "次");
            }
        } else {
            tvCountdown.setVisibility(View.GONE);
            tvBidState.setText("竞拍已结束");
            tvBidState.setTextColor(Color.parseColor("#54A530"));
        }
    }
//todo 倒计时
    private String operateTime(long start, long end) {
        String str;
        int interval = (int) (end - start);
        if (interval / 60 / 60 / 24 > 0) {
            str = (interval / 60 / 60 / 24) + "天";
        } else {
            int hour = interval / 60 / 60;
            int minute = interval % (60 * 60) / 60;
            int score = interval % (60 * 60) % 60;
            str = hour + ":" + minute + ":" + score;
        }
        return str;
    }
}
