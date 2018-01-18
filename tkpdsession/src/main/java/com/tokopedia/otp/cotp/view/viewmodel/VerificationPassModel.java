package com.tokopedia.otp.cotp.view.viewmodel;

import java.util.ArrayList;

/**
 * @author by nisie on 12/28/17.
 */

public class VerificationPassModel {

    private String phoneNumber;
    private String email;
    private int otpType;
    private InterruptVerificationViewModel interruptModel;
    private boolean canUseOtherMethod;

    public VerificationPassModel(String phoneNumber, String email, int otpType,
                                 InterruptVerificationViewModel interruptModel,
                                 boolean canUseOtherMethod) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.otpType = otpType;
        this.interruptModel = interruptModel;
        this.canUseOtherMethod = canUseOtherMethod;
    }

    public VerificationPassModel(String phoneNumber, String email, int otpType, boolean canUseOtherMethod) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.otpType = otpType;
        this.interruptModel = null;
        this.canUseOtherMethod = canUseOtherMethod;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public int getOtpType() {
        return otpType;
    }

    public InterruptVerificationViewModel getInterruptModel() {
        return interruptModel;
    }

    public boolean canUseOtherMethod() {
        return canUseOtherMethod;
    }

}
