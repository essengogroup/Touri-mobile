package com.touritouri.trtr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Departement implements Parcelable {
    private String name;
    private String image;
    private String uid;

    public Departement() {
    }

    public Departement(String uid,String name, String image) {
        this.name = name;
        this.image = image;
        this.uid = uid;
    }

    protected Departement(Parcel in) {
        name = in.readString();
        image = in.readString();
        uid = in.readString();
    }

    public static final Creator<Departement> CREATOR = new Creator<Departement>() {
        @Override
        public Departement createFromParcel(Parcel in) {
            return new Departement(in);
        }

        @Override
        public Departement[] newArray(int size) {
            return new Departement[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(uid);
    }
}
