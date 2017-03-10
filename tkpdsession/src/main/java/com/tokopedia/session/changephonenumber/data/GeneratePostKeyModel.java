package com.tokopedia.session.changephonenumber.data;

import com.tokopedia.core.network.entity.changephonenumberrequest.GeneratePostKeyData;

/**
 * Created by nisie on 3/9/17.
 */

public class GeneratePostKeyModel {
    private boolean success;
    private GeneratePostKeyData generatePostKeyData;
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

    public GeneratePostKeyData getGeneratePostKeyData() {
        return generatePostKeyData;
    }

    public void setGeneratePostKeyData(GeneratePostKeyData generatePostKeyData) {
        this.generatePostKeyData = generatePostKeyData;
    }
}
