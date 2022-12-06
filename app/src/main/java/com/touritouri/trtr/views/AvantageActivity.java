package com.touritouri.trtr.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.touritouri.trtr.R;
import com.touritouri.trtr.controllers.avantage.AvantageAdapter;
import com.touritouri.trtr.controllers.site.SiteAdapter;
import com.touritouri.trtr.models.Avantage;
import com.touritouri.trtr.models.Site;

import java.util.ArrayList;
import java.util.List;

public class AvantageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AvantageAdapter adapter;

    private FirebaseFirestore firestore;
    private CollectionReference reference;

    private final String collectionComplementPath = "complements";
    private List<Avantage> avantages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avantage);

        String filter = "";
        String site_id="";

        Bundle bundle = getIntent().getExtras();

        if (bundle!=null && !TextUtils.isEmpty(bundle.getString("query"))){
            filter =bundle.getString("query");
            site_id =bundle.getString("site_id");
        }

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(filter.toLowerCase());

        }

        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        }

        recyclerView = findViewById(R.id.recAvantage);

        firestore = FirebaseFirestore.getInstance();


        Log.d("TAG", "onCreate: "+filter);

        getData(filter.toLowerCase(),site_id);

    }


    private void getData(String filter,String site_id){
        Query query = null;
        reference= firestore.collection(collectionComplementPath);

        query = reference.whereEqualTo("category",filter).whereEqualTo("site_id",site_id);

        avantages = new ArrayList<>();
        LinearLayoutManager lLayoutManager = new GridLayoutManager(AvantageActivity.this,2);
        recyclerView.setLayoutManager(lLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        avantages.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                            avantages.add(new Avantage(
                                    document.getId(),
                                    document.getString("image"),
                                    document.getString("name"),
                                    document.getString("site_id"))
                            );

                            Log.d("TAG", document.getId() + " => " + document.getData());
                        }

                        adapter = new AvantageAdapter(AvantageActivity.this, avantages );
                        recyclerView.setAdapter(adapter);

                        if (avantages.isEmpty() || avantages.size()<0){
                            findViewById(R.id.notFound).setVisibility(View.VISIBLE);
                        }
                    }
                });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                AvantageActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}