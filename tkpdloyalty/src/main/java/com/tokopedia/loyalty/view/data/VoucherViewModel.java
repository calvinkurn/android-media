package com.tokopedia.loyalty.view.data;

/**
 * Created by kris on 12/5/17. Tokopedia
 */

public class VoucherViewModel {

    private boolean isSuccess;
    private String message;
    private String amount;
    private String code;
    private long rawDiscount;
    private long rawCashback;

    public VoucherViewModel() {}

    public VoucherViewModel(boolean isSuccess, String message, String amount, String code, long rawDiscount, long rawCashback) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.amount = amount;
        this.code = code;
        this.rawDiscount = rawDiscount;
        this.rawCashback = rawCashback;
    }

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

    public long getRawDiscount() {
        return rawDiscount;
    }

    public void setRawDiscount(long rawDiscount) {
        this.rawDiscount = rawDiscount;
    }

    public long getRawCashback() {
        return rawCashback;
    }

    public void setRawCashback(long rawCashback) {
        this.rawCashback = rawCashback;
    }

}
