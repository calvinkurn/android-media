package com.tokopedia.loyalty.domain.entity.response.promo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 03/01/18.
 */

public class Meta {

    @SerializedName("promo_code")
    @Expose
    private String promoCode;
    @SerializedName("app_link")
    @Expose
    private String appLink;
    @SerializedName("promo_link")
    @Expose
    private String promoLink;
    @SerializedName("min_transaction")
    @Expose
    private String minTransaction;
    @SerializedName("thumbnail_image")
    @Expose
    private String thumbnailImage;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;

    public String getPromoCode() {
        return promoCode;
    }

    public String getAppLink() {
        return appLink;
    }

    public String getPromoLink() {
        return promoLink;
    }

    public String getMinTransaction() {
        return minTransaction;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
