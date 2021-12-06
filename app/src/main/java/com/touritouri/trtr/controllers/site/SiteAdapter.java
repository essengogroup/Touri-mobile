package com.touritouri.trtr.controllers.site;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.touritouri.trtr.R;
import com.touritouri.trtr.models.Site;
import com.touritouri.trtr.views.DetailActivity;
import com.touritouri.trtr.views.ReservationActivity;

import java.util.List;

public class SiteAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<Site> sites;

    public SiteAdapter(Context context, List<Site> sites) {
        this.context = context;
        this.sites = sites;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.site_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Site site = sites.get(position);

        holder.titreSite.setText(site.getName());
        holder.descriptionSite.setText(site.getDescription());
        holder.departementSite.setText(site.getDepartement());
        holder.priceSite.setText(site.getPrice()+" FCFA");
        holder.starSite.setText(String.valueOf(site.getStars()));

        Glide.with(context)
                .load(site.getImage())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imageSite);

        holder.cardView.setOnClickListener(v->{
            context.startActivity(new Intent(context, DetailActivity.class).putExtra("site",site));
        });

        holder.buttonVisiter.setOnClickListener(v->{
            context.startActivity(new Intent(context, ReservationActivity.class).putExtra("site",site));
        });
    }

    @Override
    public int getItemCount() {
        return sites.size();
    }
}
