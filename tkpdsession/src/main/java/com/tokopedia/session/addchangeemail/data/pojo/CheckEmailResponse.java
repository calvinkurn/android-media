package com.tokopedia.session.addchangeemail.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 09/03/18.
 */

public class CheckEmailResponse {
    @SerializedName("is_success")
    private int isSuccess;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }
}
