package com.example.foodDelivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodDelivery.Interface.ItemClickListener;
import com.example.foodDelivery.Model.Food;
import com.example.foodDelivery.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/*
 * @Author - Shubash
 */
public class FoodList extends AppCompatActivity {

    FirebaseDatabase databaseRef;
    DatabaseReference foodlistRef;

    FirebaseRecyclerAdapter<Food, FoodViewHolder> firebaseAdapter;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar SearchBar;

    RecyclerView recyclerFood;
    RecyclerView.LayoutManager layoutManager;

    String categoryId = "";

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Init Firebase
        databaseRef = FirebaseDatabase.getInstance();
        foodlistRef = databaseRef.getReference("Food");
        foodlistRef.keepSynced(true);

        //Load Menu
        recyclerFood = findViewById(R.id.recycler_food);
        recyclerFood.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerFood.setLayoutManager(layoutManager);

        //Get intent Here
        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("CategoryId");
        }
        if (!categoryId.isEmpty() && categoryId != null) {
            loadListData(categoryId);
        }
        //Search
        SearchBar = findViewById(R.id.search_bar);
        SearchBar.setHint("Search For Food");
        loadSuggestData();
        SearchBar.setLastSuggestions(suggestList);
        SearchBar.setCardViewElevation(10);
        SearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest = new ArrayList<String>();
                for (String search : suggestList) {
                    if (search.toLowerCase().contains(SearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                SearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        SearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //When Search Bar is Close
                //Restore original suggest adapter
                if (!enabled) recyclerFood.setAdapter(firebaseAdapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearchData(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearchData(CharSequence text) {
        FirebaseRecyclerOptions<Food> op = new FirebaseRecyclerOptions.Builder<Food>().setQuery(foodlistRef.orderByChild("name").equalTo(text.toString()), Food.class).build();
        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(op) {
            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
                return new FoodViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                holder.foodName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(holder.foodImage);
                final Food local = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent fd = new Intent(FoodList.this, FoodDetail.class);
                        fd.putExtra("FoodId", searchAdapter.getRef(position).getKey());
                        startActivity(fd);
                    }
                });
            }
        };
        recyclerFood.setAdapter(searchAdapter);
        searchAdapter.startListening();
    }

    private void loadListData(String categoryid) {
        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>().setQuery(foodlistRef.orderByChild("MenuId").equalTo(categoryid), Food.class).build();
        firebaseAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);

                return new FoodViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder viewHolder, int position, @NonNull Food model) {
                viewHolder.foodName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.foodImage);
                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodDetail = new Intent(FoodList.this, FoodDetail.class);
                        foodDetail.putExtra("FoodId", firebaseAdapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }
        };
        recyclerFood.setAdapter(firebaseAdapter);
    }

    private void loadSuggestData() {
        foodlistRef.orderByChild("menuId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Food item = postSnapshot.getValue(Food.class);
                    suggestList.add(item.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}