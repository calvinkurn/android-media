package com.tokopedia.ride.bookingride.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by alvarisi on 3/15/17.
 */

public class PlacePassViewModel implements Parcelable{
    private String title;
    private String placeId;
    private String address;
    private double latitude;
    private double longitude;

    public PlacePassViewModel() {
    }

    protected PlacePassViewModel(Parcel in) {
        title = in.readString();
        placeId = in.readString();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<PlacePassViewModel> CREATOR = new Creator<PlacePassViewModel>() {
        @Override
        public PlacePassViewModel createFromParcel(Parcel in) {
            return new PlacePassViewModel(in);
        }

        @Override
        public PlacePassViewModel[] newArray(int size) {
            return new PlacePassViewModel[size];
        }
    };

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

    public void setAndFormatLatitude(double latitude) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("#.######", otherSymbols);
        this.latitude = Double.parseDouble(decimalFormat.format(latitude));
    }

    public double getLongitude() {
        return longitude;
    }

    public void setAndFormatLongitude(double longitude) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("#.######", otherSymbols);
        this.longitude = Double.parseDouble(decimalFormat.format(longitude));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(placeId);
        parcel.writeString(address);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}