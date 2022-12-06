package com.touritouri.trtr.models;

import androidx.annotation.NonNull;

public class Avantage {
    private String image;
    private String title;
    private String id;
    private String site_id;

    public Avantage() {
    }

    public Avantage(String id,String image, String title,String site_id) {
        this.image = image;
        this.title = title;
        this.id = id;
        this.site_id=site_id;
    }

    public Avantage(String image, String title) {
        this.image = image;
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    @NonNull
    @Override
    public String toString() {
        return title+" "+id+" "+image;
    }
}
