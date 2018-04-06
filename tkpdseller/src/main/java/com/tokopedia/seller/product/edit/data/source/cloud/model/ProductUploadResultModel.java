package com.tokopedia.seller.product.edit.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 2/6/18.
 */

public class ProductUploadResultModel {

    @SerializedName("ProductID")
    @Expose
    private int productID;
    @SerializedName("ShopID")
    @Expose
    private int shopID;
    @SerializedName("ChildCatID")
    @Expose
    private int childCatID;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("NormalPrice")
    @Expose
    private int normalPrice;
    @SerializedName("PriceCurrency")
    @Expose
    private int priceCurrency;
    @SerializedName("Status")
    @Expose
    private int status;
    @SerializedName("ShortDesc")
    @Expose
    private String shortDesc;
    @SerializedName("MinOrder")
    @Expose
    private int minOrder;
    @SerializedName("MaxOrder")
    @Expose
    private int maxOrder;
    @SerializedName("Weight")
    @Expose
    private int weight;
    @SerializedName("WeightUnit")
    @Expose
    private int weightUnit;
    @SerializedName("Condition")
    @Expose
    private int condition;
    @SerializedName("MustInsurance")
    @Expose
    private int mustInsurance;
    @SerializedName("Position")
    @Expose
    private int position;
    @SerializedName("CreateTime")
    @Expose
    private String createTime;
    @SerializedName("UpdateTime")
    @Expose
    private String updateTime;
    @SerializedName("DinkTime")
    @Expose
    private Object dinkTime;
    @SerializedName("LastUpdatePrice")
    @Expose
    private String lastUpdatePrice;
    @SerializedName("Stock")
    @Expose
    private int stock;
    @SerializedName("CreateBy")
    @Expose
    private int createBy;
    @SerializedName("UpdateBy")
    @Expose
    private int updateBy;

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public int getChildCatID() {
        return childCatID;
    }

    public void setChildCatID(int childCatID) {
        this.childCatID = childCatID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(int normalPrice) {
        this.normalPrice = normalPrice;
    }

    public int getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(int priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public int getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(int minOrder) {
        this.minOrder = minOrder;
    }

    public int getMaxOrder() {
        return maxOrder;
    }

    public void setMaxOrder(int maxOrder) {
        this.maxOrder = maxOrder;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(int weightUnit) {
        this.weightUnit = weightUnit;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public int getMustInsurance() {
        return mustInsurance;
    }

    public void setMustInsurance(int mustInsurance) {
        this.mustInsurance = mustInsurance;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Object getDinkTime() {
        return dinkTime;
    }

    public void setDinkTime(Object dinkTime) {
        this.dinkTime = dinkTime;
    }

    public String getLastUpdatePrice() {
        return lastUpdatePrice;
    }

    public void setLastUpdatePrice(String lastUpdatePrice) {
        this.lastUpdatePrice = lastUpdatePrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getCreateBy() {
        return createBy;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }

    public int getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(int updateBy) {
        this.updateBy = updateBy;
    }

}