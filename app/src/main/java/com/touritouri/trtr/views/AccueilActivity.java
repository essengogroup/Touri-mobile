package com.touritouri.trtr.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.touritouri.trtr.R;
import com.touritouri.trtr.controllers.departement.DepartementAdapter;
import com.touritouri.trtr.models.Departement;

import java.util.ArrayList;
import java.util.List;

public class AccueilActivity extends AppCompatActivity {
    private RecyclerView recyclerViewDepartements , recyclerViewSites;
    private DepartementAdapter departementAdapter ;
    private List<Departement> departements= new ArrayList<>();

    private FirebaseFirestore firestore;
    private CollectionReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        recyclerViewDepartements = findViewById(R.id.recDepartement);
        recyclerViewSites = findViewById(R.id.recSites);

        firestore = FirebaseFirestore.getInstance();
        reference= firestore.collection("departements");

        getDepartements();

    }

    private void getDepartements(){
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("TAG", document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }

}