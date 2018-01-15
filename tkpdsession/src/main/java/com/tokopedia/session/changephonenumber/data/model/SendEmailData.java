package com.tokopedia.session.changephonenumber.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by milhamj on 28/12/17.
 */

public class SendEmailData {
    @SerializedName("is_success")
    private int isSuccess;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }
}
