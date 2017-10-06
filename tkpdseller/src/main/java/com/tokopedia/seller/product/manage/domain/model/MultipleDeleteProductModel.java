package com.tokopedia.seller.product.manage.domain.model;

/**
 * Created by zulfikarrahman on 10/5/17.
 */

public class MultipleDeleteProductModel {
    private boolean isSuccess;
    private int countOfSuccess;
    private int countOfError;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public int getCountOfSuccess() {
        return countOfSuccess;
    }

    public void setCountOfSuccess(int countOfSuccess) {
        this.countOfSuccess = countOfSuccess;
    }

    public int getCountOfError() {
        return countOfError;
    }

    public void setCountOfError(int countOfError) {
        this.countOfError = countOfError;
    }
}
