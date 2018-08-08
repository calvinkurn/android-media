
package com.tokopedia.otp.tokocashotp.domain.pojo.verifyotp;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyOtpTokoCashPojo {

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("tokocash_account_exist")
    @Expose
    private boolean tokocashAccountExist;
    @SerializedName("user_details")
    @Expose
    private List<UserDetail> userDetails = null;
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

    public List<UserDetail> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(List<UserDetail> userDetails) {
        this.userDetails = userDetails;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

}
