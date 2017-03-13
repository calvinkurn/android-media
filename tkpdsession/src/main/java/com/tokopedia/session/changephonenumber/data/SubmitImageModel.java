package com.tokopedia.session.changephonenumber.data;

import com.tokopedia.core.network.entity.changephonenumberrequest.SubmitImageData;

/**
 * Created by nisie on 3/13/17.
 */

public class SubmitImageModel {
    private boolean success;
    private SubmitImageData submitImageData;
    private String errorMessage;
    private String statusMessage;
    private int responseCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public boolean isResponseSuccess() {
        return responseCode == 200;
    }

    public SubmitImageData getSubmitImageData() {
        return submitImageData;
    }

    public void setSubmitImageData(SubmitImageData submitImageData) {
        this.submitImageData = submitImageData;
    }
}

