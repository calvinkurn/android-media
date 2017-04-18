package com.tokopedia.session.activation.data;

import com.tokopedia.session.activation.data.pojo.ChangeEmailData;

/**
 * Created by nisie on 4/18/17.
 */

public class ChangeEmailModel {
    private boolean success;
    private ChangeEmailData changeEmailData;
    private String errorMessage;
    private String statusMessage;
    private int responseCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ChangeEmailData getChangeEmailData() {
        return changeEmailData;
    }

    public void setChangeEmailData(ChangeEmailData changeEmailData) {
        this.changeEmailData = changeEmailData;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}

