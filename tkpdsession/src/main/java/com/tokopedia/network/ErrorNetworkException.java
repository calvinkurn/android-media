package com.tokopedia.network;

import java.io.IOException;

/**
 * @author by yfsx on 09/04/18.
 */

public class ErrorNetworkException extends IOException {

    private String errorMessage;
    private String errorCode;

    public ErrorNetworkException(String errorMessage, String errorCode) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
