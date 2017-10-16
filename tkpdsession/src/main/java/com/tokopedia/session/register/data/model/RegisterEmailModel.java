package com.tokopedia.session.register.data.model;

import com.tokopedia.session.register.data.pojo.RegisterEmailData;

/**
 * Created by nisie on 4/13/17.
 */

public class RegisterEmailModel {

    private boolean success;
    private RegisterEmailData registerEmailData;
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

    public RegisterEmailData getRegisterEmailData() {
        return registerEmailData;
    }

    public void setRegisterEmailData(RegisterEmailData registerEmailData) {
        this.registerEmailData = registerEmailData;
    }
}
