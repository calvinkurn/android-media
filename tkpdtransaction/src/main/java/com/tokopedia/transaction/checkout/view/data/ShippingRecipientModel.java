package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Aghny A. Putra on 25/01/18
 */

public class ShippingRecipientModel implements Parcelable {

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

    public ShippingRecipientModel() {
    }

    protected ShippingRecipientModel(Parcel in) {
        this.recipientName = in.readString();
        this.recipientAddress = in.readString();
    }

    public static final Parcelable.Creator<ShippingRecipientModel> CREATOR = new Parcelable.Creator<ShippingRecipientModel>() {
        @Override
        public ShippingRecipientModel createFromParcel(Parcel source) {
            return new ShippingRecipientModel(source);
        }

        @Override
        public ShippingRecipientModel[] newArray(int size) {
            return new ShippingRecipientModel[size];
        }
    };
}
