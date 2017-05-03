package com.tokopedia.session.register.model;

/**
 * Created by nisie on 1/30/17.
 */
public class RegisterStep1ViewModel {
    String name;
    String password;
    String email;
    boolean isAutoVerify;

    public boolean isAutoVerify() {
        return isAutoVerify;
    }

    public void setAutoVerify(boolean autoVerify) {
        isAutoVerify = autoVerify;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
