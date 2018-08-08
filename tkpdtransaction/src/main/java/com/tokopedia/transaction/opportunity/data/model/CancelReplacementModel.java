package com.tokopedia.transaction.opportunity.data.model;

/**
 * @author by nisie on 6/2/17.
 */

public class CancelReplacementModel {
    private boolean success;
    private CancelReplacementDomain cancelreplacementDomain;
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

    public CancelReplacementDomain getCancelreplacementDomain() {
        return cancelreplacementDomain;
    }

    public void setCancelreplacementDomain(CancelReplacementDomain cancelreplacementDomain) {
        this.cancelreplacementDomain = cancelreplacementDomain;
    }
}
