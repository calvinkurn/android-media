package com.tokopedia.discovery.newdiscovery.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hangnadi on 10/3/17.
 */

public class ProductModel {

    private String productID;
    private String productName;
    private String productUrl;
    private String imageUrl;
    private String imageUrl700;
    private int rating;
    private int countReview;
    private int countCourier;
    private String price;
    private String priceRange;
    private String shopID;
    private String shopName;
    private String shopCity;
    private boolean isGoldMerchant;
    private boolean isWishlisted;
    private List<BadgeModel> badgesList;
    private List<LabelModel> labelList;
    private boolean featured;
    private String originalPrice;
    private int discountPercentage;
    private boolean isOfficial;
    private String topLabel;
    private String bottomLabel;
    private int categoryId;
    private String categoryName;
    private String categoryBreadcrumb;

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl700(String imageUrl700) {
        this.imageUrl700 = imageUrl700;
    }

    public String getImageUrl700() {
        return imageUrl700;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopCity(String shopCity) {
        this.shopCity = shopCity;
    }

    public String getShopCity() {
        return shopCity;
    }

    public boolean isGoldMerchant() {
        return isGoldMerchant;
    }

    public void setGoldMerchant(boolean isGoldMerchant) {
        this.isGoldMerchant = isGoldMerchant;
    }

    public boolean isWishlisted() {
        return isWishlisted;
    }

    public void setWishlisted(boolean wishlisted) {
        isWishlisted = wishlisted;
    }

    public void setBadgesList(List<BadgeModel> badgesList) {
        this.badgesList = badgesList;
    }

    public List<BadgeModel> getBadgesList() {
        return badgesList;
    }

    public void setLabelList(List<LabelModel> labelList) {
        this.labelList = labelList;
    }

    public List<LabelModel> getLabelList() {
        return labelList;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public boolean isFeatured() {
        return featured;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getCountReview() {
        return countReview;
    }

    public void setCountReview(int countReview) {
        this.countReview = countReview;
    }

    public int getCountCourier() {
        return countCourier;
    }

    public void setCountCourier(int countCourier) {
        this.countCourier = countCourier;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setOfficial(boolean official) {
        isOfficial = official;
    }

    public String getTopLabel() {
        return topLabel;
    }

    public void setTopLabel(String topLabel) {
        this.topLabel = topLabel;
    }

    public String getBottomLabel() {
        return bottomLabel;
    }

    public void setBottomLabel(String bottomLabel) {
        this.bottomLabel = bottomLabel;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryBreadcrumb() {
        return categoryBreadcrumb;
    }

    public void setCategoryBreadcrumb(String categoryBreadcrumb) {
        this.categoryBreadcrumb = categoryBreadcrumb;
    }
}
