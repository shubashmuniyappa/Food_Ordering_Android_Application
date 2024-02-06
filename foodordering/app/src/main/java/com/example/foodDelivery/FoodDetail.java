package com.example.foodDelivery;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.foodDelivery.Database.Database;
import com.example.foodDelivery.Model.Food;
import com.example.foodDelivery.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/*
 * @Author - Shubash
 */
public class FoodDetail extends AppCompatActivity {

    TextView food_name, food_price;
    ImageView food_image;

    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String foodId = "";

    FirebaseDatabase database;
    DatabaseReference foods;
    Food currentFood;

    private void getDetailFood(String foodId) {

        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);

                Picasso.with(getBaseContext()).load(currentFood.getImage()).into(food_image);
                collapsingToolbarLayout.setTitle(currentFood.getName());

                food_price.setText(currentFood.getPrice());
                food_name.setText(currentFood.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        //FireBase
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Food");

        //Init View
        numberButton = findViewById(R.id.number_button);
        btnCart = findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new Order(foodId, currentFood.getName(), numberButton.getNumber(), currentFood.getPrice(), currentFood.getDiscount()));

                Toast.makeText(FoodDetail.this, "Added To Cart", Toast.LENGTH_SHORT).show();
            }
        });

        food_name = findViewById(R.id.food_name1);
        food_image = findViewById(R.id.img_food);
        food_price = findViewById(R.id.food_price);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

        if (getIntent() != null) foodId = getIntent().getStringExtra("FoodId");
        if (!foodId.isEmpty()) {
            getDetailFood(foodId);
        }
    }

}
