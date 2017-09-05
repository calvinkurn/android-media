package com.tokopedia.inbox.rescenter.createreso.domain.model.createreso;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 05/09/17.
 */

public class CreateResoStep1Domain {

    @Nullable
    private ResolutionDomain resolution;

    @Nullable
    private String cacheKey;

    @Nullable
    private boolean isSuccess;

    public CreateResoStep1Domain(ResolutionDomain resolution, String cacheKey) {
        this.resolution = resolution;
        this.cacheKey = cacheKey;
    }

    @Nullable
    public ResolutionDomain getResolution() {
        return resolution;
    }

    public void setResolution(@Nullable ResolutionDomain resolution) {
        this.resolution = resolution;
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
}
