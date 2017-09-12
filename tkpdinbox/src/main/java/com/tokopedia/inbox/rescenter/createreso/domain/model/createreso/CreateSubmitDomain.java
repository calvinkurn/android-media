package com.tokopedia.inbox.rescenter.createreso.domain.model.createreso;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 05/09/17.
 */

public class CreateSubmitDomain {

    @Nullable
    private ResolutionDomain resolution;

    @Nullable
    private boolean isSuccess;

    @Nullable
    private String successMessage;

    public CreateSubmitDomain(ResolutionDomain resolution, String successMessage) {
        this.resolution = resolution;
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
}
