package com.touritouri.trtr.controllers.site;


import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.touritouri.trtr.R;

public class MyViewHolder extends RecyclerView.ViewHolder {
    protected MaterialCardView cardView;
    protected ImageView imageSite;
    protected TextView titreSite, departementSite,descriptionSite,priceSite,starSite;
    protected MaterialButton buttonVisiter;
    protected RatingBar siteRating;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        cardView = itemView.findViewById(R.id.cardSite);
        imageSite = itemView.findViewById(R.id.imageSite);
        titreSite = itemView.findViewById(R.id.titreSite);
        starSite = itemView.findViewById(R.id.starSite);
        priceSite = itemView.findViewById(R.id.priceSite);
        departementSite = itemView.findViewById(R.id.departementSite);
        descriptionSite = itemView.findViewById(R.id.descriptionSite);
        buttonVisiter = itemView.findViewById(R.id.visiterSiteBtn);
        siteRating = itemView.findViewById(R.id.siteRating);
    }
}
