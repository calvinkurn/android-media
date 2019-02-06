
package com.tokopedia.transaction.purchase.detail.model.detail.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Summary {

    @SerializedName("items_price")
    @Expose
    private String itemsPrice;
    @SerializedName("shipping_price")
    @Expose
    private String shippingPrice;
    @SerializedName("insurance_price")
    @Expose
    private String insurancePrice;
    @SerializedName("additional_price")
    @Expose
    private String additionalPrice;
    @SerializedName("total_item")
    @Expose
    private int totalItem;
    @SerializedName("total_weight")
    @Expose
    private String totalWeight;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("total_purchase_protection_quantity")
    @Expose
    private int totalProtectionItem;
    @SerializedName("total_purchase_protection_fee")
    @Expose
    private String totalProtectionFee;
    @SerializedName("is_order_cod")
    @Expose
    private Boolean isOrderCod;
    @SerializedName("cod_fee")
    @Expose
    private String codFee;


    public String getItemsPrice() {
        return itemsPrice;
    }

    public void setItemsPrice(String itemsPrice) {
        this.itemsPrice = itemsPrice;
    }

    public String getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(String shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public String getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(String insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public String getAdditionalPrice() {
        return additionalPrice;
    }

    public void setAdditionalPrice(String additionalPrice) {
        this.additionalPrice = additionalPrice;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public String getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalProtectionItem() {
        return totalProtectionItem;
    }

    public void setTotalProtectionItem(int totalProtectionItem) {
        this.totalProtectionItem = totalProtectionItem;
    }

    public String getTotalProtectionFee() {
        return totalProtectionFee;
    }

    public void setTotalProtectionFee(String totalProtectionFee) {
        this.totalProtectionFee = totalProtectionFee;
    }

    public Boolean getOrderCod() {
        return isOrderCod;
    }

    public void setOrderCod(Boolean orderCod) {
        isOrderCod = orderCod;
    }

    public String getCodFee() {
        return codFee;
    }

    public void setCodFee(String codFee) {
        this.codFee = codFee;
    }
}
