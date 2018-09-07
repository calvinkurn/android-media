package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationAddressDomain implements Parcelable {

    public static final Parcelable.Creator<ConversationAddressDomain> CREATOR = new Parcelable.Creator<ConversationAddressDomain>() {
        @Override
        public ConversationAddressDomain createFromParcel(Parcel source) {
            return new ConversationAddressDomain(source);
        }

        @Override
        public ConversationAddressDomain[] newArray(int size) {
            return new ConversationAddressDomain[size];
        }
    };
    private int addressId;
    private String address;
    private String district;
    private String city;
    private String province;
    private String phone;
    private String country;
    private String postalCode;
    private String receiver;

    public ConversationAddressDomain(int addressId,
                                     String address,
                                     String district,
                                     String city,
                                     String province,
                                     String phone,
                                     String country,
                                     String postalCode,
                                     String receiver) {
        this.addressId = addressId;
        this.address = address;
        this.district = district;
        this.city = city;
        this.province = province;
        this.phone = phone;
        this.country = country;
        this.postalCode = postalCode;
        this.receiver = receiver;
    }

    protected ConversationAddressDomain(Parcel in) {
        this.addressId = in.readInt();
        this.address = in.readString();
        this.district = in.readString();
        this.city = in.readString();
        this.province = in.readString();
        this.phone = in.readString();
        this.country = in.readString();
        this.postalCode = in.readString();
        this.receiver = in.readString();
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.addressId);
        dest.writeString(this.address);
        dest.writeString(this.district);
        dest.writeString(this.city);
        dest.writeString(this.province);
        dest.writeString(this.phone);
        dest.writeString(this.country);
        dest.writeString(this.postalCode);
        dest.writeString(this.receiver);
    }
}
