package com.tokopedia.transaction.exception;


import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.network.constant.ResponseStatus;

import java.io.IOException;

/**
 * @author anggaprasetiyo on 11/29/16.
 */

public class HttpErrorException extends IOException {

    private static final long serialVersionUID = -6934568265822386135L;
    private final int errorCode;

    public HttpErrorException(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        switch (errorCode) {
            case ResponseStatus.SC_REQUEST_TIMEOUT:
                return ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
            case ResponseStatus.SC_GATEWAY_TIMEOUT:
                return ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
            case ResponseStatus.SC_INTERNAL_SERVER_ERROR:
                return ErrorNetMessage.MESSAGE_ERROR_SERVER;
            case ResponseStatus.SC_FORBIDDEN:
                return ErrorNetMessage.MESSAGE_ERROR_FORBIDDEN;
            case ResponseStatus.SC_BAD_GATEWAY:
                return ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
            case ResponseStatus.SC_BAD_REQUEST:
                return ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
            default:
                return ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
        }
    }
}
