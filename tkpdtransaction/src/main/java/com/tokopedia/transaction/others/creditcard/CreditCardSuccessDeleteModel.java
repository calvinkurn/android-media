package com.tokopedia.transaction.others.creditcard;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 8/23/17. Tokopedia
 */

public class CreditCardSuccessDeleteModel {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
