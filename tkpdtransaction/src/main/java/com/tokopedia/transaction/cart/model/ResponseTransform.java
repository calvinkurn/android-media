package com.tokopedia.transaction.cart.model;

/**
 * @author anggaprasetiyo on 12/1/16.
 */

public class ResponseTransform<T> {

    private T data;
    private String messageSuccess;
    private String messageError;
    private Throwable throwable;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessageSuccess() {
        return messageSuccess;
    }

    public void setMessageSuccess(String messageSuccess) {
        this.messageSuccess = messageSuccess;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
