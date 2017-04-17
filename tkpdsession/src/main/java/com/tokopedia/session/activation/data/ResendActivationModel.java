package com.tokopedia.session.activation.data;

import com.tokopedia.core.network.entity.changephonenumberrequest.CheckStatusData;
import com.tokopedia.session.activation.data.pojo.ResendActivationData;

/**
 * Created by nisie on 4/17/17.
 */

public class ResendActivationModel {

    private boolean success;
    private ResendActivationData resendActivationData;
    private String errorMessage;
    private String statusMessage;
    private int responseCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ResendActivationData getResendActivationData() {
        return resendActivationData;
    }

    public void setResendActivationData(ResendActivationData resendActivationData) {
        this.resendActivationData = resendActivationData;
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
