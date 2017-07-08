/*
 * Copyright 2016 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xxl.kfapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.person.ModifyAddrActivity;
import com.xxl.kfapp.model.response.AddrVo;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

public class MenuAdapter extends SwipeMenuAdapter<MenuAdapter.DefaultViewHolder> {

    private List<AddrVo> titles;
    private String addrId;

    private ModifyAddrActivity.OnItemClickListener mOnItemClickListener;

    public MenuAdapter(List<AddrVo> titles, String addrId) {
        this.titles = titles;
        this.addrId = addrId;
    }

    public void setOnItemClickListener(ModifyAddrActivity.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return titles == null ? 0 : titles.size();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_common, parent, false);
    }

    @Override
    public MenuAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        DefaultViewHolder viewHolder = new DefaultViewHolder(realContentView);
        viewHolder.mOnItemClickListener = mOnItemClickListener;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MenuAdapter.DefaultViewHolder holder, int position) {
        holder.setData(titles.get(position).getDispaddress());
        if (!TextUtils.isEmpty(addrId) && addrId.equals(titles.get(position).getAddrid())) {
            holder.setImage();
        }
    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        ImageView ivSelect;
        ModifyAddrActivity.OnItemClickListener mOnItemClickListener;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_address);
            ivSelect = (ImageView) itemView.findViewById(R.id.ic_select);
        }

        public void setData(String title) {
            this.tvTitle.setText(title);
        }

        public void setImage() {
            this.ivSelect.setImageResource(R.mipmap.qq_red);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
                ivSelect.setImageResource(R.mipmap.qq_red);
            }
        }
    }

}
