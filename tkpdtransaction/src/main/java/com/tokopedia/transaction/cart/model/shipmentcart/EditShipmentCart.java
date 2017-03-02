package com.tokopedia.transaction.cart.model.shipmentcart;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvarisi on 11/3/16.
 */

public class EditShipmentCart implements Parcelable {
    private String message;
    private String status;

    public EditShipmentCart() {
    }

    protected EditShipmentCart(Parcel in) {
        message = in.readString();
        status = in.readString();
    }

    public static final Creator<EditShipmentCart> CREATOR = new Creator<EditShipmentCart>() {
        @Override
        public EditShipmentCart createFromParcel(Parcel in) {
            return new EditShipmentCart(in);
        }

        @Override
        public EditShipmentCart[] newArray(int size) {
            return new EditShipmentCart[size];
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
