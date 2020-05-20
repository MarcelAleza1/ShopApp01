package com.example.shopapp.ViewHolder;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shopapp.Interface.ItemClickListner;
import com.example.shopapp.R;

public class ProductViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductDescription,textProductPrice;
    public ImageView imageView;
    public ItemClickListner listner;

    public ProductViewHolder(View itemView){
        super(itemView);
        imageView = itemView.findViewById(R.id.product_image);
        txtProductName = itemView.findViewById(R.id.product_name);
        txtProductDescription = itemView.findViewById(R.id.product_description);
        textProductPrice = itemView.findViewById(R.id.product_price);
    }
    public void setItemClickListner(ItemClickListner listner){
        this.listner = listner;
    }
    @Override
    public void onClick(View view){
            listner.onClick(view,getAdapterPosition(),false);
    }
}
