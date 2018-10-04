package com.tokopedia.session.addchangepassword.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 23/03/18.
 */

public class AddPasswordResponse {
    @SerializedName("is_success")
    private int isSuccess;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }
}
