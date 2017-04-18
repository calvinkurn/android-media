package com.tokopedia.session.activation.data;

/**
 * Created by nisie on 4/18/17.
 */

public class ChangeEmailPass {

    private String oldEmail;
    private String newEmail;
    private String password;

    public String getOldEmail() {
        return oldEmail;
    }

    public void setOldEmail(String oldEmail) {
        this.oldEmail = oldEmail;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
