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
    private Long amount;

    public InfoQrTokoCash() {
    }

    protected InfoQrTokoCash(Parcel in) {
        name = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        if (in.readByte() == 0) {
            amount = null;
        } else {
            amount = in.readLong();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phoneNumber);
        if (amount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(amount);
        }
    }

    @Override
    public int describeContents() {
        return 0;
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

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
