package com.tokopedia.transaction.others.creditcard.authenticator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 10/12/17. Tokopedia
 */

public class CheckWhiteListRequestData {

    @SerializedName("user_email")
    @Expose
    private String userEmail;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
