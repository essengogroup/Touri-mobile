package com.touritouri.trtr.controllers.galery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.touritouri.trtr.R;

import java.util.List;
import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {

    private List<String> galeries;
    private Context context;
    private LayoutInflater mLayoutInflater;

    public ViewPagerAdapter() {
    }

    public ViewPagerAdapter(Context context, List<String> galeries) {
        this.context = context;
        this.galeries = galeries;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return galeries.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        // inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.galery_item, container, false);

        // referencing the image view from the item.xml file
        ImageView imageView = itemView.findViewById(R.id.imageGalery);

        // setting the image in the imageView
        Glide.with(context)
                .load(galeries.get(position))
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);

        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

