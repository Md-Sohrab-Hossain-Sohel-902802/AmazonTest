package com.example.amazontest.Admin;

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

import com.example.amazontest.DataModuler.AdminOrders;
import com.example.amazontest.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity {


    private RecyclerView orderList;
    private DatabaseReference orderRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);



        orderRef= FirebaseDatabase.getInstance().getReference().child("Orders");

        orderList=findViewById(R.id.orderListRecyclerView);
        orderList.setHasFixedSize(true);
        orderList.setLayoutManager(new LinearLayoutManager(this));






    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<AdminOrders> options=new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(orderRef,AdminOrders.class)
                .build();


        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder> adapter=new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int i, @NonNull final AdminOrders orders) {

                    holder.usernameTextview.setText(orders.getName());
                    holder.userAddress.setText("Address : "+orders.getAddress());
                    holder.priceTextview.setText("Total Amount: "+orders.getTotalAmount()+"$");
                    holder.userDateTime.setText("Order at : "+orders.getDate()+"     ,    "+orders.getTime());
                    holder.userPhoneTextview.setText("Phone:"+orders.getPhone());



                    holder.showOrdersBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String uid=getRef(i).getKey();


                            Intent intent=new Intent(AdminNewOrdersActivity.this,AdminUserProductsActivity.class);

                            intent.putExtra("uid",uid);
                            startActivity(intent);
                        }
                    });


                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                    CharSequence options[]=new CharSequence[]{
                                            "yes",
                                            "no"
                                    };

                            AlertDialog.Builder builder=new AlertDialog.Builder(AdminNewOrdersActivity.this);
                            builder.setTitle("Have you Shipped this order products?");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                        if(which==0){
                                            String uid=getRef(i).getKey();
                                            removeOrder(uid);

                                        }else if(which==1){
                                                    finish();
                                        }
                                }
                            });

                            builder.show();
                        }
                    });



            }

            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
             View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout,parent,false);

             AdminOrdersViewHolder holder=new AdminOrdersViewHolder(view);
             return holder;
            }
        };

        orderList.setAdapter(adapter);
        adapter.startListening();



    }

    private void removeOrder(String uid) {


        orderRef.child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AdminNewOrdersActivity.this, "This Order is removed successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });






    }


    public static class AdminOrdersViewHolder extends  RecyclerView.ViewHolder{
            public TextView  usernameTextview,priceTextview,userPhoneTextview,userDateTime,userAddress;
            public Button showOrdersBtn;

        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);


            usernameTextview=itemView.findViewById(R.id.order_user_name);
            priceTextview=itemView.findViewById(R.id.order_total_Price);
            userPhoneTextview=itemView.findViewById(R.id.order_phone_number);
            userDateTime=itemView.findViewById(R.id.order_date_time);
            userAddress=itemView.findViewById(R.id.order_address_city);
            showOrdersBtn=itemView.findViewById(R.id.showAllProductsButton);





        }
    }




}
