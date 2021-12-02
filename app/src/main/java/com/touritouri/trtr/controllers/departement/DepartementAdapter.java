package com.touritouri.trtr.controllers.departement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.touritouri.trtr.R;
import com.touritouri.trtr.models.Departement;

import java.util.List;

public class DepartementAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<Departement> departements;

    public DepartementAdapter(Context context, List<Departement> departements) {
        this.context = context;
        this.departements = departements;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.departement_item,parent,false);
        return new MyViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Departement departement = departements.get(position);
        holder.departementName.setText(departement.getName());
    }

    @Override
    public int getItemCount() {
        return departements.size();
    }
}
