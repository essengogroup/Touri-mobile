package com.touritouri.trtr.controllers.departement;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.touritouri.trtr.R;

public class MyViewHolder extends RecyclerView.ViewHolder {
    protected CardView cardItem;
    protected ImageView departementImage;
    protected TextView departementName;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        cardItem = itemView.findViewById(R.id.departementCard);
        departementImage = itemView.findViewById(R.id.departementImage);
        departementName = itemView.findViewById(R.id.departementName);
    }
}
