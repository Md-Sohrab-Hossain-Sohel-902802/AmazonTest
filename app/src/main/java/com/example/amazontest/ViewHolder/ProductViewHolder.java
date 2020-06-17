package com.example.amazontest.ViewHolder;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amazontest.Interface.ItemClickListner;
import com.example.amazontest.R;

public class ProductViewHolder  extends RecyclerView.ViewHolder  implements View.OnClickListener {


    public TextView txtproductname,txtProductdescription,txtProductPrice;
    public ImageView imageView;
    public ItemClickListner listner;



    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);


        imageView=itemView.findViewById(R.id.product_image);
        txtproductname=itemView.findViewById(R.id.product_name);
        txtProductdescription=itemView.findViewById(R.id.product_description);
        txtProductPrice=itemView.findViewById(R.id.product_price);





    }


    public  void   setItemClickLIstner(ItemClickListner lIstner){
        this.listner=listner;
    }


    @Override
    public void onClick(View v) {


        listner.onClick(v,getAdapterPosition(),false);

    }
}
