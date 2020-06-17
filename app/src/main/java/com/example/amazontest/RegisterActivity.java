package com.example.amazontest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {



    private Button createAccountButton;
    private EditText inputName,inputPhoneNumber,inputPassword;

    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);






        inputName=findViewById(R.id.register_username_input);
        inputPhoneNumber=findViewById(R.id.register_phone_number_input);
        inputPassword=findViewById(R.id.register_password_input);

        createAccountButton=findViewById(R.id.register_login_button);




        loadingBar=new ProgressDialog(this);



        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });







    }

    private void CreateAccount() {


        String name=inputName.getText().toString();
        String phone=inputPhoneNumber.getText().toString();
        String password=inputPassword.getText().toString();


        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Write Your Name", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(phone)){

            Toast.makeText(this, "Please Write Your Phone Number", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){

            Toast.makeText(this, "Please Write Your Password", Toast.LENGTH_SHORT).show();
        }else{

            loadingBar.setTitle("CreateAccount");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            validatePhoneNumber(name,phone,password);


        }





    }

    private void validatePhoneNumber(final String name, final String phone, final String password) {

        final DatabaseReference rootRef;
        rootRef= FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("Users").child(phone).exists()){
                    HashMap<String,Object> userlistMap=new HashMap<>();
                    userlistMap.put("phone",phone);
                    userlistMap.put("password",password);
                    userlistMap.put("name",name);




                    rootRef.child("Users").child(phone).updateChildren(userlistMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(RegisterActivity.this, "Congratulations , your account is created", Toast.LENGTH_SHORT).show();

                                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(intent);

                            }else{
                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this, "Network Error : Please Try again after some time.....", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }else{
                    Toast.makeText(RegisterActivity.this, "This "+phone+"  already exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again using another phone number", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}

