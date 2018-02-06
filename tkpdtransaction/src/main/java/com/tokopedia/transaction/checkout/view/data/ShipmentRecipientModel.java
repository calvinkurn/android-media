package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.transaction.pickuppoint.domain.model.Store;

/**
 * @author Aghny A. Putra on 25/01/18
 */
public class ShipmentRecipientModel implements Parcelable {

    private boolean isPrimerAddress;
    private String addressIdentifier;
    private String recipientName;
    private String recipientAddressDescription;
    private String recipientAddress;
    private String recipientPhoneNumber;
    private String destinationDistrictId;
    private String destinationDistrictName;
    private String tokenPickup;
    private String unixTime;
    private Store store;

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

    public static final Creator<ShipmentRecipientModel> CREATOR = new Creator<ShipmentRecipientModel>() {
        @Override
        public ShipmentRecipientModel createFromParcel(Parcel in) {
            return new ShipmentRecipientModel(in);
        }

        @Override
        public ShipmentRecipientModel[] newArray(int size) {
            return new ShipmentRecipientModel[size];
        }
    };

}
