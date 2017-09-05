package com.tokopedia.inbox.rescenter.createreso.domain.model.createreso;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 05/09/17.
 */

public class CreateResoStep2Domain {

    @Nullable
    private String resolution;

    @Nullable
    private boolean isSuccess;

    public CreateResoStep2Domain(@Nullable String resolution) {
        this.resolution = resolution;
    }

    @Nullable
    public String getResolution() {
        return resolution;
    }

    public void setResolution(@Nullable String resolution) {
        this.resolution = resolution;
    }

    @Nullable
    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(@Nullable boolean success) {
        isSuccess = success;
    }
}
