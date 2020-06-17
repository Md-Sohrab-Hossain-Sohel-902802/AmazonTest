package com.example.amazontest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.amazontest.DataModuler.Users;
import com.example.amazontest.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {



    private Button joinNowButton,loginButton;
    private ProgressDialog loadingBar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        joinNowButton=findViewById(R.id.main_join_now_Button);
        loginButton=findViewById(R.id.main_login_button);

        loadingBar=new ProgressDialog(this);
        Paper.init(this);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });


        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });



        String userphonekey=Paper.book().read(Prevalent.userPhonekey);
        String userpasswordkey=Paper.book().read(Prevalent.userPasswordKey);

        if(userphonekey!="" && userpasswordkey!=""){
            if(!TextUtils.isEmpty(userphonekey) && !TextUtils.isEmpty(userpasswordkey)){
                AllowAccess(userphonekey,userpasswordkey);
                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait, while we are checking the credentials.");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

            }

        }


    }


    private void AllowAccess(final String phone, final String password) {


        final DatabaseReference rootRef;
        rootRef= FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(phone).exists()){
                    Users userData=dataSnapshot.child("Users").child(phone).getValue(Users.class);
                    if(userData.getPhone().equals(phone )){
                        if(userData.getPassword().equals(password)){

                            Toast.makeText(MainActivity.this, " logged in Successfully .........", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                            Prevalent.currentOnlineUsers=userData;
                            startActivity(intent);
                        }else {
                            Toast.makeText(MainActivity.this, "The password is incorrect", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }


                }else{
                    Toast.makeText(MainActivity.this, "Account :  with this"+phone+" number do not exists...", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this, "You Need to Create An Account", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });









    }



}
