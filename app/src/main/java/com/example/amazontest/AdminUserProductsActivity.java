package com.example.amazontest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amazontest.DataModuler.Cart;
import com.example.amazontest.Prevalent.Prevalent;
import com.example.amazontest.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUserProductsActivity extends AppCompatActivity {

    private RecyclerView productList;
    private DatabaseReference productRef;

    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        userid=getIntent().getStringExtra("uid");

        productRef= FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(userid).child("Products");


        productList=findViewById(R.id.productListRecyclerviewid);
        productList.setHasFixedSize(true);
        productList.setLayoutManager(new LinearLayoutManager(this));





    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options=new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(productRef,Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter=new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int i, @NonNull Cart cart) {
                holder.txtProductQuantity.setText("Quantity = "+cart.getQuantity());
                holder.txtProductPrice.setText("Price = "+cart.getPrice()+"$");
                holder.txtProductname.setText(cart.getPname());


            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);

                CartViewHolder holder=new CartViewHolder(view);
                return  holder;
            }
        };

        productList.setAdapter(adapter);
        adapter.startListening();








    }
}
