package com.tokopedia.session.addchangeemail.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 09/03/18.
 */

public class CheckEmailResponse {
    @SerializedName("isExist")
    private boolean isExist;

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setIsExist(boolean isExist) {
        this.isExist = isExist;
    }
}
