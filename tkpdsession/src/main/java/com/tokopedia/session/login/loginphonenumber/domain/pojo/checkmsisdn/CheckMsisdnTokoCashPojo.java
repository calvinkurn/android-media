package com.tokopedia.session.login.loginphonenumber.domain.pojo.checkmsisdn;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 12/6/17.
 */

public class CheckMsisdnTokoCashPojo {

    @SerializedName("tokopedia_account_exist")
    @Expose
    private boolean tokopediaAccountExist;
    @SerializedName("tokocash_account_exist")
    @Expose
    private boolean tokocashAccountExist;

    public boolean isTokopediaAccountExist() {
        return tokopediaAccountExist;
    }

    public void setTokopediaAccountExist(boolean tokopediaAccountExist) {
        this.tokopediaAccountExist = tokopediaAccountExist;
    }

    public boolean isTokocashAccountExist() {
        return tokocashAccountExist;
    }

    public void setTokocashAccountExist(boolean tokocashAccountExist) {
        this.tokocashAccountExist = tokocashAccountExist;
    }
}
