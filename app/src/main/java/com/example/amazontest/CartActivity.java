package com.example.amazontest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.amazontest.DataModuler.Cart;

import com.example.amazontest.Prevalent.Prevalent;
import com.example.amazontest.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private Button nextProcessbutton;
    private TextView txtToalAmount,msg1Textview;

    private  int overTotalPrice=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);




        recyclerView=findViewById(R.id.cartListRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    nextProcessbutton=findViewById(R.id.next_process_button);
    txtToalAmount=findViewById(R.id.total_price);
    msg1Textview=findViewById(R.id.msg1);


    nextProcessbutton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            txtToalAmount.setText("Total Price="+String.valueOf(overTotalPrice)+"$");
                Intent intent=new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("totalPrice",String.valueOf(overTotalPrice));
                startActivity(intent);
        }
    });





    }


    @Override
    protected void onStart() {
        super.onStart();

        checkOrderState();


        final DatabaseReference cartListref= FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options=new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListref.child("User View").child(Prevalent.currentOnlineUsers.getPhone()).child("Products"),Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter=new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, final int i, @NonNull final Cart cart) {

                holder.txtProductQuantity.setText("Quantity = "+cart.getQuantity());
                holder.txtProductPrice.setText("Price = "+cart.getPrice()+"$");
                holder.txtProductname.setText(cart.getPname());


                int  oneTypeProductTPrice=((Integer.valueOf(cart.getPrice())))*Integer.valueOf(cart.getQuantity());
                overTotalPrice=overTotalPrice+oneTypeProductTPrice;
                txtToalAmount.setText("Total Price="+String.valueOf(overTotalPrice)+"$");






                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] =new CharSequence[]{
                                    "Edit",
                                "Remove"
                        };
                        AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");


                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                if(which==0){
                                    Intent intent=new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid",cart.getPid());
                                    startActivity(intent);


                                }if(which==1){
                                            cartListref.child("User View").child(Prevalent.currentOnlineUsers.getPhone()).child("Products")
                                                    .child(cart.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                 if(task.isSuccessful()){


                                                     cartListref.child("Admin View").child(Prevalent.currentOnlineUsers.getPhone()).child("Products")
                                                             .child(cart.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                         @Override
                                                         public void onComplete(@NonNull Task<Void> task) {
                                                             if(task.isSuccessful()){

                                                                 Toast.makeText(CartActivity.this, cart.getPname()+"   Removed", Toast.LENGTH_SHORT).show();
                                                                 Intent intent=new Intent(CartActivity.this, HomeActivity.class);
                                                                 startActivity(intent);
                                                             }
                                                         }
                                                     });



                                                 }
                                                }
                                            });
                                }


                            }
                        });

                    builder.show();

                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);

                 CartViewHolder holder=new CartViewHolder(view);
                 return  holder;

            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();



    }


    private  void checkOrderState(){
        DatabaseReference orderRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUsers.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String shippingSteap=dataSnapshot.child("state").getValue().toString();
                    String userName=dataSnapshot.child("name").getValue().toString();
                    if(shippingSteap.equals("shipped")){
                               txtToalAmount.setText("Dear "+userName+"\n order is shipped successfully.");
                               recyclerView.setVisibility(View.GONE);
                               msg1Textview.setVisibility(View.VISIBLE);

                               msg1Textview.setText("Congratulation,your final order has been shipped successfully,soon you will received your order at your door steap ");
                               nextProcessbutton.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "you can purchase more products,once you received  your first final order", Toast.LENGTH_LONG).show();


                    }else if(shippingSteap.equals("not shipped")){
                        txtToalAmount.setText("Shipping state = not shipped");
                        recyclerView.setVisibility(View.GONE);
                        msg1Textview.setVisibility(View.VISIBLE);

                        nextProcessbutton.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "you can purchase more products,once you received  your first final order", Toast.LENGTH_LONG).show();


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
