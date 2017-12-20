package com.tokopedia.seller.shop.open.view.model;

/**
 * Created by normansyahputa on 12/20/17.
 */

import android.content.Context;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.geolocation.utils.GeoLocationUtils;
import  com.tokopedia.core.manage.people.address.model.Destination;
import com.tokopedia.core.util.MethodChecker;

public class DestinationViewModel {

    public static DestinationViewModel convertFromBundle(Parcelable bundle) {
        com.tokopedia.core.manage.people.address.model.Destination address = (com.tokopedia.core.manage.people.address.model.Destination)bundle;
        DestinationViewModel destination = new DestinationViewModel();
        destination.setPostalCode(address.getPostalCode());
        destination.setAddressStreet(address.getAddressStreet());
        destination.setReceiverPhone(address.getReceiverPhone());
        destination.setReceiverName(address.getReceiverName());
        destination.setAddressId(address.getAddressId());
        destination.setAddressName(address.getAddressName());
        destination.setAddressStatus(address.getAddressStatus());
        destination.setCityId(address.getCityId());
        destination.setCityName(address.getCityName());
        destination.setCountryName(address.getCountryName());
        destination.setDistrictId(address.getDistrictId());
        destination.setDistrictName(address.getDistrictName());
        destination.setProvinceId(address.getProvinceId());
        destination.setProvinceName(address.getProvinceName());
        destination.setLatitude(address.getLatitude());
        destination.setLongitude(address.getLongitude());
        destination.setPassword(address.getPassword());
        return destination;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCityId() {
        return this.cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDistrictId() {
        return this.districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getProvinceId() {
        return this.provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getReceiverName() {
        return this.receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getAddressName() {
        return this.addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddressId() {
        return this.addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getReceiverPhone() {
        return this.receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getProvinceName() {
        return this.provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Integer getAddressStatus() {
        return this.addressStatus;
    }

    public void setAddressStatus(Integer addressStatus) {
        this.addressStatus = addressStatus;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddressStreet() {
        return this.addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getCityName() {
        return this.cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return this.districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getAddressDetail() {
        return MethodChecker.fromHtml(this.receiverName) + "<br>" + MethodChecker.fromHtml(this.addressStreet) + "<br>" + this.districtName + ", " + this.cityName + ", " + this.postalCode + "<br>" + this.provinceName + "<br>" + this.receiverPhone;
    }

    public String getGeoLocation(Context context) {
        this.geoLocation = GeoLocationUtils.reverseGeoCode(context, this.latitude, this.longitude);
        return this.geoLocation;
    }

    public boolean isCompleted() {
        return this.addressId != null && !this.addressId.equals("0") && !this.cityId.equals("0") && !this.districtId.equals("0") && !this.provinceId.equals("0");
    }

    public LatLng getLatLng() {
        return this.getLatitude() != null && this.getLongitude() != null?new LatLng(Double.parseDouble(this.getLatitude()), Double.parseDouble(this.getLongitude())):null;
    }

    public void setLatLng(LatLng latLng) {
        this.latitude = String.valueOf(latLng.latitude);
        this.longitude = String.valueOf(latLng.longitude);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    private String longitude;
    private String countryName;
    private String cityId;
    private String districtId;
    private String provinceId;
    private String receiverName;
    private String addressName;
    private String addressId;
    private String receiverPhone;
    private String provinceName;
    private Integer addressStatus;
    private String postalCode;
    private String latitude;
    private String addressStreet;
    private String cityName;
    private String districtName;
    private String geoLocation;
    private String password;

    @Override
    public String toString() {
        return "DestinationViewModel{" +
                "longitude='" + longitude + '\'' +
                ", countryName='" + countryName + '\'' +
                ", cityId='" + cityId + '\'' +
                ", districtId='" + districtId + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", addressName='" + addressName + '\'' +
                ", addressId='" + addressId + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", addressStatus=" + addressStatus +
                ", postalCode='" + postalCode + '\'' +
                ", latitude='" + latitude + '\'' +
                ", addressStreet='" + addressStreet + '\'' +
                ", cityName='" + cityName + '\'' +
                ", districtName='" + districtName + '\'' +
                ", geoLocation='" + geoLocation + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
