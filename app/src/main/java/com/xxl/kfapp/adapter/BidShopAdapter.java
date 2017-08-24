package com.xxl.kfapp.adapter;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxl.kfapp.R;
import com.xxl.kfapp.base.BaseApplication;
import com.xxl.kfapp.model.response.BidShopListVo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BidShopAdapter extends BaseQuickAdapter<BidShopListVo.BidShopVo> {
    private CountDownTimer c;

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
        final TextView tvCountdown = baseViewHolder.getView(R.id.tv_countdown);
        TextView tvBidState = baseViewHolder.getView(R.id.tv_bid_state);
        String sale = bidShopVo.getSalests();
        final long nowTime = System.currentTimeMillis() / 1000;
        long start = Long.valueOf(bidShopVo.getStarttime());
        final long end = Long.valueOf(bidShopVo.getEndtime());
        if (!TextUtils.isEmpty(sale)) {
            if ("3".equals(sale)) {
                tvCountdown.setVisibility(View.GONE);
                tvBidState.setText("竞拍已结束");
                tvBidState.setTextColor(Color.parseColor("#54A530"));
            } else if ("2".equals(sale)) {
                tvBidState.setText("剩余时间");
                tvViewcouont.setText("围观次数：" + bidShopVo.getViewcount() + "次");
                c = new CountDownTimer((end - nowTime) * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvCountdown.setText(calculatTime(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {

                    }
                };
                if ((end - nowTime) / 60 / 60 / 24 > 0) {
                    tvCountdown.setText(((end - nowTime) / 60 / 60 / 24) + "天");
                } else {
                    c.start();
                }
            } else if ("1".equals(sale)) {
                tvBidNum.setVisibility(View.GONE);
                tvBidState.setText("距离开拍时间");
                tvViewcouont.setText("围观次数：" + bidShopVo.getViewcount() + "次");
                c = new CountDownTimer((start - nowTime) * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvCountdown.setText(calculatTime(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {

                    }
                };
                if ((start - nowTime) / 60 / 60 / 24 > 0) {
                    tvCountdown.setText(((start - nowTime) / 60 / 60 / 24) + "天");
                } else {
                    c.start();
                }
            }
        }
    }

    private String calculatTime(long milliSecondTime) {

        int hour = (int) (milliSecondTime / (60 * 60 * 1000));
        int minute = (int) ((milliSecondTime - hour * 60 * 60 * 1000) / (60 * 1000));
        int seconds = (int) ((milliSecondTime - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000);

        if (seconds >= 60) {
            seconds = seconds % 60;
            minute += seconds / 60;
        }
        if (minute >= 60) {
            minute = minute % 60;
            hour += minute / 60;
        }

        String sh = "";
        String sm = "";
        String ss = "";
        if (hour < 10) {
            sh = "0" + String.valueOf(hour);
        } else {
            sh = String.valueOf(hour);
        }
        if (minute < 10) {
            sm = "0" + String.valueOf(minute);
        } else {
            sm = String.valueOf(minute);
        }
        if (seconds < 10) {
            ss = "0" + String.valueOf(seconds);
        } else {
            ss = String.valueOf(seconds);
        }

        return sh + ":" + sm + ":" + ss;
    }
}
