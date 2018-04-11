package com.tokopedia.otp.registerphonenumber.view.viewmodel;


/**
 * @author by yfsx on 5/3/18.
 */

public class VerifyOtpViewModel {

    private boolean isSuccess;
    private String uuid;

    public VerifyOtpViewModel(boolean isSuccess, String uuid) {
        this.isSuccess = isSuccess;
        this.uuid = uuid;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getUuid() {
        return uuid;
    }

}
