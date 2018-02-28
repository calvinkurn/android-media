package com.tokopedia.tkpdstream.common.network;

import java.io.IOException;

/**
 * @author by nisie on 2/3/18.
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
