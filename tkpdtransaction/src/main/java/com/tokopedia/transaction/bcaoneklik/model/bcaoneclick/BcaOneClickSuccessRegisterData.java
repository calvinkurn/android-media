package com.tokopedia.transaction.bcaoneklik.model.bcaoneclick;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 7/25/17. Tokopedia
 */

public class BcaOneClickSuccessRegisterData {

    @SerializedName("success")
    boolean success;

    @SerializedName("message")
    String message;

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
