package com.tokopedia.otp.data.model;

import com.tokopedia.core.network.entity.otp.RequestOtpData;

/**
 * Created by nisie on 3/7/17.
 * @deprecated use RequestOtpViewModel instead.
 */
@Deprecated
public class RequestOtpModel {
    private boolean success;
    private RequestOtpData requestOtpData;
    private String errorMessage;
    private String statusMessage;
    private int responseCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public RequestOtpData getRequestOtpData() {
        return requestOtpData;
    }

    public void setRequestOtpData(RequestOtpData requestOtpData) {
        this.requestOtpData = requestOtpData;
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
}
