
package com.tokopedia.shop.product.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopProduct {

    @SerializedName("shop_lucky")
    @Expose
    private long shopLucky;
    @SerializedName("shop_gold_status")
    @Expose
    private long shopGoldStatus;
    @SerializedName("shop_id")
    @Expose
    private long shopId;
    @SerializedName("badges")
    @Expose
    private java.util.List<Badge> badges = null;
    @SerializedName("labels")
    @Expose
    private java.util.List<Object> labels = null;
    @SerializedName("product_talk_count")
    @Expose
    private String productTalkCount;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_wholesale")
    @Expose
    private long productWholesale;
    @SerializedName("product_image_300")
    @Expose
    private String productImage300;
    @SerializedName("product_image_700")
    @Expose
    private String productImage700;
    @SerializedName("product_url")
    @Expose
    private String productUrl;
    @SerializedName("shop_url")
    @Expose
    private String shopUrl;
    @SerializedName("product_id")
    @Expose
    private long productId;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("product_preorder")
    @Expose
    private long productPreorder;
    @SerializedName("shop_location")
    @Expose
    private String shopLocation;
    @SerializedName("product_review_count")
    @Expose
    private String productReviewCount;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("product_name")
    @Expose
    private String productName;

    public long getShopLucky() {
        return shopLucky;
    }

    public void setShopLucky(long shopLucky) {
        this.shopLucky = shopLucky;
    }

    public long getShopGoldStatus() {
        return shopGoldStatus;
    }

    public void setShopGoldStatus(long shopGoldStatus) {
        this.shopGoldStatus = shopGoldStatus;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public java.util.List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(java.util.List<Badge> badges) {
        this.badges = badges;
    }

    public java.util.List<Object> getLabels() {
        return labels;
    }

    public void setLabels(java.util.List<Object> labels) {
        this.labels = labels;
    }

    public String getProductTalkCount() {
        return productTalkCount;
    }

    public void setProductTalkCount(String productTalkCount) {
        this.productTalkCount = productTalkCount;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public long getProductWholesale() {
        return productWholesale;
    }

    public void setProductWholesale(long productWholesale) {
        this.productWholesale = productWholesale;
    }

    public String getProductImage300() {
        return productImage300;
    }

    public void setProductImage300(String productImage300) {
        this.productImage300 = productImage300;
    }

    public String getProductImage700() {
        return productImage700;
    }

    public void setProductImage700(String productImage700) {
        this.productImage700 = productImage700;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public long getProductPreorder() {
        return productPreorder;
    }

    public void setProductPreorder(long productPreorder) {
        this.productPreorder = productPreorder;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public String getProductReviewCount() {
        return productReviewCount;
    }

    public void setProductReviewCount(String productReviewCount) {
        this.productReviewCount = productReviewCount;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}
