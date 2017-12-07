package com.tokopedia.loyalty.view.data;

/**
 * Created by kris on 12/5/17. Tokopedia
 */

public class VoucherViewModel {

    private boolean isSuccess;

    private String message;

    private String amount;

    private String code;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
