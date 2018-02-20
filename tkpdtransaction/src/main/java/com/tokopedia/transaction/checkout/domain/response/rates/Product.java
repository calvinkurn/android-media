package com.tokopedia.transaction.checkout.domain.response.rates;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 20/02/18.
 */

public class Product {

    @SerializedName("service_id")
    @Expose
    private int serviceId;
    @SerializedName("service_desc")
    @Expose
    private String serviceDesc;
    @SerializedName("shipper_name")
    @Expose
    private String shipperName;
    @SerializedName("shipper_id")
    @Expose
    private int shipperId;
    @SerializedName("shipper_product_id")
    @Expose
    private int shipperProductId;
    @SerializedName("shipper_product_name")
    @Expose
    private String shipperProductName;
    @SerializedName("shipper_product_desc")
    @Expose
    private String shipperProductDesc;
    @SerializedName("is_show_map")
    @Expose
    private int isShowMap;
    @SerializedName("price")
    @Expose
    private int price;
    @SerializedName("formatted_price")
    @Expose
    private String formattedPrice;
    @SerializedName("etd")
    @Expose
    private String etd;
    @SerializedName("min_etd")
    @Expose
    private int minEtd;
    @SerializedName("max_etd")
    @Expose
    private int maxEtd;
    @SerializedName("check_sum")
    @Expose
    private String checkSum;
    @SerializedName("ut")
    @Expose
    private String ut;
    @SerializedName("max_hours_id")
    @Expose
    private String maxHoursId;
    @SerializedName("desc_hours_id")
    @Expose
    private String descHoursId;
    @SerializedName("max_hours")
    @Expose
    private String masHours;
    @SerializedName("desc_hours")
    @Expose
    private String descHours;
    @SerializedName("insurance_price")
    @Expose
    private int insurancePrice;
    @SerializedName("insurance_type")
    @Expose
    private int insuranceType;
    @SerializedName("insurance_type_info")
    @Expose
    private String insuranceTypeInfo;
    @SerializedName("weight_product")
    @Expose
    private int weightProduct;
    @SerializedName("weight_order_spid")
    @Expose
    private int weightOrderSpid;
    @SerializedName("insurance_used_type")
    @Expose
    private int insuranceUsedType;
    @SerializedName("insurance_used_default")
    @Expose
    private int insuranceUsedDefault;
    @SerializedName("insurance_used_info")
    @Expose
    private String insuranceUsedInfo;

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    public int getShipperProductId() {
        return shipperProductId;
    }

    public void setShipperProductId(int shipperProductId) {
        this.shipperProductId = shipperProductId;
    }

    public String getShipperProductName() {
        return shipperProductName;
    }

    public void setShipperProductName(String shipperProductName) {
        this.shipperProductName = shipperProductName;
    }

    public String getShipperProductDesc() {
        return shipperProductDesc;
    }

    public void setShipperProductDesc(String shipperProductDesc) {
        this.shipperProductDesc = shipperProductDesc;
    }

    public int getIsShowMap() {
        return isShowMap;
    }

    public void setIsShowMap(int isShowMap) {
        this.isShowMap = isShowMap;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
    }

    public int getMinEtd() {
        return minEtd;
    }

    public void setMinEtd(int minEtd) {
        this.minEtd = minEtd;
    }

    public int getMaxEtd() {
        return maxEtd;
    }

    public void setMaxEtd(int maxEtd) {
        this.maxEtd = maxEtd;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public String getUt() {
        return ut;
    }

    public void setUt(String ut) {
        this.ut = ut;
    }

    public String getMaxHoursId() {
        return maxHoursId;
    }

    public void setMaxHoursId(String maxHoursId) {
        this.maxHoursId = maxHoursId;
    }

    public String getDescHoursId() {
        return descHoursId;
    }

    public void setDescHoursId(String descHoursId) {
        this.descHoursId = descHoursId;
    }

    public String getMasHours() {
        return masHours;
    }

    public void setMasHours(String masHours) {
        this.masHours = masHours;
    }

    public String getDescHours() {
        return descHours;
    }

    public void setDescHours(String descHours) {
        this.descHours = descHours;
    }

    public int getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(int insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public int getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(int insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getInsuranceTypeInfo() {
        return insuranceTypeInfo;
    }

    public void setInsuranceTypeInfo(String insuranceTypeInfo) {
        this.insuranceTypeInfo = insuranceTypeInfo;
    }

    public int getWeightProduct() {
        return weightProduct;
    }

    public void setWeightProduct(int weightProduct) {
        this.weightProduct = weightProduct;
    }

    public int getWeightOrderSpid() {
        return weightOrderSpid;
    }

    public void setWeightOrderSpid(int weightOrderSpid) {
        this.weightOrderSpid = weightOrderSpid;
    }

    public int getInsuranceUsedType() {
        return insuranceUsedType;
    }

    public void setInsuranceUsedType(int insuranceUsedType) {
        this.insuranceUsedType = insuranceUsedType;
    }

    public int getInsuranceUsedDefault() {
        return insuranceUsedDefault;
    }

    public void setInsuranceUsedDefault(int insuranceUsedDefault) {
        this.insuranceUsedDefault = insuranceUsedDefault;
    }

    public String getInsuranceUsedInfo() {
        return insuranceUsedInfo;
    }

    public void setInsuranceUsedInfo(String insuranceUsedInfo) {
        this.insuranceUsedInfo = insuranceUsedInfo;
    }
}
