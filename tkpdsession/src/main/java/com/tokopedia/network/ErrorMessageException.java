package com.tokopedia.network;

/**
 * Created by nisie on 3/14/17.
 */

public class ErrorMessageException extends RuntimeException {

    public ErrorMessageException(String errorMessage) {
        super(errorMessage);
    }

    public ErrorMessageException(String errorMessage, int errorCode) {
        super(errorMessage + " " + "( " + errorCode + " )");
    }
}
