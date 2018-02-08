package com.tokopedia.otp.securityquestion.data.model.changephonenumberrequest;

import com.tokopedia.otp.securityquestion.domain.pojo.changephonenumberrequest.UploadHostData;

/**
 * Created by nisie on 3/9/17.
 */

public class UploadHostModel {
    private boolean success;
    private UploadHostData uploadHostData;
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

    public UploadHostData getUploadHostData() {
        return uploadHostData;
    }

    public void setUploadHostData(UploadHostData uploadHostData) {
        this.uploadHostData = uploadHostData;
    }
}
