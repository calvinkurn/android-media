
package com.tokopedia.session.domain.pojo.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SecurityPojo {

    @SerializedName("allow_login")
    @Expose
    private int allowLogin;
    @SerializedName("user_check_security_2")
    @Expose
    private int userCheckSecurity2;
    @SerializedName("user_check_security_1")
    @Expose
    private int userCheckSecurity1;

    public int getAllowLogin() {
        return allowLogin;
    }

    public void setAllowLogin(int allowLogin) {
        this.allowLogin = allowLogin;
    }

    public int getUserCheckSecurity2() {
        return userCheckSecurity2;
    }

    public void setUserCheckSecurity2(int userCheckSecurity2) {
        this.userCheckSecurity2 = userCheckSecurity2;
    }

    public int getUserCheckSecurity1() {
        return userCheckSecurity1;
    }

    public void setUserCheckSecurity1(int userCheckSecurity1) {
        this.userCheckSecurity1 = userCheckSecurity1;
    }

}
