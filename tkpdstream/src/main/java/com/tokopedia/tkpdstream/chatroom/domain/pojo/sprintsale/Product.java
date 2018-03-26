
package com.tokopedia.tkpdstream.chatroom.domain.pojo.sprintsale;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

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
    private String discountPercentage;
    @SerializedName("discounted_price")
    @Expose
    private String discountedPrice;
    @SerializedName("original_price")
    @Expose
    private String originalPrice;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlMobile() {
        return urlMobile;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

}
