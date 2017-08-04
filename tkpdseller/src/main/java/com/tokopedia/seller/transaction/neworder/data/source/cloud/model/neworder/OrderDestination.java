
package com.tokopedia.seller.transaction.neworder.data.source.cloud.model.neworder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDestination {

    @SerializedName("receiver_phone_is_tokopedia")
    @Expose
    private int receiverPhoneIsTokopedia;
    @SerializedName("receiver_name")
    @Expose
    private String receiverName;
    @SerializedName("address_country")
    @Expose
    private String addressCountry;
    @SerializedName("address_postal")
    @Expose
    private String addressPostal;
    @SerializedName("address_district")
    @Expose
    private String addressDistrict;
    @SerializedName("receiver_phone")
    @Expose
    private String receiverPhone;
    @SerializedName("address_street")
    @Expose
    private String addressStreet;
    @SerializedName("address_city")
    @Expose
    private String addressCity;
    @SerializedName("address_province")
    @Expose
    private String addressProvince;

    public int getReceiverPhoneIsTokopedia() {
        return receiverPhoneIsTokopedia;
    }

    public void setReceiverPhoneIsTokopedia(int receiverPhoneIsTokopedia) {
        this.receiverPhoneIsTokopedia = receiverPhoneIsTokopedia;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

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

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressProvince() {
        return addressProvince;
    }

    public void setAddressProvince(String addressProvince) {
        this.addressProvince = addressProvince;
    }

}
