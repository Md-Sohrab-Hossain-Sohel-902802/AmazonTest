package com.example.amazontest.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.example.amazontest.HomeActivity;
import com.example.amazontest.MainActivity;
import com.example.amazontest.R;

import io.paperdb.Paper;

public class AdminCategoryActivity extends AppCompatActivity  implements View.OnClickListener {






    private ImageView tShirts,sportsTshirts,femaleDresses,swethers;
    private  ImageView glasses,hatsCaps,walletBagsPurses,shoes;
    private  ImageView headphonesHandFree,laptops,watches,mobilePhones;


    private Button logoutButton,checkOrdersButton,maintainProductButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);





        checkOrdersButton=findViewById(R.id.check_orders_button);
        logoutButton=findViewById(R.id.admin_logout_Button);
        maintainProductButton=findViewById(R.id.maintain_product_button);


        tShirts=findViewById(R.id.t_shirts);
        sportsTshirts=findViewById(R.id.sports_t_shirts);
        femaleDresses=findViewById(R.id.female_dresses);
        swethers=findViewById(R.id.sweather);
        glasses=findViewById(R.id.glasses);
        hatsCaps=findViewById(R.id.hats_caps);
        walletBagsPurses=findViewById(R.id.purses_bags_wallets);
        shoes=findViewById(R.id.shoes);
        headphonesHandFree=findViewById(R.id.headphones_handfree);
        laptops=findViewById(R.id.laptops_pc);
        watches=findViewById(R.id.watches);
        mobilePhones=findViewById(R.id.mobilephones);



        tShirts.setOnClickListener(this);
        sportsTshirts.setOnClickListener(this);
        femaleDresses.setOnClickListener(this);
        swethers.setOnClickListener(this);
        glasses.setOnClickListener(this);
        hatsCaps.setOnClickListener(this);
        walletBagsPurses.setOnClickListener(this);
        shoes.setOnClickListener(this);
        headphonesHandFree.setOnClickListener(this);
        laptops.setOnClickListener(this);
        watches.setOnClickListener(this);
        mobilePhones.setOnClickListener(this);



        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent intent=new Intent(AdminCategoryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        checkOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminNewOrdersActivity.class);
                startActivity(intent);

            }
        });

        maintainProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent intent=new Intent(AdminCategoryActivity.this, HomeActivity.class);
                        intent.putExtra("admin","Admin");
                        startActivity(intent);
            }
        });














    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.t_shirts){
            gotoAddProduct("tShirts");
        }else if(v.getId()==R.id.sports_t_shirts){
            gotoAddProduct("Sports tShirts");
        }else if(v.getId()==R.id.female_dresses){
            gotoAddProduct("Female Dresses");
        }else if(v.getId()==R.id.sweather){
            gotoAddProduct("Sweathers");
        }else if(v.getId()==R.id.glasses){
            gotoAddProduct("Glasses");
        }else if(v.getId()==R.id.hats_caps){
            gotoAddProduct("Hats Caps");
        }else if(v.getId()==R.id.purses_bags_wallets){
            gotoAddProduct("Wallets Bags purses");
        }else if(v.getId()==R.id.shoes){
            gotoAddProduct("Shoes");
        }else if(v.getId()==R.id.headphones_handfree){
            gotoAddProduct("Headphone handFree");
        }else if(v.getId()==R.id.laptops_pc){
            gotoAddProduct("Laptops");
        }else if(v.getId()==R.id.watches){
            gotoAddProduct("Watches");
        }else if(v.getId()==R.id.mobilephones){
            gotoAddProduct("Mobile Phones");
        }
    }


    public void  gotoAddProduct(String value){
        Intent intent=new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
        intent.putExtra("category",value);
        startActivity(intent);
    }
}
