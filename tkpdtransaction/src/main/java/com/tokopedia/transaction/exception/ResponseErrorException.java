package com.tokopedia.transaction.exception;

/**
 * @author anggaprasetiyo on 11/29/16.
 */

public class ResponseErrorException extends Exception {

    private static final long serialVersionUID = 713361211323198507L;

    public ResponseErrorException() {
        super("General Error");
    }

    public ResponseErrorException(String detailMessage) {
        super(detailMessage);
    }

}
