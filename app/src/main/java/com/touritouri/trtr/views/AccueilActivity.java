package com.touritouri.trtr.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.touritouri.trtr.R;
import com.touritouri.trtr.controllers.departement.DepartementAdapter;
import com.touritouri.trtr.controllers.site.SiteAdapter;
import com.touritouri.trtr.models.Departement;
import com.touritouri.trtr.models.Site;

import java.util.ArrayList;
import java.util.List;


public class AccueilActivity extends AppCompatActivity {
    private RecyclerView recyclerViewDepartements , recyclerViewSites;
    private SearchView searchView;

    private FirebaseFirestore firestore;
    private CollectionReference reference;
    private String collectionDepartementPath = "departements";
    private String collectionSitePath = "sites";
    private List<Departement> departements;
    private List<Site> sites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        recyclerViewDepartements = findViewById(R.id.recDepartement);
        recyclerViewSites = findViewById(R.id.recSites);
        searchView = findViewById(R.id.searchSite);

        firestore = FirebaseFirestore.getInstance();

        getDepartementData();

        getSiteData();

        findViewById(R.id.allSiteBtn).setOnClickListener(v -> {
            startActivity(
                    new Intent(AccueilActivity.this,AllSiteActivity.class)
                            .putExtra("departement",new Departement("","",""))
            );
        });

        searchView.setIconifiedByDefault(false);
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startActivity(new Intent(AccueilActivity.this, AllSiteActivity.class).putExtra("query",query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void getDepartementData(){
        reference= firestore.collection(collectionDepartementPath);
        departements = new ArrayList<>();
        LinearLayoutManager hLayoutManager = new LinearLayoutManager(AccueilActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDepartements.setLayoutManager(hLayoutManager);

        recyclerViewSites.setNestedScrollingEnabled(false);

        reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                departements.clear();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                    departements.add(new Departement(
                            document.getId(),
                            document.getString("name"),
                            document.getString("image")
                    ));
                    Log.d("TAG", document.getId() + " => " + document.getData());
                }

                DepartementAdapter adapter = new DepartementAdapter(AccueilActivity.this, departements );
                recyclerViewDepartements.setAdapter(adapter);
            }
        });

    }

    private void getSiteData(){
        reference= firestore.collection(collectionSitePath);
        sites = new ArrayList<>();
        LinearLayoutManager lLayoutManager = new LinearLayoutManager(AccueilActivity.this);
        recyclerViewSites.setLayoutManager(lLayoutManager);
        recyclerViewSites.setNestedScrollingEnabled(false);

        reference.orderBy("name", Query.Direction.DESCENDING)
                .limit(10)
                .get()
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
                            site.setAvantages((List<String>) document.get("avantages"));
                            site.setDepartement(document.getString("departement"));
                            site.setDescription(document.getString("description"));
                            site.setPrice(Integer.parseInt(String.valueOf(document.getLong("price"))));
                            site.setStars(Integer.parseInt(String.valueOf(document.getLong("stars"))));
                            site.setVisite(Integer.parseInt(String.valueOf(document.getLong("visite"))));

                            sites.add(site);

                            Log.d("TAG", document.getId() + " => " + document.getData());
                        }

                        SiteAdapter adapter = new SiteAdapter(AccueilActivity.this, sites );
                        recyclerViewSites.setAdapter(adapter);
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.app_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                startActivity(new Intent(AccueilActivity.this,AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}