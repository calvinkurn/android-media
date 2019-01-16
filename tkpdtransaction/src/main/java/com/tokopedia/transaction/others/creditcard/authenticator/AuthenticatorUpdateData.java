package com.tokopedia.transaction.others.creditcard.authenticator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 10/11/17. Tokopedia
 */

public class AuthenticatorUpdateData {

    @SerializedName("user_email")
    @Expose
    private String userEmail;

    @SerializedName("state")
    @Expose
    private int state;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
