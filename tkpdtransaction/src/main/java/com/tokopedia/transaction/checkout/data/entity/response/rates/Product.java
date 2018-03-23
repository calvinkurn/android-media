package com.tokopedia.transaction.checkout.data.entity.response.rates;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 20/02/18.
 */

public class Product {

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
    @SerializedName("shipper_price")
    @Expose
    private int shipperPrice;
    @SerializedName("shipper_formatted_price")
    @Expose
    private String shipperFormattedPrice;
    @SerializedName("shipper_etd")
    @Expose
    private String shipperEtd;
    @SerializedName("min_etd")
    @Expose
    private int minEtd;
    @SerializedName("max_etd")
    @Expose
    private int maxEtd;
    @SerializedName("shipper_weight")
    @Expose
    private int shipperWeight;
    @SerializedName("check_sum")
    @Expose
    private String checkSum;
    @SerializedName("ut")
    @Expose
    private String ut;
    @SerializedName("insurance_price")
    @Expose
    private int insurancePrice;
    @SerializedName("insurance_type")
    @Expose
    private int insuranceType;
    @SerializedName("insurance_type_info")
    @Expose
    private String insuranceTypeInfo;
    @SerializedName("insurance_used_type")
    @Expose
    private int insuranceUsedType;
    @SerializedName("insurance_used_info")
    @Expose
    private String insuranceUsedInfo;
    @SerializedName("insurance_used_default")
    @Expose
    private int insuranceUsedDefault;

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

    public int getShipperPrice() {
        return shipperPrice;
    }

    public void setShipperPrice(int shipperPrice) {
        this.shipperPrice = shipperPrice;
    }

    public String getShipperFormattedPrice() {
        return shipperFormattedPrice;
    }

    public void setShipperFormattedPrice(String shipperFormattedPrice) {
        this.shipperFormattedPrice = shipperFormattedPrice;
    }

    public String getShipperEtd() {
        return shipperEtd;
    }

    public void setShipperEtd(String shipperEtd) {
        this.shipperEtd = shipperEtd;
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

    public int getShipperWeight() {
        return shipperWeight;
    }

    public void setShipperWeight(int shipperWeight) {
        this.shipperWeight = shipperWeight;
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

    public int getInsuranceUsedType() {
        return insuranceUsedType;
    }

    public void setInsuranceUsedType(int insuranceUsedType) {
        this.insuranceUsedType = insuranceUsedType;
    }

    public String getInsuranceUsedInfo() {
        return insuranceUsedInfo;
    }

    public void setInsuranceUsedInfo(String insuranceUsedInfo) {
        this.insuranceUsedInfo = insuranceUsedInfo;
    }

    public int getInsuranceUsedDefault() {
        return insuranceUsedDefault;
    }

    public void setInsuranceUsedDefault(int insuranceUsedDefault) {
        this.insuranceUsedDefault = insuranceUsedDefault;
    }

}
