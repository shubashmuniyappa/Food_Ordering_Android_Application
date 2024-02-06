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
public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtMenuNameComp;
    public ImageView imageViewComp;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    public MenuViewHolder(View itemView) {
        super(itemView);
        txtMenuNameComp = (TextView)itemView.findViewById(R.id.menu_name);
        imageViewComp = (ImageView)itemView.findViewById(R.id.menu_image);
        itemView.setOnClickListener(this);
    }
}