
package com.tokopedia.shop.common.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopInfoAddress {

    @SerializedName("location_address")
    @Expose
    private String locationAddress;
    @SerializedName("location_address_id")
    @Expose
    private String locationAddressId;
    @SerializedName("location_address_name")
    @Expose
    private String locationAddressName;
    @SerializedName("location_area")
    @Expose
    private String locationArea;
    @SerializedName("location_city_id")
    @Expose
    private String locationCityId;
    @SerializedName("location_city_name")
    @Expose
    private String locationCityName;
    @SerializedName("location_district_id")
    @Expose
    private String locationDistrictId;
    @SerializedName("location_district_name")
    @Expose
    private String locationDistrictName;
    @SerializedName("location_email")
    @Expose
    private String locationEmail;
    @SerializedName("location_fax")
    @Expose
    private String locationFax;
    @SerializedName("location_phone")
    @Expose
    private String locationPhone;
    @SerializedName("location_postal_code")
    @Expose
    private String locationPostalCode;
    @SerializedName("location_province_id")
    @Expose
    private String locationProvinceId;
    @SerializedName("location_province_name")
    @Expose
    private String locationProvinceName;

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public String getLocationAddressId() {
        return locationAddressId;
    }

    public void setLocationAddressId(String locationAddressId) {
        this.locationAddressId = locationAddressId;
    }

    public String getLocationAddressName() {
        return locationAddressName;
    }

    public void setLocationAddressName(String locationAddressName) {
        this.locationAddressName = locationAddressName;
    }

    public String getLocationArea() {
        return locationArea;
    }

    public void setLocationArea(String locationArea) {
        this.locationArea = locationArea;
    }

    public String getLocationCityId() {
        return locationCityId;
    }

    public void setLocationCityId(String locationCityId) {
        this.locationCityId = locationCityId;
    }

    public String getLocationCityName() {
        return locationCityName;
    }

    public void setLocationCityName(String locationCityName) {
        this.locationCityName = locationCityName;
    }

    public String getLocationDistrictId() {
        return locationDistrictId;
    }

    public void setLocationDistrictId(String locationDistrictId) {
        this.locationDistrictId = locationDistrictId;
    }

    public String getLocationDistrictName() {
        return locationDistrictName;
    }

    public void setLocationDistrictName(String locationDistrictName) {
        this.locationDistrictName = locationDistrictName;
    }

    public String getLocationEmail() {
        return locationEmail;
    }

    public void setLocationEmail(String locationEmail) {
        this.locationEmail = locationEmail;
    }

    public String getLocationFax() {
        return locationFax;
    }

    public void setLocationFax(String locationFax) {
        this.locationFax = locationFax;
    }

    public String getLocationPhone() {
        return locationPhone;
    }

    public void setLocationPhone(String locationPhone) {
        this.locationPhone = locationPhone;
    }

    public String getLocationPostalCode() {
        return locationPostalCode;
    }

    public void setLocationPostalCode(String locationPostalCode) {
        this.locationPostalCode = locationPostalCode;
    }

    public String getLocationProvinceId() {
        return locationProvinceId;
    }

    public void setLocationProvinceId(String locationProvinceId) {
        this.locationProvinceId = locationProvinceId;
    }

    public String getLocationProvinceName() {
        return locationProvinceName;
    }

    public void setLocationProvinceName(String locationProvinceName) {
        this.locationProvinceName = locationProvinceName;
    }

}
