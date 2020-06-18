package com.example.amazontest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amazontest.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity{


    private CircleImageView profileImageView;
    private EditText fullNameEdittext,userPhoneEdittext,addressEditText;
    private TextView profileChangeTextBtn,closeBtn,saveTextButton;


    private StorageTask uploadTast;
    private Uri imageUri;
    private  String myUrl="";
    private StorageReference  storageProfilePictureREf;
    private  String checker="";
    private Button securityQuestionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        storageProfilePictureREf= FirebaseStorage.getInstance().getReference().child("Profile_Pictures");

        profileImageView=findViewById(R.id.settings_Profiel_Image);

        fullNameEdittext=findViewById(R.id.settings_full_name);
        userPhoneEdittext=findViewById(R.id.settings_phone_number);
        addressEditText=findViewById(R.id.settings_address);

        profileChangeTextBtn=findViewById(R.id.profile_Image_change_btn);
        closeBtn=findViewById(R.id.close_settings);
        saveTextButton=findViewById(R.id.update_account_settings);
        securityQuestionButton=findViewById(R.id.sequrity_question_btn);




        userInfoDisplay(profileImageView,fullNameEdittext,userPhoneEdittext,addressEditText);



        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                finish();
            }
        });



        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                if(checker.equals("clicked")){
                                        userinfoSaved();
                                }else{
                                           updateOnlyuserInfo();
                                }
            }
        });

        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            checker="clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);





            }
        });


        securityQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsActivity.this,ResetPasswordActivity.class);

                intent.putExtra("check","settings");
                startActivity(intent);
            }
        });




















    }

    private void updateOnlyuserInfo() {


      DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String,Object> userlistMap=new HashMap<>();
        userlistMap.put("phone",userPhoneEdittext.getText().toString());
        userlistMap.put("address",addressEditText.getText().toString());
        userlistMap.put("name",fullNameEdittext.getText().toString());

        ref.child(Prevalent.currentOnlineUsers.getPhone()).updateChildren(userlistMap);

        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));

        Toast.makeText(SettingsActivity.this, "Profile Info Updated", Toast.LENGTH_SHORT).show();







    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK && data!=null){
                    CropImage.ActivityResult result=CropImage.getActivityResult(data);
                    imageUri=result.getUri();
                    profileImageView.setImageURI(imageUri);
        }else{
            Toast.makeText(this, "Error , Try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
            finish();
        }


    }

    private void userinfoSaved() {


        if(TextUtils.isEmpty(fullNameEdittext.getText().toString())){
            Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(addressEditText.getText().toString())){
            Toast.makeText(this, "Address  is mandatory.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(userPhoneEdittext.getText().toString())){
            Toast.makeText(this, "Phone Number  is mandatory.", Toast.LENGTH_SHORT).show();
        }else if(checker.equals("clicked")){
                        uploadFileTEst();
                     //   uploadImage();
              }



    }


    private void uploadFileTEst() {


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please Wait ,while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        if (imageUri != null) {

            final StorageReference fileRef = storageProfilePictureREf.child(Prevalent.currentOnlineUsers.getPhone() + ".jpg");
            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uri=taskSnapshot.getStorage().getDownloadUrl();
                    while (!uri.isSuccessful());
                    Uri url=uri.getResult();

                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");

                    HashMap<String,Object> userlistMap=new HashMap<>();
                    userlistMap.put("phone",userPhoneEdittext.getText().toString());
                    userlistMap.put("address",addressEditText.getText().toString());
                    userlistMap.put("name",fullNameEdittext.getText().toString());
                    userlistMap.put("image",url.toString());

                    ref.child(Prevalent.currentOnlineUsers.getPhone()).updateChildren(userlistMap);


                    progressDialog.dismiss();

                    startActivity(new Intent(SettingsActivity.this,HomeActivity.class));

                    Toast.makeText(SettingsActivity.this, "Profile Info Updated", Toast.LENGTH_SHORT).show();



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SettingsActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }





    private void uploadImage() {

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please Wait ,while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        if(imageUri!=null){
            final StorageReference fileRef=storageProfilePictureREf.child(Prevalent.currentOnlineUsers.getPhone()+".jpg");
            uploadTast=fileRef.putFile(imageUri);
            uploadTast.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if(task.isSuccessful()){
                        throw  task.getException();
                    }



                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUrl=task.getResult();
                        myUrl=downloadUrl.toString();
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String,Object> userlistMap=new HashMap<>();
                        userlistMap.put("phone",userPhoneEdittext.getText().toString());
                        userlistMap.put("address",addressEditText.getText().toString());
                        userlistMap.put("name",fullNameEdittext.getText().toString());
                        userlistMap.put("image",myUrl);

                        ref.child(Prevalent.currentOnlineUsers.getPhone()).updateChildren(userlistMap);


                    progressDialog.dismiss();
                    
                    startActivity(new Intent(SettingsActivity.this,MainActivity.class));

                        Toast.makeText(SettingsActivity.this, "Profile Info Updated", Toast.LENGTH_SHORT).show();

                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(this, "Image is not selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEdittext, final EditText userPhoneEdittext, final EditText addressEditText) {

        DatabaseReference userRef= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUsers.getPhone());

userRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                        if(dataSnapshot.child("image").exists()){
                            String image=dataSnapshot.child("image").getValue().toString();
                            String name=dataSnapshot.child("name").getValue().toString();
                            String phone=dataSnapshot.child("phone").getValue().toString();
                            String address=dataSnapshot.child("address").getValue().toString();


                            Picasso.get().load(image).into(profileImageView);

                            fullNameEdittext.setText(name);
                            userPhoneEdittext.setText(phone);
                            addressEditText.setText(address);





                        }
            }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});







    }
}
