package com.example.foodDelivery;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.foodDelivery.Common.Common;
import com.example.foodDelivery.Interface.ItemClickListener;
import com.example.foodDelivery.Model.Requests;
import com.example.foodDelivery.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*
 * @Author - Shubash
 */
public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Requests, OrderViewHolder> firebaseAdapter;

    FirebaseDatabase databaseRef;
    DatabaseReference requestsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //FireBase init
        databaseRef = FirebaseDatabase.getInstance();
        requestsRef = databaseRef.getReference("Requests");

        recyclerView = findViewById(R.id.listOrder);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrder(Common.currentUser.getPhone());
    }

    private void loadOrder(String phone) {
        FirebaseRecyclerOptions<Requests> options;
        if (!Common.currentUser.getIsStaff().equals("false")) {
            options =
                    new FirebaseRecyclerOptions.Builder<Requests>()
                            .setQuery(requestsRef, Requests.class)
                            .build();
        } else {
            options =
                    new FirebaseRecyclerOptions.Builder<Requests>()
                            .setQuery(requestsRef.orderByChild("phone").equalTo(phone), Requests.class)
                            .build();
        }
        firebaseAdapter = new FirebaseRecyclerAdapter<Requests, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull final Requests model) {
                holder.txtOrderIdTv.setText(firebaseAdapter.getRef(position).getKey());
                holder.txtOrderStatusTv.setText(convertStatus(model.getStatus()));
                holder.txtOrderAddressTv.setText(model.getAddress());
                holder.txtOrderPhoneTv.setText(model.getPhone());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        if (!Common.currentUser.getIsStaff().equals("false")) {
                            AlertDialog.Builder alertDialogue = new AlertDialog.Builder(OrderStatus.this);
                            alertDialogue.setTitle("Order Delivered ? Add Comments");
                            alertDialogue.setMessage("Yes or No");

                            final EditText editAddress = new EditText(OrderStatus.this);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT
                            );
                            editAddress.setLayoutParams(lp);
                            alertDialogue.setView(editAddress);
                            alertDialogue.setIcon(R.drawable.ic_shopping_cart_black_24dp);

                            alertDialogue.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (editAddress.getText().toString().equals("Yes") || editAddress.getText().toString().equals("yes")) {
                                        model.setStatus("2");
                                        requestsRef.child(model.getPk()).setValue(model);
                                    } else {
                                        model.setStatus("1");
                                        requestsRef.child(model.getPk()).setValue(model);
                                    }
                                    dialogInterface.dismiss();

                                }
                            });

                            alertDialogue.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            alertDialogue.show();
                        }
                    }
                });
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_layout, parent, false);

                return new OrderViewHolder(view);
            }
        };
        recyclerView.setAdapter(firebaseAdapter);
    }

    /*
    * util used to change the status of the order
     */
    private String convertStatus(String status) {
        if (status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "On its way";
        else
            return "Delivered";
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseAdapter.stopListening();


    }
}
