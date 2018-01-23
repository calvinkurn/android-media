package com.tokopedia.tokocash.qrpayment.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 1/2/18.
 */

public class InfoQrTokoCash implements Parcelable {

    private String name;
    private String email;
    private String phoneNumber;
    private long amount;

    public InfoQrTokoCash() {
    }


    protected InfoQrTokoCash(Parcel in) {
        name = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        amount = in.readLong();
    }

    public static final Creator<InfoQrTokoCash> CREATOR = new Creator<InfoQrTokoCash>() {
        @Override
        public InfoQrTokoCash createFromParcel(Parcel in) {
            return new InfoQrTokoCash(in);
        }

        @Override
        public InfoQrTokoCash[] newArray(int size) {
            return new InfoQrTokoCash[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(phoneNumber);
        parcel.writeLong(amount);
    }
}
