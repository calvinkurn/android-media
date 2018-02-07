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
        dest.writeString(recipientName);
        dest.writeString(recipientAddress);
        dest.writeString(recipientAddressDescription);
        dest.writeString(recipientPhoneNumber);
        dest.writeString(destinationDistrictId);
        dest.writeString(destinationDistrictName);
        dest.writeString(tokenPickup);
        dest.writeString(unixTime);
        dest.writeParcelable(store, flags);
        dest.writeByte(this.isPrimerAddress ? (byte) 1 : (byte) 0);
        dest.writeString(this.addressIdentifier);
        dest.writeString(this.recipientName);
        dest.writeString(this.recipientAddress);
        dest.writeString(this.recipientPhone);
    }

    protected ShipmentRecipientModel(Parcel in) {
        recipientName = in.readString();
        recipientAddress = in.readString();
        recipientAddressDescription = in.readString();
        recipientPhoneNumber = in.readString();
        destinationDistrictId = in.readString();
        destinationDistrictName = in.readString();
        tokenPickup = in.readString();
        unixTime = in.readString();
        store = in.readParcelable(Store.class.getClassLoader());
        this.isPrimerAddress = in.readByte() != 0;
        this.addressIdentifier = in.readString();
        this.recipientName = in.readString();
        this.recipientAddress = in.readString();
        this.recipientPhone = in.readString();
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
