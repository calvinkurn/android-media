package com.tokopedia.session.register.model.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 1/31/17.
 */

public class ValidateEmailResult {
    @SerializedName("email_status")
    @Expose
    private int emailStatus;
    @SerializedName("action")
    @Expose
    private int action;

    public int getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(int emailStatus) {
        this.emailStatus = emailStatus;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

}
