package com.example.foodDelivery.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.foodDelivery.Interface.ItemClickListener;
import com.example.foodDelivery.R;

/*
 * @Author - Shubash
 */
public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderIdTv, txtOrderStatusTv, txtOrderPhoneTv, txtOrderAddressTv;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public OrderViewHolder(View itemView) {
        super(itemView);
        txtOrderAddressTv = itemView.findViewById(R.id.order_address);
        txtOrderPhoneTv = itemView.findViewById(R.id.order_phone);
        txtOrderStatusTv = itemView.findViewById(R.id.order_status);
        txtOrderIdTv = itemView.findViewById(R.id.order_id);
        itemView.setOnClickListener(this);
    }
}