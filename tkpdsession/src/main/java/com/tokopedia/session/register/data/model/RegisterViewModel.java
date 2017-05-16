package com.tokopedia.session.register.data.model;

/**
 * Created by nisie on 1/30/17.
 */

public class RegisterViewModel {

    private boolean isAgreedTermCondition;

    private String name;
    private String email;
    private String phone;
    private String password;
    private String confirmPassword;
    private int isAutoVerify;

    public boolean isAgreedTermCondition() {
        return isAgreedTermCondition;
    }

    public void setAgreedTermCondition(boolean agreedTermCondition) {
        isAgreedTermCondition = agreedTermCondition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public int getIsAutoVerify() {
        return isAutoVerify;
    }

    public void setIsAutoVerify(int isAutoVerify){
        this.isAutoVerify = isAutoVerify;
    }
}

