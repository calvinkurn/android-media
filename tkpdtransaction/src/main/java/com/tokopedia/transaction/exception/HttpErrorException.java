package com.tokopedia.transaction.exception;

import java.io.IOException;

/**
 * @author anggaprasetiyo on 11/29/16.
 */

public class HttpErrorException extends IOException {

    private static final long serialVersionUID = -6934568265822386135L;

    public HttpErrorException() {
    }

    public HttpErrorException(int errorCode) {
        super("HTTP Error ==> Error code : " + errorCode);
    }

    public HttpErrorException(int errorCode, String detailMessage) {
        super("HTTP Error ==> Error code : " + errorCode + "\n" + detailMessage);
    }


    public HttpErrorException(String detailMessage) {
        super(detailMessage);
    }
}
