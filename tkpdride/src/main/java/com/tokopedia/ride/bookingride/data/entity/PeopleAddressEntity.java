package com.tokopedia.ride.bookingride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 4/6/17.
 */

public class PeopleAddressEntity {
    @SerializedName("address_id")
    @Expose
    private String addressId;
    @SerializedName("receiver_phone")
    @Expose
    private String receiverPhone;
    @SerializedName("address_status")
    @Expose
    private Integer addressStatus;
    @SerializedName("postal_code")
    @Expose
    private String postalCode;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("address_street")
    @Expose
    private String addressStreet;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("district_id")
    @Expose
    private String districtId;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("province_id")
    @Expose
    private String provinceId;
    @SerializedName("address_name")
    @Expose
    private String addressName;
    @SerializedName("receiver_name")
    @Expose
    private String receiverName;
    @SerializedName("province_name")
    @Expose
    private String provinceName;
    @SerializedName("district_name")
    @Expose
    private String districtName;

    public String getAddressId() {
        return addressId;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public Integer getAddressStatus() {
        return addressStatus;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public String getCityName() {
        return cityName;
    }

    public String getDistrictId() {
        return districtId;
    }

    public String getCityId() {
        return cityId;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public String getAddressName() {
        return addressName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public String getDistrictName() {
        return districtName;
    }
}
