package com.tokopedia.flight.search.data.cloud.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 10/26/2017.
 */

public class Amenity implements Parcelable {

    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("label")
    @Expose
    private String label;

    public String getIcon() {
        return icon;
    }
    public String getLabel() {
        return label;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.icon);
        dest.writeString(this.label);
    }

    public Amenity() {
    }

    protected Amenity(Parcel in) {
        this.icon = in.readString();
        this.label = in.readString();
    }

    public static final Parcelable.Creator<Amenity> CREATOR = new Parcelable.Creator<Amenity>() {
        @Override
        public Amenity createFromParcel(Parcel source) {
            return new Amenity(source);
        }

        @Override
        public Amenity[] newArray(int size) {
            return new Amenity[size];
        }
    };
}
