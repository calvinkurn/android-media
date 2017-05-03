package com.tokopedia.otp.phoneverification.data;

import com.tokopedia.core.network.entity.phoneverification.VerifyPhoneNumberData;

/**
 * Created by nisie on 3/7/17.
 */
public class VerifyPhoneNumberModel {
    private boolean success;
    private VerifyPhoneNumberData verifyPhoneNumberData;
    private String errorMessage;
    private String statusMessage;
    private int responseCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public VerifyPhoneNumberData getVerifyPhoneNumberData() {
        return verifyPhoneNumberData;
    }

    public void setVerifyPhoneNumberData(VerifyPhoneNumberData verifyPhoneNumberData) {
        this.verifyPhoneNumberData = verifyPhoneNumberData;
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
