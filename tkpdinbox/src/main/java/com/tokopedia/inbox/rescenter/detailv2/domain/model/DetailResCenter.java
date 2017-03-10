package com.tokopedia.inbox.rescenter.detailv2.domain.model;

/**
 * Created by hangnadi on 3/9/17.
 */

public class DetailResCenter {
    private boolean success;
    private String messageError;
    private int errorCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
