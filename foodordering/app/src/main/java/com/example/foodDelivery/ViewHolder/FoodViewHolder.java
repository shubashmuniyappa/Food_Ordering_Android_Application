package com.example.foodDelivery.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodDelivery.Interface.ItemClickListener;
import com.example.foodDelivery.R;

/*
 * @Author - Shubash
 */
public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView foodName;
    public ImageView foodImage;
    private ItemClickListener itemClickListener;

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodViewHolder(View itemView) {
        super(itemView);
        foodName = itemView.findViewById(R.id.food_name);
        foodImage = itemView.findViewById(R.id.food_image);
        itemView.setOnClickListener(this);
    }
}
