package com.touritouri.trtr.controllers.avantage;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.touritouri.trtr.R;

public class MyViewHolder extends RecyclerView.ViewHolder {
    protected CardView cardView;
    protected ImageView image;
    protected TextView title;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.cardAvantage);
        image = itemView.findViewById(R.id.imageAvantage);
        title = itemView.findViewById(R.id.categoryAvantage);
    }
}
