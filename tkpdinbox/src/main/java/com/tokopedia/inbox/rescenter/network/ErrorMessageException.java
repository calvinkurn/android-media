package com.tokopedia.inbox.rescenter.network;

/**
 * @author by yfsx on 26/07/18.
 */

public class ErrorMessageException extends RuntimeException {

    public ErrorMessageException(String errorMessage) {
        super(errorMessage);
    }

    public ErrorMessageException(String errorMessage, int errorCode) {
        super(errorMessage + " " + "( " + errorCode + " )");
    }
}
