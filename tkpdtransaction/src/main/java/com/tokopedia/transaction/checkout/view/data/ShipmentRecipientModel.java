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

    public boolean isPrimerAddress() {
        return isPrimerAddress;
    }

    public void setPrimerAddress(boolean primerAddress) {
        isPrimerAddress = primerAddress;
    }

    public String getAddressIdentifier() {
        return addressIdentifier;
    }

    public void setAddressIdentifier(String addressIdentifier) {
        this.addressIdentifier = addressIdentifier;
    }

    public String getRecipientAddressDescription() {
        return recipientAddressDescription;
    }

    public void setRecipientAddressDescription(String recipientAddressDescription) {
        this.recipientAddressDescription = recipientAddressDescription;
    }

    public String getRecipientPhoneNumber() {
        return recipientPhoneNumber;
    }

    public void setRecipientPhoneNumber(String recipientPhoneNumber) {
        this.recipientPhoneNumber = recipientPhoneNumber;
    }

    public String getDestinationDistrictId() {
        return destinationDistrictId;
    }

    public void setDestinationDistrictId(String destinationDistrictId) {
        this.destinationDistrictId = destinationDistrictId;
    }

    public String getDestinationDistrictName() {
        return destinationDistrictName;
    }

    public void setDestinationDistrictName(String destinationDistrictName) {
        this.destinationDistrictName = destinationDistrictName;
    }

    public String getTokenPickup() {
        return tokenPickup;
    }

    public void setTokenPickup(String tokenPickup) {
        this.tokenPickup = tokenPickup;
    }

    public String getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(String unixTime) {
        this.unixTime = unixTime;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public ShipmentRecipientModel() {
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
        dest.writeString(this.recipientAddressDescription);
        dest.writeString(this.recipientAddress);
        dest.writeString(this.recipientPhoneNumber);
        dest.writeString(this.destinationDistrictId);
        dest.writeString(this.destinationDistrictName);
        dest.writeString(this.tokenPickup);
        dest.writeString(this.unixTime);
        dest.writeParcelable(this.store, flags);
    }

    protected ShipmentRecipientModel(Parcel in) {
        this.isPrimerAddress = in.readByte() != 0;
        this.addressIdentifier = in.readString();
        this.recipientName = in.readString();
        this.recipientAddressDescription = in.readString();
        this.recipientAddress = in.readString();
        this.recipientPhoneNumber = in.readString();
        this.destinationDistrictId = in.readString();
        this.destinationDistrictName = in.readString();
        this.tokenPickup = in.readString();
        this.unixTime = in.readString();
        this.store = in.readParcelable(Store.class.getClassLoader());
    }

    public static final Creator<ShipmentRecipientModel> CREATOR = new Creator<ShipmentRecipientModel>() {
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
