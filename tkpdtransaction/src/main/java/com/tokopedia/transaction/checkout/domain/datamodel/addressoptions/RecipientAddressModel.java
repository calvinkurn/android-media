package com.tokopedia.transaction.checkout.domain.datamodel.addressoptions;

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
    private Double latitude;
    private Double longitude;

    // For PickupPoint Alfamart
    private String tokenPickup;
    private String unixTime;
    private Store store;

    private boolean selected;

    public RecipientAddressModel() {
    }

    protected RecipientAddressModel(Parcel in) {
        id = in.readString();
        addressStatus = in.readInt();
        addressName = in.readString();
        addressProvinceName = in.readString();
        addressPostalCode = in.readString();
        addressCityName = in.readString();
        addressStreet = in.readString();
        addressCountryName = in.readString();
        recipientName = in.readString();
        recipientPhoneNumber = in.readString();
        destinationDistrictId = in.readString();
        destinationDistrictName = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        tokenPickup = in.readString();
        unixTime = in.readString();
        store = in.readParcelable(Store.class.getClassLoader());
        selected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(addressStatus);
        dest.writeString(addressName);
        dest.writeString(addressProvinceName);
        dest.writeString(addressPostalCode);
        dest.writeString(addressCityName);
        dest.writeString(addressStreet);
        dest.writeString(addressCountryName);
        dest.writeString(recipientName);
        dest.writeString(recipientPhoneNumber);
        dest.writeString(destinationDistrictId);
        dest.writeString(destinationDistrictName);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        dest.writeString(tokenPickup);
        dest.writeString(unixTime);
        dest.writeParcelable(store, flags);
        dest.writeByte((byte) (selected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecipientAddressModel> CREATOR = new Creator<RecipientAddressModel>() {
        @Override
        public RecipientAddressModel createFromParcel(Parcel in) {
            return new RecipientAddressModel(in);
        }

        @Override
        public RecipientAddressModel[] newArray(int size) {
            return new RecipientAddressModel[size];
        }
    };

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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipientAddressModel)) return false;

        RecipientAddressModel that = (RecipientAddressModel) o;

        if (getAddressStatus() != that.getAddressStatus()) return false;
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getAddressName() != null ? !getAddressName().equals(that.getAddressName()) : that.getAddressName() != null)
            return false;
        if (getAddressProvinceName() != null ? !getAddressProvinceName().equals(that.getAddressProvinceName()) : that.getAddressProvinceName() != null)
            return false;
        if (getAddressPostalCode() != null ? !getAddressPostalCode().equals(that.getAddressPostalCode()) : that.getAddressPostalCode() != null)
            return false;
        if (getAddressCityName() != null ? !getAddressCityName().equals(that.getAddressCityName()) : that.getAddressCityName() != null)
            return false;
        if (getAddressStreet() != null ? !getAddressStreet().equals(that.getAddressStreet()) : that.getAddressStreet() != null)
            return false;
        if (getAddressCountryName() != null ? !getAddressCountryName().equals(that.getAddressCountryName()) : that.getAddressCountryName() != null)
            return false;
        if (getRecipientName() != null ? !getRecipientName().equals(that.getRecipientName()) : that.getRecipientName() != null)
            return false;
        if (getRecipientPhoneNumber() != null ? !getRecipientPhoneNumber().equals(that.getRecipientPhoneNumber()) : that.getRecipientPhoneNumber() != null)
            return false;
        if (getDestinationDistrictId() != null ? !getDestinationDistrictId().equals(that.getDestinationDistrictId()) : that.getDestinationDistrictId() != null)
            return false;
        return getDestinationDistrictName() != null ? getDestinationDistrictName().equals(that.getDestinationDistrictName()) : that.getDestinationDistrictName() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + getAddressStatus();
        result = 31 * result + (getAddressName() != null ? getAddressName().hashCode() : 0);
        result = 31 * result + (getAddressProvinceName() != null ? getAddressProvinceName().hashCode() : 0);
        result = 31 * result + (getAddressPostalCode() != null ? getAddressPostalCode().hashCode() : 0);
        result = 31 * result + (getAddressCityName() != null ? getAddressCityName().hashCode() : 0);
        result = 31 * result + (getAddressStreet() != null ? getAddressStreet().hashCode() : 0);
        result = 31 * result + (getAddressCountryName() != null ? getAddressCountryName().hashCode() : 0);
        result = 31 * result + (getRecipientName() != null ? getRecipientName().hashCode() : 0);
        result = 31 * result + (getRecipientPhoneNumber() != null ? getRecipientPhoneNumber().hashCode() : 0);
        result = 31 * result + (getDestinationDistrictId() != null ? getDestinationDistrictId().hashCode() : 0);
        result = 31 * result + (getDestinationDistrictName() != null ? getDestinationDistrictName().hashCode() : 0);
        return result;
    }

}
