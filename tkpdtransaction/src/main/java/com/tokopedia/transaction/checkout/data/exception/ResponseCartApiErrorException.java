package com.tokopedia.transaction.checkout.data.exception;

/**
 * @author anggaprasetiyo on 23/02/18.
 */

public class ResponseCartApiErrorException extends RuntimeException {
    private static final long serialVersionUID = 4222561907629283720L;

    private String sourcePathApi;
    private int errorCode;
    private String errorMessage;

    public ResponseCartApiErrorException(String sourcePathApi, int errorCode, String errorMessage) {
        super(errorMessage);
        this.sourcePathApi = sourcePathApi;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

    public String getSourcePathApi() {
        return sourcePathApi;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
