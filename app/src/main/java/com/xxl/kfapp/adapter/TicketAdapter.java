package com.xxl.kfapp.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxl.kfapp.R;
import com.xxl.kfapp.model.response.TicketListVo;

import java.util.List;

/**
 * Created by Administrator on 2017/8/5.
 */

public class TicketAdapter extends BaseQuickAdapter<TicketListVo.TicketVo> {

    public TicketAdapter(List<TicketListVo.TicketVo> data) {
        super(R.layout.item_ticket_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, TicketListVo.TicketVo vo) {
        baseViewHolder.setText(R.id.tv_name, "理发师：" + vo.getRealname());
        baseViewHolder.setText(R.id.tv_shopname, vo.getShopname());
        baseViewHolder.setText(R.id.tv_create, "理发时间：" + vo.getShopname());
        baseViewHolder.setText(R.id.tv_ticketno, "票号：" + vo.getTicketno());
        baseViewHolder.setText(R.id.tv_ticket_price, vo.getPrice());
        baseViewHolder.setText(R.id.tv_use, "理发时间：" + vo.getChecktime());
    }
}
