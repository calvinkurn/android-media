package com.tokopedia.otp.cotp.view.viewmodel;

import java.util.ArrayList;

/**
 * @author by nisie on 12/28/17.
 */

public class VerificationPassModel {

    String phoneNumber;
    ArrayList<MethodItem> listAvailableMethods;
    private String email;
    private int otpType;
    InterruptVerificationViewModel interruptModel;

    public VerificationPassModel(String phoneNumber,
                                 String email,
                                 ArrayList<MethodItem> listAvailableMethods, int otpType) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.listAvailableMethods = listAvailableMethods;
        this.otpType = otpType;
        this.interruptModel = null;
    }

    public VerificationPassModel(String phoneNumber,
                                 String email,
                                 ArrayList<MethodItem> listAvailableMethods,
                                 int otpType,
                                 InterruptVerificationViewModel interruptModel) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.listAvailableMethods = listAvailableMethods;
        this.otpType = otpType;
        this.interruptModel = interruptModel;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ArrayList<MethodItem> getListAvailableMethods() {
        return listAvailableMethods;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getOtpType() {
        return otpType;
    }

    public InterruptVerificationViewModel getInterruptModel() {
        return interruptModel;
    }
}
