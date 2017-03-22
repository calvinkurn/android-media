package com.tokopedia.inbox.rescenter.detailv2.domain.model;

/**
 * Created by hangnadi on 3/22/17.
 */

public class ResolutionActionDomainData {
    private boolean success;
    private int errorCode;
    private String messageError;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }
}
