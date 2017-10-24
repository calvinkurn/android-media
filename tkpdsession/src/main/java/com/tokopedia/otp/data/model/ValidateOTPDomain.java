package com.tokopedia.otp.data.model;

/**
 * @author by nisie on 10/23/17.
 */

public class ValidateOTPDomain {

    private boolean isSuccess;
    private String uuid;

    public ValidateOTPDomain(boolean isSuccess, String uuid) {
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
