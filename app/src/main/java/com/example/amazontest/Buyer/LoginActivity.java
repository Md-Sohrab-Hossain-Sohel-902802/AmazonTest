package com.example.amazontest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amazontest.Admin.AdminCategoryActivity;
import com.example.amazontest.DataModuler.Users;
import com.example.amazontest.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {



    private EditText inputNumber,inputPassword;
    private Button loginButton;
    private ProgressDialog loadingBar;

    private CheckBox checkBoxRememberme;

    private  String  parentDbname="Users";

    private TextView adminLink,notAdminLink;

    private  TextView forgotPasswordLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        inputNumber=findViewById(R.id.login_phone_number_input);
        inputPassword=findViewById(R.id.login_password_input);
        loginButton=findViewById(R.id.login_login_button);
        adminLink=findViewById(R.id.admin_panel_link);
        notAdminLink=findViewById(R.id.not_admin_panel_link);
        forgotPasswordLink=findViewById(R.id.forget_password_link);



        loadingBar=new ProgressDialog(this);
        Paper.init(this);

        checkBoxRememberme=findViewById(R.id.remember_me_checkbox);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });


        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login Admin");
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);



                parentDbname="Admins";






            }
        });



        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login");
                adminLink.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);



                parentDbname="Users";


            }
        });


    forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent=new Intent(LoginActivity.this,ResetPasswordActivity.class);
            intent.putExtra("check","home");
            startActivity(intent);

        }
    });
























    }




    private void loginUser() {

        String  phone=inputNumber.getText().toString();
        String password=inputPassword.getText().toString();
        if(TextUtils.isEmpty(phone)){

            Toast.makeText(this, "Please Write Your Phone Number", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){

            Toast.makeText(this, "Please Write Your Password", Toast.LENGTH_SHORT).show();
        }else {

            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccesstoAccount(phone,password);




        }






    }

    private void AllowAccesstoAccount(final String phone, final String password) {

        if(checkBoxRememberme.isChecked()){
            Paper.book().write(Prevalent.userPhonekey,phone);
            Paper.book().write(Prevalent.userPasswordKey,password);

        }


        final DatabaseReference rootRef;
        rootRef= FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDbname).child(phone).exists()){
                    Users userData=dataSnapshot.child(parentDbname).child(phone).getValue(Users.class);
                    if(userData.getPhone().equals(phone )){
                        if(userData.getPassword().equals(password)){
                            if(parentDbname.equals("Admins")){
                                Toast.makeText(LoginActivity.this, " logged in Successfully .........", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                          Intent intent=new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                            }else if(parentDbname.equals("Users")){
                                Toast.makeText(LoginActivity.this, " logged in Successfully .........", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                                Prevalent.currentOnlineUsers=userData;


                                startActivity(intent);
                            }


                        }else {
                            Toast.makeText(LoginActivity.this, "The password is incorrect", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }


                }else{
                    Toast.makeText(LoginActivity.this, "Account :  with this"+phone+" number do not exists...", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "You Need to Create An Account", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
