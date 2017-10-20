package com.tokopedia.inbox.rescenter.createreso.domain.model.createreso;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 05/09/17.
 */

public class CreateValidateDomain {

    @Nullable
    private String cacheKey;

    @Nullable
    private boolean isSuccess;

    @Nullable
    private String successMessage;

    public CreateValidateDomain(@Nullable String cacheKey) {
        this.cacheKey = cacheKey;
    }

    @Nullable
    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(@Nullable String cacheKey) {
        this.cacheKey = cacheKey;
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
}
