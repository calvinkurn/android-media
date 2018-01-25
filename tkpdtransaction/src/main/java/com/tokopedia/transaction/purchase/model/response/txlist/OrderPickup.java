package com.tokopedia.transaction.purchase.model.response.txlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 04/01/18.
 */

public class OrderPickup implements Parcelable {

    @SerializedName("pickup_code")
    @Expose
    private String pickupCode;

    public OrderPickup() {
    }

    protected OrderPickup(Parcel in) {
        pickupCode = in.readString();
    }

    public static final Creator<OrderPickup> CREATOR = new Creator<OrderPickup>() {
        @Override
        public OrderPickup createFromParcel(Parcel in) {
            return new OrderPickup(in);
        }

        @Override
        public OrderPickup[] newArray(int size) {
            return new OrderPickup[size];
        }
    };

    public String getPickupCode() {
        return pickupCode;
    }

    public void setPickupCode(String pickupCode) {
        this.pickupCode = pickupCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pickupCode);
    }
}
