package com.tokopedia.inbox.rescenter.createreso.domain.model.solution;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 14/09/17.
 */

public class EditAppealResolutionSolutionDomain {

    @Nullable
    private boolean isSuccess;

    public EditAppealResolutionSolutionDomain(boolean isSuccess, String successMessage) {
        this.isSuccess = isSuccess;
        this.message = successMessage;
    }


    @Nullable
    private String message;

    @Nullable
    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(@Nullable boolean success) {
        isSuccess = success;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    public void setMessage(@Nullable String message) {
        this.message = message;
    }
}
