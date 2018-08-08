package com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 05/09/17.
 */

public class CreateResoWithoutAttachmentResponse {

    @SerializedName("resolution")
    @Expose
    private ResolutionResponse resolution;

    @SerializedName("shop")
    @Expose
    private ShopResponse shop;

    @SerializedName("cacheKey")
    @Expose
    private String cacheKey;

    @SerializedName("successMessage")
    @Expose
    private String successMessage;

    public ShopResponse getShop() {
        return shop;
    }

    public void setShop(ShopResponse shop) {
        this.shop = shop;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public ResolutionResponse getResolution() {
        return resolution;
    }

    public void setResolution(ResolutionResponse resolution) {
        this.resolution = resolution;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }


    @Override
    public String toString() {
        return "CreateResoWithoutAttachmentResponse{" +
                "resolution='" + resolution + '\'' +
                ", cacheKey='" + cacheKey + '\'' +
                ", successMessage='" + successMessage + '\'' +
                '}';
    }

}
