
package com.tokopedia.tkpdstream.chatroom.domain.pojo.sprintsale;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url_mobile")
    @Expose
    private String urlMobile;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("discount_percentage")
    @Expose
    private int discountPercentage;
    @SerializedName("discounted_price")
    @Expose
    private String discountedPrice;
    @SerializedName("original_price")
    @Expose
    private String originalPrice;
    @SerializedName("stock_sold_percentage")
    @Expose
    private int remainingStockPercentage;
    @SerializedName("stock_text")
    @Expose
    private String stockText;

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getUrlMobile() {
        return urlMobile;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public int getRemainingStockPercentage() {
        return remainingStockPercentage;
    }

    public String getStockText() {
        return stockText;
    }

}
