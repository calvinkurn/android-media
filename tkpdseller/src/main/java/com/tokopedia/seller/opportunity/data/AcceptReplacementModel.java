package com.tokopedia.seller.opportunity.data;

import com.tokopedia.core.network.entity.replacement.AcceptReplacementData;

/**
 * Created by hangnadi on 3/3/17.
 */
public class AcceptReplacementModel {

    private boolean success;
    private AcceptReplacementData acceptReplacementData;
    private String errorMessage;
    private int errorCode;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setAcceptReplacementData(AcceptReplacementData acceptReplacementData) {
        this.acceptReplacementData = acceptReplacementData;
    }

    public AcceptReplacementData getAcceptReplacementData() {
        return acceptReplacementData;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
