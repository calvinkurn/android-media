
package com.tokopedia.events.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressDetail {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("product_schedule_id")
    @Expose
    private Integer productScheduleId;
    @SerializedName("product_schedule_package_id")
    @Expose
    private Integer productSchedulePackageId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductScheduleId() {
        return productScheduleId;
    }

    public void setProductScheduleId(Integer productScheduleId) {
        this.productScheduleId = productScheduleId;
    }

    public Integer getProductSchedulePackageId() {
        return productSchedulePackageId;
    }

    public void setProductSchedulePackageId(Integer productSchedulePackageId) {
        this.productSchedulePackageId = productSchedulePackageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
