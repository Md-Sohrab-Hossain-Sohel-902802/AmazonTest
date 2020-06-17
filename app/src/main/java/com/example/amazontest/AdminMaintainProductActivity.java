package com.example.amazontest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductActivity extends AppCompatActivity {


    private Button applyChangesButton;
    private EditText name,price,description;
    private ImageView imageView;

    private  String productid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_product);


        productid=getIntent().getStringExtra("pid");

        applyChangesButton=findViewById(R.id.applychangesbuttonId);
        name=findViewById(R.id.product_name_maintain);
        price=findViewById(R.id.product_price_maintain);
        description=findViewById(R.id.product_description_maintain);
        imageView=findViewById(R.id.product_image_maintain);



        applyChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                updateValue();
            }
        });
















    }


    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Products");
        reference.child(productid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                name.setText(dataSnapshot.child("pname").getValue().toString());
                                price.setText(dataSnapshot.child("price").getValue().toString());
                                description.setText(dataSnapshot.child("description").getValue().toString());


                                Picasso.get().load(dataSnapshot.child("image").getValue().toString()).into(imageView);




                            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });









    }

    private void updateValue() {

            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Products").child(productid);
        HashMap<String,Object> updatevalue=new HashMap<>();
        updatevalue.put("pname",name.getText().toString());
        updatevalue.put("price",price.getText().toString());
        updatevalue.put("description",description.getText().toString());

        databaseReference.updateChildren(updatevalue).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AdminMaintainProductActivity.this, "Product Details Updated Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(AdminMaintainProductActivity.this,AdminCategoryActivity.class);
                    startActivity(intent);

                }
            }
        });






    }
}
