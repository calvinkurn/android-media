package com.tokopedia.shop.product.view.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.shop.address.view.adapter.ShopAddressTypeFactory;
import com.tokopedia.shop.product.view.adapter.ShopProductTypeFactory;

import java.util.List;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductViewModel implements Visitable<ShopProductTypeFactory> {

    private long shopLucky;
    private long shopGoldStatus;
    private long shopId;
    private List<Badge> badges = null;
    private List<Object> labels = null;
    private String productTalkCount;
    private String productPrice;
    private long productWholesale;
    private String productImage300;
    private String productImage700;
    private String productUrl;
    private String shopUrl;
    private long productId;
    private String productImage;
    private long productPreorder;
    private String shopLocation;
    private String productReviewCount;
    private String shopName;
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

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    public List<Object> getLabels() {
        return labels;
    }

    public void setLabels(List<Object> labels) {
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

    @Override
    public int type(ShopProductTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public static class Badge {

        private String title;
        private String imageUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

    }
}
