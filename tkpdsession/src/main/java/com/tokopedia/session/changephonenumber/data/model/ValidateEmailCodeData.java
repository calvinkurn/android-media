package com.tokopedia.session.changephonenumber.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by milhamj on 04/01/18.
 */

public class ValidateEmailCodeData {
    @SerializedName("is_success")
    private int isSuccess;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }
}
