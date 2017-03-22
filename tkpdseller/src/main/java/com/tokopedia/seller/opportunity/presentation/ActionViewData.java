package com.tokopedia.seller.opportunity.presentation;

/**
 * Created by hangnadi on 3/3/17.
 */

public class ActionViewData {
    private boolean success;
    private boolean timeOut;
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

    public void setTimeOut(boolean timeOut) {
        this.timeOut = timeOut;
    }

    public boolean isTimeOut() {
        return timeOut;
    }
}
