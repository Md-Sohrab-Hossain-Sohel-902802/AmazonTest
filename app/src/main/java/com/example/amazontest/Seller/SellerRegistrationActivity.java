package com.example.amazontest.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amazontest.R;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {

    private Button sellerLogin;

    private EditText nameEdittext,phoneEdittext,emailEdittext,passwordEdittext,addressEdittext;
    private  Button registerButton;

    private FirebaseAuth mAuth;

    private ProgressDialog loadingBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);



        mAuth=FirebaseAuth.getInstance();

        sellerLogin=findViewById(R.id.seller_AlreadyHaveAccount_Button);
        nameEdittext=findViewById(R.id.seller_NameEditText);
        phoneEdittext=findViewById(R.id.seller_PhoneEdittext);
        emailEdittext=findViewById(R.id.seller_EmailEdittext);
        passwordEdittext=findViewById(R.id.seller_PasswordEdittext);
        addressEdittext=findViewById(R.id.sellerAddressEdittext);

        registerButton=findViewById(R.id.seller_Register_Button);
        loadingBar=new ProgressDialog(this);



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sellerRegister();



            }
        });












        sellerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerRegistrationActivity.this,SellerLoginActivity.class);
                startActivity(intent);
            }
        });





    }

    private void sellerRegister() {

        final String email=emailEdittext.getText().toString();
        final String password=passwordEdittext.getText().toString();
        final String address=addressEdittext.getText().toString();
        final String  name=nameEdittext.getText().toString();
        final String phone=phoneEdittext.getText().toString();



        if(name.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() || phone.isEmpty()){
            Toast.makeText(this, "Please Fill Up All The Thing", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("CreateAccount");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();



            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){


                     final   DatabaseReference sellerRef= FirebaseDatabase.getInstance().getReference();
                        HashMap<String,Object> sellermap=new HashMap<>();


                        String uid=mAuth.getCurrentUser().getUid();
                        sellermap.put("email",email);
                        sellermap.put("uid",uid);
                        sellermap.put("password",password);
                        sellermap.put("address",address);
                        sellermap.put("name",name);
                        sellermap.put("phone",phone);
                        sellerRef.child("Seller").child(uid).updateChildren(sellermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            loadingBar.dismiss();
                                            Toast.makeText(SellerRegistrationActivity.this, "Your Account Created Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(SellerRegistrationActivity.this,SellerHomeActivity.class);
                                            startActivity(intent);

                                        }
                            }
                        });

                    }
                }
            });











        }










    }
}