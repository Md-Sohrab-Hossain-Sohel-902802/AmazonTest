package com.example.amazontest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amazontest.DataModuler.Products;
import com.example.amazontest.Prevalent.Prevalent;
import com.example.amazontest.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity  implements
        NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;

    private DatabaseReference productRef;
    private RecyclerView recyclerView;




    private  String type="";







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Intent intent=getIntent();

        Bundle bundle=intent.getExtras();
        if(bundle!=null){
            type=getIntent().getStringExtra("admin");

        }else{

        }




        productRef= FirebaseDatabase.getInstance().getReference().child("Products");



        Toolbar toolbar = findViewById(R.id.toolbar);

            toolbar.setTitle("Home");


        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(type.equals("Admin")){
                    Toast.makeText(HomeActivity.this, "This is For Only User", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(HomeActivity.this,CartActivity.class);
                    startActivity(intent);
                }


            }
        });



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);





        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }


        View headerView=navigationView.getHeaderView(0);
        if(!type.equals("Admin")){
            TextView userNameTExtview=headerView.findViewById(R.id.user_profile_name);
            CircleImageView profileImageView=headerView.findViewById(R.id.user_profile_Image);

            userNameTExtview.setText(Prevalent.currentOnlineUsers.getName());
            Picasso.get().load(Prevalent.currentOnlineUsers.getImage()).placeholder(R.drawable.profile).into(profileImageView);

        }

        recyclerView=findViewById(R.id.recyclermain);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));














    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productRef,Products.class)
                .build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull final Products products) {
                    holder.txtproductname.setText(products.getPname());

                    holder.txtProductdescription.setText(products.getDescription());
                    holder.txtProductPrice.setText("Price = "+products.getPrice()+"$");

                Picasso.get().load(products.getImage()).into(holder.imageView);


                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(type.equals("Admin")){
                                Intent intent=new Intent(HomeActivity.this,AdminMaintainProductActivity.class);
                                intent.putExtra("pid",products.getPid());
                                startActivity(intent);
                            }else{
                                Intent intent=new Intent(HomeActivity.this,ProductDetailsActivity.class);
                                intent.putExtra("pid",products.getPid());
                                startActivity(intent);
                            }

                        }
                    });



            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.products_item_layoute,parent,false);
               ProductViewHolder holder=new ProductViewHolder(view);
               return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        if(item.getItemId()==R.id.nav_cart){

            if(!type.equals("Admin")){
                Intent intent=new Intent(HomeActivity.this,CartActivity.class);
                startActivity(intent);
            }


        }else if(item.getItemId()==R.id.nav_Search){

            if(!type.equals("Admin")){
                Intent intent=new Intent(HomeActivity.this,SearchProductActivity.class);
                startActivity(intent);
            }



        }else if(item.getItemId()==R.id.nav_categories){
            Toast.makeText(this, "Categories", Toast.LENGTH_SHORT).show();
        }else if(item.getItemId()==R.id.nav_settings){
            if(!type.equals("Admin")){
                Intent intent=new Intent(HomeActivity.this,SettingsActivity.class);
                startActivity(intent);
            }

        }else if(item.getItemId()==R.id.nav_logout){

            if(!type.equals("Admin")){
                Paper.book().destroy();
                Intent intent=new Intent(HomeActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }




        }



        return false;
    }
}
