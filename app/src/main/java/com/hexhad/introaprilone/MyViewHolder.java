package com.hexhad.introaprilone;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView itm_img;
    TextView itm_name, itm_desc, itm_price;
    View v;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        itm_img = itemView.findViewById(R.id.itm_img);
        itm_name = itemView.findViewById(R.id.itm_name);
        itm_desc = itemView.findViewById(R.id.itm_desc);
        itm_price = itemView.findViewById(R.id.itm_price);
        v = itemView;
    }
}
