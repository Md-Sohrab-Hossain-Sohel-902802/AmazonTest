package com.example.amazontest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amazontest.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameEditText,phoneEdittext,addressEdittext,cityEdittext;
    private Button confirmOrderButton;
    private  String totalAmount="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);


        totalAmount=getIntent().getStringExtra("totalPrice");
        Toast.makeText(this, "Total Price ="+totalAmount+"$", Toast.LENGTH_SHORT).show();




        confirmOrderButton=findViewById(R.id.confirm_final_order_button);
        nameEditText=findViewById(R.id.shipment_name);
        phoneEdittext=findViewById(R.id.shipment_phone_number);
        addressEdittext=findViewById(R.id.shipment_address);
        cityEdittext=findViewById(R.id.shipment_city);


        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                          check();
            }
        });







    }

    private void check() {




        if(TextUtils.isEmpty(nameEditText.getText().toString())){
            Toast.makeText(this, "Please Provide your Full name", Toast.LENGTH_SHORT).show();
        }else    if(TextUtils.isEmpty(phoneEdittext.getText().toString())){
            Toast.makeText(this, "Please Provide your Phone Number", Toast.LENGTH_SHORT).show();
        }else    if(TextUtils.isEmpty(addressEdittext.getText().toString())){
            Toast.makeText(this, "Please Provide your Current Address", Toast.LENGTH_SHORT).show();
        }else    if(TextUtils.isEmpty(cityEdittext.getText().toString())){
            Toast.makeText(this, "Please Provide your City Name", Toast.LENGTH_SHORT).show();
        }else{
            confirmOrder();
        }




    }

    private void confirmOrder() {
      final   String saveCurrentTime,saveCurrentDate;

        Calendar calforDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM  dd, yyyy");
        saveCurrentDate=currentDate.format(calforDate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calforDate.getTime());

        final DatabaseReference orderRef= FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUsers.getPhone());

        final HashMap<String,Object> orderMap=new HashMap<>();
        orderMap.put("totalAmount",totalAmount);
        orderMap.put("name",nameEditText.getText().toString());
        orderMap.put("phone",phoneEdittext.getText().toString());
        orderMap.put("address",addressEdittext.getText().toString());
        orderMap.put("city",cityEdittext.getText().toString());
        orderMap.put("date",saveCurrentDate);
        orderMap.put("time",saveCurrentTime);
        orderMap.put("state","not shipped");

        orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                            FirebaseDatabase.getInstance().getReference().child("Cart List")
                                    .child("User View")
                                    .child(Prevalent.currentOnlineUsers.getPhone())
                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "your final order has been placed successfull", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(ConfirmFinalOrderActivity.this,HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                    }
            }
        });




    }
}
