package com.tokopedia.session.login.domain.model;

/**
 * @author by nisie on 5/26/17.
 */

public class MakeLoginDomainModel {

    private boolean success;
    private MakeLoginDomainData makeLoginDomainData;
    private String errorMessage;
    private String statusMessage;
    private int responseCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public MakeLoginDomainData getMakeLoginDomainData() {
        return makeLoginDomainData;
    }

    public void setMakeLoginDomainData(MakeLoginDomainData makeLoginDomainData) {
        this.makeLoginDomainData = makeLoginDomainData;
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
