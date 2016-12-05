package com.tokopedia.transaction.cart.model.shipmentcart;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvarisi on 11/3/16.
 */

public class ShipmentCartData implements Parcelable {
    @SerializedName("message_status")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public ShipmentCartData() {
    }

    protected ShipmentCartData(Parcel in) {
        message = in.readString();
        status = in.readString();
    }

    public static final Creator<ShipmentCartData> CREATOR = new Creator<ShipmentCartData>() {
        @Override
        public ShipmentCartData createFromParcel(Parcel in) {
            return new ShipmentCartData(in);
        }

        @Override
        public ShipmentCartData[] newArray(int size) {
            return new ShipmentCartData[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(message);
        parcel.writeString(status);
    }
}
