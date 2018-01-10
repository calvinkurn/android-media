package com.tokopedia.inbox.rescenter.createreso.domain.model.createreso;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 05/09/17.
 */

public class CreateSubmitDomain {

    @Nullable
    private ResolutionDomain resolution;

    private ShopDomain shop;

    @Nullable
    private boolean isSuccess;

    @Nullable
    private String successMessage;

    public CreateSubmitDomain(ResolutionDomain resolution, ShopDomain shop, String successMessage) {
        this.resolution = resolution;
        this.shop = shop;
        this.successMessage = successMessage;
    }

    @Nullable
    public ResolutionDomain getResolution() {
        return resolution;
    }

    public void setResolution(@Nullable ResolutionDomain resolution) {
        this.resolution = resolution;
    }

    @Nullable
    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(@Nullable boolean success) {
        isSuccess = success;
    }

    @Nullable
    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(@Nullable String successMessage) {
        this.successMessage = successMessage;
    }

    public ShopDomain getShop() {
        return shop;
    }

    public void setShop(ShopDomain shop) {
        this.shop = shop;
    }
}
