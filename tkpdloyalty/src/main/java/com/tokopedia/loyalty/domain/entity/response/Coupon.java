package com.tokopedia.loyalty.domain.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class Coupon {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("promo_id")
    @Expose
    private int promoId;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("expired")
    @Expose
    private String expired;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("sub_title")
    @Expose
    private String subTitle;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("image_url_mobile")
    @Expose
    private String imageUrlMobile;

    public int getId() {
        return id;
    }

    public int getPromoId() {
        return promoId;
    }

    public String getCode() {
        return code;
    }

    public String getExpired() {
        return expired;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageUrlMobile() {
        return imageUrlMobile;
    }
}
