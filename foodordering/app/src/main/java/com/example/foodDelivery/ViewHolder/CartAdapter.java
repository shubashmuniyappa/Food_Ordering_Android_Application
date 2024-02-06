package com.example.foodDelivery.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.foodDelivery.Interface.ItemClickListener;
import com.example.foodDelivery.Model.Order;
import com.example.foodDelivery.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
 * @Author - Shubash
 */
class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtCartName, txtCartPrice;
    public ImageView imgCartCount;

    private ItemClickListener itemClickListener;

    public CartViewHolder(View itemView) {
        super(itemView);
        txtCartName = itemView.findViewById(R.id.cart_item_name);
        txtCartPrice = itemView.findViewById(R.id.cart_item_Price);
        imgCartCount = itemView.findViewById(R.id.cart_item_count);

    }

    public void setTxtCartName(TextView txtCartName) {
        this.txtCartName = txtCartName;
    }

    @Override
    public void onClick(View view) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listData = new ArrayList<>();
    private final Context context;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflaterLayout = LayoutInflater.from(context);
        View itemView = inflaterLayout.inflate(R.layout.cart_layout, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int position) {
        TextDrawable textDrawable = TextDrawable.builder().buildRound("" + listData.get(position).getQuantity(), Color.RED);
        cartViewHolder.imgCartCount.setImageDrawable(textDrawable);
        Locale locale = new Locale("en", "US");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(position).getPrice())) * (Integer.parseInt(listData.get(position).getQuantity()));
        cartViewHolder.txtCartPrice.setText(numberFormat.format(price));

        cartViewHolder.txtCartName.setText(listData.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}