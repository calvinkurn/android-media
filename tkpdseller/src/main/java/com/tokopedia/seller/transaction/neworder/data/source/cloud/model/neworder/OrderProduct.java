
package com.tokopedia.seller.transaction.neworder.data.source.cloud.model.neworder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderProduct {

    @SerializedName("order_deliver_quantity")
    @Expose
    private int orderDeliverQuantity;
    @SerializedName("product_weight_unit")
    @Expose
    private int productWeightUnit;
    @SerializedName("order_detail_id")
    @Expose
    private int orderDetailId;
    @SerializedName("product_status")
    @Expose
    private String productStatus;
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("product_current_weight")
    @Expose
    private String productCurrentWeight;
    @SerializedName("product_picture")
    @Expose
    private String productPicture;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_description")
    @Expose
    private String productDescription;
    @SerializedName("product_normal_price")
    @Expose
    private String productNormalPrice;
    @SerializedName("product_price_currency")
    @Expose
    private int productPriceCurrency;
    @SerializedName("product_notes")
    @Expose
    private String productNotes;
    @SerializedName("order_subtotal_price")
    @Expose
    private String orderSubtotalPrice;
    @SerializedName("product_quantity")
    @Expose
    private int productQuantity;
    @SerializedName("product_weight")
    @Expose
    private String productWeight;
    @SerializedName("order_subtotal_price_idr")
    @Expose
    private String orderSubtotalPriceIdr;
    @SerializedName("product_reject_quantity")
    @Expose
    private int productRejectQuantity;
    @SerializedName("product_url")
    @Expose
    private String productUrl;
    @SerializedName("product_name")
    @Expose
    private String productName;

    public int getOrderDeliverQuantity() {
        return orderDeliverQuantity;
    }

    public void setOrderDeliverQuantity(int orderDeliverQuantity) {
        this.orderDeliverQuantity = orderDeliverQuantity;
    }

    public int getProductWeightUnit() {
        return productWeightUnit;
    }

    public void setProductWeightUnit(int productWeightUnit) {
        this.productWeightUnit = productWeightUnit;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductCurrentWeight() {
        return productCurrentWeight;
    }

    public void setProductCurrentWeight(String productCurrentWeight) {
        this.productCurrentWeight = productCurrentWeight;
    }

    public String getProductPicture() {
        return productPicture;
    }

    public void setProductPicture(String productPicture) {
        this.productPicture = productPicture;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductNormalPrice() {
        return productNormalPrice;
    }

    public void setProductNormalPrice(String productNormalPrice) {
        this.productNormalPrice = productNormalPrice;
    }

    public int getProductPriceCurrency() {
        return productPriceCurrency;
    }

    public void setProductPriceCurrency(int productPriceCurrency) {
        this.productPriceCurrency = productPriceCurrency;
    }

    public String getProductNotes() {
        return productNotes;
    }

    public void setProductNotes(String productNotes) {
        this.productNotes = productNotes;
    }

    public String getOrderSubtotalPrice() {
        return orderSubtotalPrice;
    }

    public void setOrderSubtotalPrice(String orderSubtotalPrice) {
        this.orderSubtotalPrice = orderSubtotalPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getOrderSubtotalPriceIdr() {
        return orderSubtotalPriceIdr;
    }

    public void setOrderSubtotalPriceIdr(String orderSubtotalPriceIdr) {
        this.orderSubtotalPriceIdr = orderSubtotalPriceIdr;
    }

    public int getProductRejectQuantity() {
        return productRejectQuantity;
    }

    public void setProductRejectQuantity(int productRejectQuantity) {
        this.productRejectQuantity = productRejectQuantity;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}
