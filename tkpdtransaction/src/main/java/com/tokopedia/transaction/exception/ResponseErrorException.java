package com.tokopedia.transaction.exception;

/**
 * @author anggaprasetiyo on 11/29/16.
 */

public class ResponseErrorException extends Exception {

    private static final long serialVersionUID = 713361211323198507L;
    private static final String GENERAL_ORDER_DETAIL_RESPONSE_ERROR = "General OrderDetailResponseError";

    public ResponseErrorException() {
        super(GENERAL_ORDER_DETAIL_RESPONSE_ERROR);
    }

    public ResponseErrorException(String detailMessage) {
        super(detailMessage);
    }

}
