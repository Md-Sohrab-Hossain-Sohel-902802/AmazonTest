package com.example.amazontest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.amazontest.DataModuler.Products;
import com.example.amazontest.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

 //   private FloatingActionButton addToCartBtn;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productprice,productDescription,productName;
private Button addtoCartButton;

    String productID="";
    String state="Normal";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);


        productID=getIntent().getStringExtra("pid");


       // addToCartBtn=findViewById(R.id.add_product_to_cart_btn);
        numberButton=findViewById(R.id.number_btn);
        productImage=findViewById(R.id.product_image_details);
        productprice=findViewById(R.id.product_price_details);
        productDescription=findViewById(R.id.product_description_details);
        productName=findViewById(R.id.product_name_details);
        addtoCartButton=findViewById(R.id.pd_add_to_cart_button);


        getproductDetails(productID);




        addtoCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                                if(state.equals("Order Shipped") || state.equals("Order Placed")){
                                    Toast.makeText(ProductDetailsActivity.this, "you can  add purchase more products,once your order is shipped or confirmed.", Toast.LENGTH_LONG).show();
                                }else{
                                    addingToCartList();
                                }

            }
        });















    }

    private void addingToCartList() {
        String saveCurrentTime,saveCurrentDate;

        Calendar calforDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM  dd, yyyy");
        saveCurrentDate=currentDate.format(calforDate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calforDate.getTime());

      final   DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Cart List");


        final HashMap<String,Object> cartMap=new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",productprice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");


        databaseReference.child("User View").child(Prevalent.currentOnlineUsers.getPhone()).child("Products")
                .child(productID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    databaseReference.child("Admin View").child(Prevalent.currentOnlineUsers.getPhone()).child("Products")
                            .child(productID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ProductDetailsActivity.this, "Added To Cart", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(ProductDetailsActivity.this,HomeActivity.class);
                                    startActivity(intent);



                                }else {
                                    Toast.makeText(ProductDetailsActivity.this, "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        }
                    });
                }else{
                    Toast.makeText(ProductDetailsActivity.this, "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });








    }

    private void getproductDetails(String productID) {


        DatabaseReference  productRef= FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    Products products=dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productprice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).placeholder(R.drawable.background).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrderState();
    }

    private  void checkOrderState(){
        DatabaseReference orderRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUsers.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String shippingSteap=dataSnapshot.child("state").getValue().toString();

                    if(shippingSteap.equals("shipped")){
                        state="Order Shipped";

                    }else if(shippingSteap.equals("not shipped")){
                            state="Order Placed";
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }












}
