
package com.tokopedia.otp.tokocashotp.domain.pojo.accesstoken;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAccessTokenTokoCashPojo {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("state")
    @Expose
    private String state;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
