package com.xxl.kfapp.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxl.kfapp.R;
import com.xxl.kfapp.model.response.MessageListVo;

import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */

public class MessageAdapter extends BaseQuickAdapter<MessageListVo.MessageVo> {
    public MessageAdapter(List<MessageListVo.MessageVo> data) {
        super(R.layout.item_message, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MessageListVo.MessageVo vo) {
        baseViewHolder.setOnClickListener(R.id.lyt_item, new OnItemChildClickListener());
        baseViewHolder.setText(R.id.tv_msg_type, vo.getTitle());
        baseViewHolder.setText(R.id.tv_msg_disp, vo.getSendmsg());
        baseViewHolder.setText(R.id.tv_receive_date, vo.getSendtime());

        ImageView ivMsgSts = baseViewHolder.getView(R.id.iv_msg_state);
        if ("0".equals(vo.getReadsts()))
            ivMsgSts.setImageResource(R.mipmap.msg_unread);
        else if ("1".equals(vo.getReadsts()))
            ivMsgSts.setImageResource(R.mipmap.ic_msg_read);
    }
}
