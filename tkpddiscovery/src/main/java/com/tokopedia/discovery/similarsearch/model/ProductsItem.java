package com.tokopedia.discovery.similarsearch.model;

import com.google.android.gms.tagmanager.DataLayer;
import com.google.gson.annotations.SerializedName;
import com.tkpd.library.utils.CurrencyFormatHelper;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ProductsItem {

    @SerializedName("original_price")
    private String originalPrice = "";

    @SerializedName("shop")
    private Shop shop;

    @SerializedName("category_name")
    private String categoryName = "";

    @SerializedName("image_url")
    private String imageUrl = "";

    @SerializedName("image_url_700")
    private String imageUrl700 = "";

    @SerializedName("rating")
    private int rating;

    @SerializedName("discount_start_time")
    private String discountStartTime = "";

    @SerializedName("discount_percentage")
    private int discountPercentage;

    @SerializedName("url")
    private String url;

    @SerializedName("category_id")
    private int categoryId;

    @SerializedName("count_review")
    private int countReview;

    @SerializedName("price")
    private String price = "";

    @SerializedName("name")
    private String name = "";

    @SerializedName("id")
    private int id;

    @SerializedName("discount_expired_time")
    private String discountExpiredTime;

    @SerializedName("badges")
    private List<Badges> badges;

    @SerializedName("wishlist")
    private boolean wishListed;

    private String originProductID;
    private boolean isWishlistButtonEnabled = true;

    public boolean isWishListed() {
        return wishListed;
    }

    public boolean isWishlistButtonEnabled() {
        return isWishlistButtonEnabled;
    }

    public void setWishlistButtonEnabled(boolean wishlistButtonEnabled) {
        isWishlistButtonEnabled = wishlistButtonEnabled;
    }

    public void setWishListed(boolean wishListed) {
        this.wishListed = wishListed;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Shop getShop() {
        return shop;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
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

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setDiscountStartTime(String discountStartTime) {
        this.discountStartTime = discountStartTime;
    }

    public String getDiscountStartTime() {
        return discountStartTime;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCountReview(int countReview) {
        this.countReview = countReview;
    }

    public int getCountReview() {
        return countReview;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDiscountExpiredTime(String discountExpiredTime) {
        this.discountExpiredTime = discountExpiredTime;
    }

    public String getDiscountExpiredTime() {
        return discountExpiredTime;
    }

    public List<Badges> getBadges() {
        return badges;
    }

    public void setBadges(List<Badges> badges) {
        this.badges = badges;
    }

    public String getOriginProductID() {
        return originProductID;
    }

    public void setOriginProductID(String originProductID) {
        this.originProductID = originProductID;
    }

    @Override
    public String toString() {
        return
                "ProductsItem{" +
                        "original_price = '" + originalPrice + '\'' +
                        ",shop = '" + shop + '\'' +
                        ",category_name = '" + categoryName + '\'' +
                        ",image_url = '" + imageUrl + '\'' +
                        ",image_url_700 = '" + imageUrl700 + '\'' +
                        ",rating = '" + rating + '\'' +
                        ",discount_start_time = '" + discountStartTime + '\'' +
                        ",discount_percentage = '" + discountPercentage + '\'' +
                        ",url = '" + url + '\'' +
                        ",category_id = '" + categoryId + '\'' +
                        ",count_review = '" + countReview + '\'' +
                        ",price = '" + price + '\'' +
                        ",wishlist = '" + wishListed + '\'' +
                        ",name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",discount_expired_time = '" + discountExpiredTime + '\'' +
                        "}";
    }

    public Object getProductAsObjectDataLayer(String position) {
        String price = "";
        if (!getPrice().trim().isEmpty()) {
            price = Integer.toString(CurrencyFormatHelper.convertRupiahToInt(getPrice()));
        }
        return DataLayer.mapOf(
                "name", getName(),
                "id", getId(),
                "price", price,
                "brand", "none / other",
                "category", categoryName,
                "variant", "none / other",
                "list", "/similarproduct",
                "position", position
        );
    }

    @Override
    public boolean equals(Object obj) {
        ProductsItem productsItem = (ProductsItem) obj;
        return id == productsItem.getId();

    }
}