package com.tokopedia.ride.bookingride.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alvarisi on 3/15/17.
 */

public class PlacePassViewModel implements Parcelable {
    private String title;
    private String placeId;
    private String address;
    private double latitude;
    private double longitude;
    private TYPE type;

    public PlacePassViewModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public enum TYPE {
        HOME,
        WORK,
        OTHER
    }

    protected PlacePassViewModel(Parcel in) {
        title = in.readString();
        placeId = in.readString();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        type = (TYPE) in.readValue(TYPE.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(placeId);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeValue(type);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PlacePassViewModel> CREATOR = new Parcelable.Creator<PlacePassViewModel>() {
        @Override
        public PlacePassViewModel createFromParcel(Parcel in) {
            return new PlacePassViewModel(in);
        }

        @Override
        public PlacePassViewModel[] newArray(int size) {
            return new PlacePassViewModel[size];
        }
    };
}