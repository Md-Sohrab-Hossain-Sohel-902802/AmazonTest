package com.example.amazontest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amazontest.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {


    private  String check="";

    private TextView pageTitle;
    private EditText phoneNumber;
    private  EditText question1,question2;
    private  TextView titleQuestions;

    private Button verifyButton;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        check=getIntent().getStringExtra("check");



        pageTitle=findViewById(R.id.page_title);
        phoneNumber=findViewById(R.id.find_phone_number);
        question1=findViewById(R.id.questionONe);
        question2=findViewById(R.id.quesTionTwo);
        titleQuestions=findViewById(R.id.title_questions);
        verifyButton=findViewById(R.id.veryfyButtonid);





    }

    private void showData() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child( Prevalent.currentOnlineUsers.getPhone());
        ref.child("Security Questions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String answer1=dataSnapshot.child("answer1").getValue().toString();
                        String answer2=dataSnapshot.child("answer2").getValue().toString();

                        question1.setText(answer1);
                        question2.setText(answer2);


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



        phoneNumber.setVisibility(View.GONE);

        if(check.equals("settings")){
            showData();
            pageTitle.setText("Set Questions");
            titleQuestions.setText("Please set Answers for the Following Security Questions.");
          verifyButton.setText("Set");


          verifyButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                            setAnswer();
              }
          });


        }else if(check.equals("home")){
                phoneNumber.setVisibility(View.VISIBLE);

                verifyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        veryfyUser();
                    }
                });



        }



    }

    private void veryfyUser() {

            final String phone=phoneNumber.getText().toString();

        final String  ans1=question1.getText().toString().toLowerCase();
        final String ans2=question2.getText().toString().toLowerCase();


        if(phone.isEmpty() || ans1.isEmpty() || ans2.isEmpty()){
            Toast.makeText(this, "Please Fill up All the fields", Toast.LENGTH_SHORT).show();


        }else{

            final DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child( phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){

                        if(dataSnapshot.hasChild("Security Questions")){


                            String answer1=dataSnapshot.child("Security Questions").child("answer1").getValue().toString();
                            String answer2=dataSnapshot.child("Security Questions").child("answer2").getValue().toString();
                            if(!answer1.equals(ans1)){
                                Toast.makeText(ResetPasswordActivity.this, "Your First Answer is wrong", Toast.LENGTH_SHORT).show();
                            }else if(!answer2.equals(ans2)){
                                Toast.makeText(ResetPasswordActivity.this, "Your Second Answer is wrong", Toast.LENGTH_SHORT).show();

                            }else{

                                final AlertDialog.Builder builder=new AlertDialog.Builder(ResetPasswordActivity.this);

                                builder.setTitle("New Password");
                                final EditText newPassword=new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Write Password Here....");
                                builder.setView(newPassword);
                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {
                                        if(!newPassword.getText().toString().isEmpty()){
                                            ref.child("password").setValue(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(ResetPasswordActivity.this, "Your Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent intent=new Intent(ResetPasswordActivity.this,LoginActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });




                            builder.show();







                            }

                        }else{
                            Toast.makeText(ResetPasswordActivity.this, "you have not set the security question", Toast.LENGTH_SHORT).show();

                        }

                    }else{
                        Toast.makeText(ResetPasswordActivity.this, "Something Error : ", Toast.LENGTH_SHORT).show();
                    }



                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });











        }



    }


    public void setAnswer(){

        String  answer1=question1.getText().toString().toLowerCase();
        String answer2=question2.getText().toString().toLowerCase();

        if(answer1.isEmpty()  || answer2.isEmpty()){
            Toast.makeText(ResetPasswordActivity.this, "Please Answer The both questions", Toast.LENGTH_SHORT).show();
        }else{
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child( Prevalent.currentOnlineUsers.getPhone())
                     .child("Security Questions");


            HashMap<String,Object> question=new HashMap<>();
            question.put("answer1",answer1);
            question.put("answer2",answer2);
            ref.updateChildren(question).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ResetPasswordActivity.this, "Security Questions Setup Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ResetPasswordActivity.this,HomeActivity.class);
                        startActivity(intent);
                    }else
                    {
                        Toast.makeText(ResetPasswordActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });



        }

    }




}