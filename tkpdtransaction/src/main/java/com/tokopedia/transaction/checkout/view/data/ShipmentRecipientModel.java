package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Aghny A. Putra on 25/01/18
 */

public class ShipmentRecipientModel implements Parcelable {

    private String recipientName;
    private String recipientAddress;

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.recipientName);
        dest.writeString(this.recipientAddress);
    }

    public ShipmentRecipientModel() {
    }

    protected ShipmentRecipientModel(Parcel in) {
        this.recipientName = in.readString();
        this.recipientAddress = in.readString();
    }

    public static final Parcelable.Creator<ShipmentRecipientModel> CREATOR = new Parcelable.Creator<ShipmentRecipientModel>() {
        @Override
        public ShipmentRecipientModel createFromParcel(Parcel source) {
            return new ShipmentRecipientModel(source);
        }

        @Override
        public ShipmentRecipientModel[] newArray(int size) {
            return new ShipmentRecipientModel[size];
        }
    };
}
