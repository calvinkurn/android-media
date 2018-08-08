package com.tokopedia.session.activation.data;

import com.tokopedia.session.activation.data.pojo.ActivateUnicodeData;

/**
 * Created by nisie on 4/17/17.
 */

public class ActivateUnicodeModel {

    private boolean success;
    private ActivateUnicodeData activateUnicodeData;
    private String errorMessage;
    private String statusMessage;
    private int responseCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ActivateUnicodeData getActivateUnicodeData() {
        return activateUnicodeData;
    }

    public void setActivateUnicodeData(ActivateUnicodeData activateUnicodeData) {
        this.activateUnicodeData = activateUnicodeData;
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
