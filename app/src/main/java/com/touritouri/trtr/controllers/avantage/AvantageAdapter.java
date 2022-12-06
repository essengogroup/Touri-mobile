package com.touritouri.trtr.controllers.avantage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.touritouri.trtr.R;
import com.touritouri.trtr.models.Avantage;
import com.touritouri.trtr.views.AllSiteActivity;

import java.util.List;

public class AvantageAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<Avantage> avantages;

    public AvantageAdapter() {
    }

    public AvantageAdapter(Context context, List<Avantage> avantages) {
        this.context = context;
        this.avantages = avantages;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.avantage_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Avantage avantage=avantages.get(position);

        Glide.with(context)
                .load(avantage.getImage())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.image);

        holder.title.setText(avantage.getTitle());
    }

    @Override
    public int getItemCount() {
        return avantages.size();
    }
}
