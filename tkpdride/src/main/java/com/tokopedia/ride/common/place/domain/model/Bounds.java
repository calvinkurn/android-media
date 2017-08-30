package com.tokopedia.ride.common.place.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.ride.common.ride.domain.model.Location;

/**
 * Created by alvarisi on 6/7/17.
 */

public class Bounds implements Parcelable{
    private Location northeast;
    private Location southwest;

    protected Bounds(Parcel in) {
        northeast = in.readParcelable(Location.class.getClassLoader());
        southwest = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Creator<Bounds> CREATOR = new Creator<Bounds>() {
        @Override
        public Bounds createFromParcel(Parcel in) {
            return new Bounds(in);
        }

        @Override
        public Bounds[] newArray(int size) {
            return new Bounds[size];
        }
    };

    public Bounds() {
    }

    public Location getNortheast() {
        return northeast;
    }

    public void setNortheast(Location northeast) {
        this.northeast = northeast;
    }

    public Location getSouthwest() {
        return southwest;
    }

    public void setSouthwest(Location southwest) {
        this.southwest = southwest;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(northeast, i);
        parcel.writeParcelable(southwest, i);
    }
}
