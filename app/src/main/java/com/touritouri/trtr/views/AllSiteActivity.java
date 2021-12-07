package com.touritouri.trtr.views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.touritouri.trtr.R;
import com.touritouri.trtr.controllers.site.SiteAdapter;
import com.touritouri.trtr.models.Departement;
import com.touritouri.trtr.models.Site;

import java.util.ArrayList;
import java.util.List;

public class AllSiteActivity extends AppCompatActivity {
    private RecyclerView recyclerViewSites;
    private FirebaseFirestore firestore;
    private CollectionReference reference;

    private final String collectionSitePath = "sites";
    private List<Site> sites;

    private LinearLayout relaDepartement;
    private ImageView departementImage;
    private TextView departementName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_site);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        }

        recyclerViewSites = findViewById(R.id.recAllSite);
        relaDepartement = findViewById(R.id.relaDepartement);
        departementImage = findViewById(R.id.departementImage);
        departementName = findViewById(R.id.departementName);

        firestore = FirebaseFirestore.getInstance();

        String filter = "";

        if (getIntent() != null ){
            Departement departement = getIntent().getParcelableExtra("departement");
            filter = departement.getName();

            if (!departement.getName().equals("")){
                relaDepartement.setVisibility(View.VISIBLE);

                Glide.with(AllSiteActivity.this)
                        .load(departement.getImage())
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .into(departementImage);

                departementName.setText(departement.getName());
            }

        }else {
            filter ="";
        }
        getSiteData(filter);
    }

    private void getSiteData(String filter){
        Query query = null;
        reference= firestore.collection(collectionSitePath);
        if (TextUtils.isEmpty(filter)){
            query = reference.orderBy("name", Query.Direction.DESCENDING);
        }else {
            query = reference.whereEqualTo("departement",filter).orderBy("name", Query.Direction.DESCENDING);
        }

        sites = new ArrayList<>();
        LinearLayoutManager lLayoutManager = new LinearLayoutManager(AllSiteActivity.this);
        recyclerViewSites.setLayoutManager(lLayoutManager);
        recyclerViewSites.setNestedScrollingEnabled(false);

        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        sites.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Site site = new Site();


                            site.setConsign(document.getString("consign"));
                            site.setUid(document.getId());
                            site.setName(document.getString("name"));
                            site.setImage(document.getString("image"));
                            site.setGalery((List<String>) document.get("galery"));
                            site.setDepartement(document.getString("departement"));
                            site.setDescription(document.getString("description"));
                            site.setPrice(Integer.parseInt(String.valueOf(document.getLong("price"))));
                            site.setStars(Integer.parseInt(String.valueOf(document.getLong("stars"))));
                            site.setVisite(Integer.parseInt(String.valueOf(document.getLong("visite"))));

                            sites.add(site);

                            Log.d("TAG", document.getId() + " => " + document.getData());
                        }

                        SiteAdapter adapter = new SiteAdapter(AllSiteActivity.this, sites );
                        recyclerViewSites.setAdapter(adapter);
                    }
                });

    }
}