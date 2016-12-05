package com.tokopedia.transaction.cart.model.cartdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartDestination implements Parcelable {

    @SerializedName("address_postal")
    @Expose
    private String addressPostal;
    @SerializedName("address_district")
    @Expose
    private String addressDistrict;
    @SerializedName("address_id")
    @Expose
    private String addressId;
    @SerializedName("address_name")
    @Expose
    private String addressName;
    @SerializedName("address_province")
    @Expose
    private String addressProvince;
    @SerializedName("address_country")
    @Expose
    private String addressCountry;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("address_city")
    @Expose
    private String addressCity;
    @SerializedName("receiver_phone")
    @Expose
    private String receiverPhone;
    @SerializedName("address_street")
    @Expose
    private String addressStreet;
    @SerializedName("receiver_name")
    @Expose
    private String receiverName;

    public String getAddressPostal() {
        return addressPostal;
    }

    public void setAddressPostal(String addressPostal) {
        this.addressPostal = addressPostal;
    }

    public String getAddressDistrict() {
        return addressDistrict;
    }

    public void setAddressDistrict(String addressDistrict) {
        this.addressDistrict = addressDistrict;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddressProvince() {
        return addressProvince;
    }

    public void setAddressProvince(String addressProvince) {
        this.addressProvince = addressProvince;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.addressPostal);
        dest.writeString(this.addressDistrict);
        dest.writeString(this.addressId);
        dest.writeString(this.addressName);
        dest.writeString(this.addressProvince);
        dest.writeString(this.addressCountry);
        dest.writeString(this.longitude);
        dest.writeString(this.latitude);
        dest.writeString(this.addressCity);
        dest.writeString(this.receiverPhone);
        dest.writeString(this.addressStreet);
        dest.writeString(this.receiverName);
    }

    public CartDestination() {
    }

    protected CartDestination(Parcel in) {
        this.addressPostal = in.readString();
        this.addressDistrict = in.readString();
        this.addressId = in.readString();
        this.addressName = in.readString();
        this.addressProvince = in.readString();
        this.addressCountry = in.readString();
        this.longitude = in.readString();
        this.latitude = in.readString();
        this.addressCity = in.readString();
        this.receiverPhone = in.readString();
        this.addressStreet = in.readString();
        this.receiverName = in.readString();
    }

    public static final Creator<CartDestination> CREATOR = new Creator<CartDestination>() {
        @Override
        public CartDestination createFromParcel(Parcel source) {
            return new CartDestination(source);
        }

        @Override
        public CartDestination[] newArray(int size) {
            return new CartDestination[size];
        }
    };
}
