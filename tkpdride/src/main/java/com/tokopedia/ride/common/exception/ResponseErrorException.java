package com.tokopedia.ride.common.exception;

/**
 * Created by alvarisi on 4/7/17.
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