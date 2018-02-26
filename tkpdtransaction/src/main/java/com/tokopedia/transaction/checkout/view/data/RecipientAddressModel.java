package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.transaction.pickuppoint.domain.model.Store;

/**
 * @author Aghny A. Putra on 25/01/18
 */

public class RecipientAddressModel implements Parcelable {

    private String id;
    private int addressStatus;
    private String addressName;
    private String addressProvinceName;
    private String addressPostalCode;
    private String addressCityName;
    private String addressStreet;
    private String addressCountryName;
    private String recipientName;
    private String recipientPhoneNumber;
    private String destinationDistrictId;
    private String destinationDistrictName;

    private String tokenPickup;
    private String unixTime;
    private Store store;

    private boolean selected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAddressStatus() {
        return addressStatus;
    }

    public void setAddressStatus(int addressStatus) {
        this.addressStatus = addressStatus;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddressProvinceName() {
        return addressProvinceName;
    }

    public void setAddressProvinceName(String addressProvinceName) {
        this.addressProvinceName = addressProvinceName;
    }

    public String getAddressPostalCode() {
        return addressPostalCode;
    }

    public void setAddressPostalCode(String addressPostalCode) {
        this.addressPostalCode = addressPostalCode;
    }

    public String getAddressCityName() {
        return addressCityName;
    }

    public void setAddressCityName(String addressCityName) {
        this.addressCityName = addressCityName;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getAddressCountryName() {
        return addressCountryName;
    }

    public void setAddressCountryName(String addressCountryName) {
        this.addressCountryName = addressCountryName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.addressStatus);
        dest.writeString(this.addressName);
        dest.writeString(this.addressProvinceName);
        dest.writeString(this.addressPostalCode);
        dest.writeString(this.addressCityName);
        dest.writeString(this.addressStreet);
        dest.writeString(this.addressCountryName);
        dest.writeString(this.recipientName);
        dest.writeString(this.recipientPhoneNumber);
        dest.writeString(this.destinationDistrictId);
        dest.writeString(this.destinationDistrictName);
        dest.writeString(this.tokenPickup);
        dest.writeString(this.unixTime);
        dest.writeParcelable(this.store, flags);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    public RecipientAddressModel() {
    }

    protected RecipientAddressModel(Parcel in) {
        this.id = in.readString();
        this.addressStatus = in.readInt();
        this.addressName = in.readString();
        this.addressProvinceName = in.readString();
        this.addressPostalCode = in.readString();
        this.addressCityName = in.readString();
        this.addressStreet = in.readString();
        this.addressCountryName = in.readString();
        this.recipientName = in.readString();
        this.recipientPhoneNumber = in.readString();
        this.destinationDistrictId = in.readString();
        this.destinationDistrictName = in.readString();
        this.tokenPickup = in.readString();
        this.unixTime = in.readString();
        this.store = in.readParcelable(Store.class.getClassLoader());
        this.selected = in.readByte() != 0;
    }

    public static final Creator<RecipientAddressModel> CREATOR = new Creator<RecipientAddressModel>() {
        @Override
        public RecipientAddressModel createFromParcel(Parcel source) {
            return new RecipientAddressModel(source);
        }

        @Override
        public RecipientAddressModel[] newArray(int size) {
            return new RecipientAddressModel[size];
        }
    };
}
