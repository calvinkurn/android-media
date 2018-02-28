package com.tokopedia.session.register.data.model;

import com.tokopedia.session.register.data.pojo.RegisterPhoneNumberData;

/**
 * @author yfsx on 28/2/18.
 */

public class RegisterPhoneNumberModel {

    private boolean success;
    private RegisterPhoneNumberData registerPhoneNumberData;
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

    public RegisterPhoneNumberData getRegisterPhoneNumberData() {
        return registerPhoneNumberData;
    }

    public void setRegisterPhoneNumberData(RegisterPhoneNumberData registerPhoneNumberData) {
        this.registerPhoneNumberData = registerPhoneNumberData;
    }
}
