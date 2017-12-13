package com.tokopedia.otp.data.model;

/**
 * @author by nisie on 10/23/17.
 */

public class ValidateOtpDomain {

    private boolean isSuccess;
    private String uuid;

    public ValidateOtpDomain(boolean isSuccess, String uuid) {
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
