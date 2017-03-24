package com.tokopedia.inbox.rescenter.history.domain.model;

/**
 * Created by hangnadi on 3/23/17.
 */

public class HistoryAwbData {
    private boolean success;
    private int errorCode;
    private String messageError;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public String getMessageError() {
        return messageError;
    }
}
