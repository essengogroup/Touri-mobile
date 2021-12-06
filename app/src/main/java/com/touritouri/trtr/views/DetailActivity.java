package com.touritouri.trtr.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.touritouri.trtr.R;
import com.touritouri.trtr.controllers.galery.ViewPagerAdapter;
import com.touritouri.trtr.models.Site;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private TextView siteDescription, siteDepartement, siteStar,sitePrice,siteConsigne;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    private Site site;
    private List<String> galery = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        site = getIntent().getParcelableExtra("site");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
            actionBar.setTitle(site.getName());
        }

        siteDescription = findViewById(R.id.siteDescription);
        mViewPager = findViewById(R.id.viewPagerMain);
        siteDepartement = findViewById(R.id.departementSite);
        sitePrice = findViewById(R.id.priceSite);
        siteStar = findViewById(R.id.starSite);
        siteConsigne = findViewById(R.id.siteConsigne);

        galery = site.getGalery();
        siteDescription.setText(site.getDescription());
        siteStar.setText(String.valueOf(site.getStars()));
        siteDepartement.setText(site.getDepartement());
        siteConsigne.setText(site.getConsign());
        sitePrice.setText(String.valueOf(site.getPrice())+" XAF");

        mViewPagerAdapter = new ViewPagerAdapter(DetailActivity.this, galery);
        mViewPager.setAdapter(mViewPagerAdapter);


        ExtendedFloatingActionButton fab = findViewById(R.id.reserverSiteBtn);
        fab.setOnClickListener(v -> startActivity(new Intent(DetailActivity.this, ReservationActivity.class).putExtra("site", site)));


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                DetailActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
