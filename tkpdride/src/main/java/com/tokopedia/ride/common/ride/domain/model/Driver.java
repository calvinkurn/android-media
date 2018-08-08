package com.tokopedia.ride.common.ride.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alvarisi on 3/24/17.
 */

public class Driver implements Parcelable {
    private String phoneNumber;
    private String smsNumber;
    private String rating;
    private String pictureUrl;
    private String name;

    public Driver() {
    }

    protected Driver(Parcel in) {
        phoneNumber = in.readString();
        smsNumber = in.readString();
        rating = in.readString();
        pictureUrl = in.readString();
        name = in.readString();
    }

    public static final Creator<Driver> CREATOR = new Creator<Driver>() {
        @Override
        public Driver createFromParcel(Parcel in) {
            return new Driver(in);
        }

        @Override
        public Driver[] newArray(int size) {
            return new Driver[size];
        }
    };

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSmsNumber() {
        return smsNumber;
    }

    public void setSmsNumber(String smsNumber) {
        this.smsNumber = smsNumber;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(phoneNumber);
        parcel.writeString(smsNumber);
        parcel.writeString(rating);
        parcel.writeString(pictureUrl);
        parcel.writeString(name);
    }
}
