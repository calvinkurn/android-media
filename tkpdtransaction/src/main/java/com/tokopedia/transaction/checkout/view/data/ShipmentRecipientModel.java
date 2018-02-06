package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Aghny A. Putra on 25/01/18
 */
public class ShipmentRecipientModel implements Parcelable {

    private boolean isPrimerAddress;
    private String addressIdentifier;
    private String recipientName;
    private String recipientAddress;
    private String recipientPhone;

    public ShipmentRecipientModel() {
    }

    public boolean isPrimerAddress() {
        return isPrimerAddress;
    }

    public void setPrimerAddress(boolean isPrimerAddress) {
        this.isPrimerAddress = isPrimerAddress;
    }

    public String getAddressIdentifier() {
        return addressIdentifier;
    }

    public void setAddressIdentifier(String addressIdentifier) {
        this.addressIdentifier = addressIdentifier;
    }

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

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isPrimerAddress ? (byte) 1 : (byte) 0);
        dest.writeString(this.addressIdentifier);
        dest.writeString(this.recipientName);
        dest.writeString(this.recipientAddress);
        dest.writeString(this.recipientPhone);
    }

    protected ShipmentRecipientModel(Parcel in) {
        this.isPrimerAddress = in.readByte() != 0;
        this.addressIdentifier = in.readString();
        this.recipientName = in.readString();
        this.recipientAddress = in.readString();
        this.recipientPhone = in.readString();
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
