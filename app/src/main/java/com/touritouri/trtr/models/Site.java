package com.touritouri.trtr.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Site implements Parcelable {
    private String consign;
    private String departement;
    private String departement_id;
    private String description;
    private List<String> galery;
    private String image;
    private String name;
    private int price;
    private int stars;
    private String uid;

    public Site() {
    }

    public Site(String consign, String departement, String departement_id, String description, List<String> galery, String image, String name, int price, int stars) {
        this.consign = consign;
        this.departement = departement;
        this.departement_id = departement_id;
        this.description = description;
        this.galery = galery;
        this.image = image;
        this.name = name;
        this.price = price;
        this.stars = stars;
    }

    protected Site(Parcel in) {
        consign = in.readString();
        departement = in.readString();
        departement_id = in.readString();
        description = in.readString();
        galery = in.createStringArrayList();
        image = in.readString();
        name = in.readString();
        price = in.readInt();
        stars = in.readInt();
        uid = in.readString();
    }

    public static final Creator<Site> CREATOR = new Creator<Site>() {
        @Override
        public Site createFromParcel(Parcel in) {
            return new Site(in);
        }

        @Override
        public Site[] newArray(int size) {
            return new Site[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(consign);
        dest.writeString(departement);
        dest.writeString(departement_id);
        dest.writeString(description);
        dest.writeStringList(galery);
        dest.writeString(image);
        dest.writeString(name);
        dest.writeInt(price);
        dest.writeInt(stars);
        dest.writeString(uid);
    }

    public String getConsign() {
        return consign;
    }

    public void setConsign(String consign) {
        this.consign = consign;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getDepartement_id() {
        return departement_id;
    }

    public void setDepartement_id(String departement_id) {
        this.departement_id = departement_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getGalery() {
        return galery;
    }

    public void setGalery(List<String> galery) {
        this.galery = galery;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}