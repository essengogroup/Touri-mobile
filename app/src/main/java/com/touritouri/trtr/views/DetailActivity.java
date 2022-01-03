package com.touritouri.trtr.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.touritouri.trtr.R;
import com.touritouri.trtr.controllers.galery.ViewPagerAdapter;
import com.touritouri.trtr.models.Site;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DetailActivity extends AppCompatActivity {
    private TextView siteDescription, siteDepartement, siteStar,sitePrice,siteConsigne;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private RatingBar siteRating;

    private Site site;
    private List<String> galery = new ArrayList<>();
    private List<String> avantages = new ArrayList<>();

    private ChipGroup chipsPrograms;
    private Chip mChip;

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
        siteRating = findViewById(R.id.siteRating);
        chipsPrograms = findViewById(R.id.chipsPrograms);

        galery = site.getGalery();
        avantages = site.getAvantages();
        siteDescription.setText(site.getDescription());
        siteStar.setText(String.valueOf(site.getVisite()));
        siteDepartement.setText(site.getDepartement());
        siteConsigne.setText(site.getConsign());
        sitePrice.setText(String.valueOf(site.getPrice())+" XAF");
        siteRating.setRating((float) site.getStars());

        if (!avantages.isEmpty()){
            setCategoryChips(avantages);
        }

        mViewPagerAdapter = new ViewPagerAdapter(DetailActivity.this, galery);
        mViewPager.setAdapter(mViewPagerAdapter);


        ExtendedFloatingActionButton fab = findViewById(R.id.reserverSiteBtn);
        fab.setOnClickListener(v -> startActivity(new Intent(DetailActivity.this, ReservationActivity.class).putExtra("site", site)));


        Timer timer= new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 3000, 5000);
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


    public void setCategoryChips(List<String> date_visite) {
        for (String category : date_visite) {
            mChip = (Chip) this.getLayoutInflater().inflate(R.layout.item_chip_category, null, false);
            mChip.setText(category);
            mChip.setChipIcon(getResources().getDrawable(getIcon(category)));
            mChip.setClickable(false);

            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10,
                    getResources().getDisplayMetrics()
            );
            mChip.setPadding(paddingDp, 0, paddingDp, 0);
            mChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    Log.d("TAG", "onCheckedChanged: " + compoundButton.isChecked());
                }
            });
            chipsPrograms.addView(mChip);

        }
    }

    private int getIcon(String category){
        int chipIcon = 0;
        if (category.equalsIgnoreCase("déplacement")){
            chipIcon= R.drawable.ic_baseline_directions_bus_24;
        }
        else if (category.equalsIgnoreCase("boutique")){
            chipIcon= R.drawable.ic_baseline_shopping_bag_24;
        }
        else if (category.equalsIgnoreCase("restaurant")){
            chipIcon= R.drawable.ic_baseline_restaurant_24;
        }
        else if (category.equalsIgnoreCase("assistance")){
            chipIcon= R.drawable.ic_baseline_assistant_24;
        }
        else if (category.equalsIgnoreCase("sécurité")){
            chipIcon= R.drawable.ic_baseline_security_24;
        }
        else if (category.equalsIgnoreCase("santé")){
            chipIcon= R.drawable.ic_baseline_local_hospital_24;
        }

        return chipIcon;
    }

    class SliderTimer extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(() -> {
                if(mViewPager.getCurrentItem()< galery.size()-1){
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
                }else {
                    mViewPager.setCurrentItem(0);
                }
            });
        }
    }
}
