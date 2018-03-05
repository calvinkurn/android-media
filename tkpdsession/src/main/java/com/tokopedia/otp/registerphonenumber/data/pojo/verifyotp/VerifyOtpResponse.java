
package com.tokopedia.otp.registerphonenumber.data.pojo.verifyotp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VerifyOtpResponse {

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("tokocash_account_exist")
    @Expose
    private boolean tokocashAccountExist;
    @SerializedName("user_details")
    @Expose
    private List<UserDetailResponse> userDetailResponses = null;
    @SerializedName("verified")
    @Expose
    private boolean verified;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isTokocashAccountExist() {
        return tokocashAccountExist;
    }

    public void setTokocashAccountExist(boolean tokocashAccountExist) {
        this.tokocashAccountExist = tokocashAccountExist;
    }

    public List<UserDetailResponse> getUserDetailResponses() {
        return userDetailResponses;
    }

    public void setUserDetailResponses(List<UserDetailResponse> userDetailResponses) {
        this.userDetailResponses = userDetailResponses;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

}
