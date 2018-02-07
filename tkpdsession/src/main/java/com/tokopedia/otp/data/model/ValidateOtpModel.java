package com.tokopedia.otp.data.model;

import com.tokopedia.core.network.entity.phoneverification.ValidateOtpData;

/**
 * Created by nisie on 3/7/17.
 * @deprecated  use ValidateOTPDomain instead.
 */
@Deprecated
public class ValidateOtpModel {
    private boolean success;
    private ValidateOtpData validateOtpData;
    private String errorMessage;
    private String statusMessage;
    private int responseCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ValidateOtpData getValidateOtpData() {
        return validateOtpData;
    }

    public void setValidateOtpData(ValidateOtpData validateOtpData) {
        this.validateOtpData = validateOtpData;
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

    public boolean isResponseSuccess() {
        return responseCode == 200;
    }
}
