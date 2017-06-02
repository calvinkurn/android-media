package com.tokopedia.otp.phoneverification.data;

import com.tokopedia.otp.phoneverification.data.pojo.ChangePhoneNumberData;

/**
 * Created by nisie on 5/10/17.
 */

public class ChangePhoneNumberModel {
    private boolean success;
    private ChangePhoneNumberData changePhoneNumberData;
    private String errorMessage;
    private String statusMessage;
    private int responseCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ChangePhoneNumberData getChangePhoneNumberData() {
        return changePhoneNumberData;
    }

    public void setChangePhoneNumberData(ChangePhoneNumberData changePhoneNumberData) {
        this.changePhoneNumberData = changePhoneNumberData;
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
