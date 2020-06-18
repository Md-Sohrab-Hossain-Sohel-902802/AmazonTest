package com.example.amazontest.Admin;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.amazontest.Prevalent.Prevalent;
import com.example.amazontest.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {




    private  String  categoryName,description,price,productname,saveCurrentDate,saveCurrentTime;

    private Button addnewProductButton;
    private EditText inputProductname,inputproductdescription,inputProductprice;
    private ImageView inputProductImage;

    private ProgressDialog loadingBar;

    private  static  final  int GalleryPic=1;

    private Uri imageUri;

    private  String productRandomkey;



    private StorageReference productImagesRef;
    private DatabaseReference databaseReference;
    private  String downloadImageurl;





    //<---------------for testing--------------->


    private StorageReference storageReference;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);



        categoryName=getIntent().getStringExtra("category").toString();

        productImagesRef= FirebaseStorage.getInstance().getReference().child("Products_Image");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Products");

        addnewProductButton=findViewById(R.id.add_new_ProductButton);
        inputProductImage=findViewById(R.id.select_Product_Image);
        inputProductname=findViewById(R.id.product_name);
        inputproductdescription=findViewById(R.id.product_description);
        inputProductprice=findViewById(R.id.product_price);
        loadingBar=new ProgressDialog(this);




      //  <-------------for testing-------------------->


        storageReference=FirebaseStorage.getInstance().getReference();















        inputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });



        addnewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uploadPDFFile(imageUri);

                  ValidateProductData();
            }
        });







    }








    private void ValidateProductData() {

        description=inputproductdescription.getText().toString();
        price=inputProductprice.getText().toString();
        productname=inputProductname.getText().toString();



        if(imageUri == null){
            Toast.makeText(this, Prevalent.currentOnlineUsers.getName() +": Product Image Mandatory", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(description)){

            Toast.makeText(this, "Please Write Product Description", Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(price)){

            Toast.makeText(this, "Please Write Product Price", Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(productname)){

            Toast.makeText(this, "Please Write Product name", Toast.LENGTH_SHORT).show();

        }else{

            storeProductInformation();






        }




    }

    private void storeProductInformation() {

        loadingBar.setTitle("Adding New Product");
        loadingBar.setMessage("Please wait, while we are Adding the new Product");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        Calendar calendar=Calendar.getInstance();


        SimpleDateFormat currentDate=new SimpleDateFormat("MM dd,yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime=currentTime.format(calendar.getTime());

        productRandomkey=saveCurrentDate+saveCurrentTime;


        final StorageReference filePath=productImagesRef.child(imageUri.getLastPathSegment()+productRandomkey+".jpg");

        final UploadTask uploadTask=filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message=e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Error : "+message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw  task.getException();


                        }
                        downloadImageurl=filePath.getDownloadUrl().toString();
                        return  filePath.getDownloadUrl();



                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){

                            downloadImageurl=task.getResult().toString();
                            saveProductInfoToDatabase();

                        }
                    }
                });

            }
        });

    }

    private void saveProductInfoToDatabase() {


        HashMap<String,Object> productMap=new HashMap<>();
        productMap.put("pid",productRandomkey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",description);
        productMap.put("image",downloadImageurl);
        productMap.put("category",categoryName);
        productMap.put("price",price);
        productMap.put("pname",productname);


        databaseReference.child(productRandomkey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Intent intent=new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);


                    loadingBar.dismiss();
                    Toast.makeText(AdminAddNewProductActivity.this, "Product is Added Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }else{
                    loadingBar.dismiss();
                    Toast.makeText(AdminAddNewProductActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void OpenGallery() {

        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,GalleryPic);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPic && resultCode==RESULT_OK && data.getData()!=null){
            imageUri=data.getData();
            inputProductImage.setImageURI(data.getData());
        }

    }
}
