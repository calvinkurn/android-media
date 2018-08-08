package com.tokopedia.loyalty.domain.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 12/5/17. Tokopedia
 */

public class ValidateRedeemCouponResponse {

    @SerializedName("is_valid")
    @Expose
    private int isValid;

    @SerializedName("message_succcess")
    @Expose
    private String messageSuccess;

    public int getIsValid() {
        return isValid;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }

    public String getMessageSuccess() {
        return messageSuccess;
    }

    public void setMessageSuccess(String messageSuccess) {
        this.messageSuccess = messageSuccess;
    }
}
