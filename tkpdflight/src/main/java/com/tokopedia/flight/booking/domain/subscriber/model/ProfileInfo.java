package com.tokopedia.flight.booking.domain.subscriber.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alvarisi on 1/16/18.
 */

public class ProfileInfo implements Parcelable {
    private String fullname;
    private String email;
    private String phoneNumber;

    protected ProfileInfo(Parcel in) {
        fullname = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
    }

    public static final Creator<ProfileInfo> CREATOR = new Creator<ProfileInfo>() {
        @Override
        public ProfileInfo createFromParcel(Parcel in) {
            return new ProfileInfo(in);
        }

        @Override
        public ProfileInfo[] newArray(int size) {
            return new ProfileInfo[size];
        }
    };

    public ProfileInfo() {
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fullname);
        parcel.writeString(email);
        parcel.writeString(phoneNumber);
    }
}
