package com.tokopedia.session.changename.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 22/03/18.
 */

public class ChangeNameResponse {
    @SerializedName("is_success")
    private int isSuccess;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }
}
