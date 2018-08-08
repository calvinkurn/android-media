package com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 05/09/17.
 */

public class CreateSubmitResponse {
    @SerializedName("resolution")
    @Expose
    private ResolutionResponse resolution;

    @SerializedName("shop")
    @Expose
    private ShopResponse shop;

    @SerializedName("successMessage")
    @Expose
    private String successMessage;


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

    public ShopResponse getShop() {
        return shop;
    }

    public void setShop(ShopResponse shop) {
        this.shop = shop;
    }

    @Override
    public String toString() {
        return "CreateSubmitResponse{" +
                "resolution='" + resolution + '\'' +
                ", successMessage='" + successMessage + '\'' +
                '}';
    }
}
