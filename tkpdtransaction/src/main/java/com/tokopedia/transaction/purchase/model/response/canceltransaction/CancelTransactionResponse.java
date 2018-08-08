package com.tokopedia.transaction.purchase.model.response.canceltransaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 9/13/17. Tokopedia
 */

public class CancelTransactionResponse {

    @SerializedName("success")
    @Expose
    private int success;

    @SerializedName("message")
    @Expose
    private String message;

    public int getResponseCode() {
        return success;
    }

    public void setSuccess(int status) {
        this.success = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
