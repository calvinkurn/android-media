package com.tokopedia.otp.registerphonenumber.view.viewmodel;


/**
 * @author by yfsx on 5/3/17.
 */

public class VerifyOtpViewModel {

    private String key;
    private boolean isVerified;

    public VerifyOtpViewModel(String key,
                              boolean isVerified) {
        this.key = key;
        this.isVerified = isVerified;
    }

    public String getKey() {
        return key;
    }

    public boolean isVerified() {
        return isVerified;
    }

}
