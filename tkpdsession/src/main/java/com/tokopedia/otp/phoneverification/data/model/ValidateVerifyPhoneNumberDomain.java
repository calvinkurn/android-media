package com.tokopedia.otp.phoneverification.data.model;

import com.tokopedia.otp.data.model.ValidateOtpDomain;
import com.tokopedia.otp.phoneverification.data.VerifyPhoneNumberDomain;

/**
 * @author by nisie on 10/24/17.
 */

public class ValidateVerifyPhoneNumberDomain {
    private ValidateOtpDomain validateOtpDomain;
    private VerifyPhoneNumberDomain verifyPhoneDomain;

    public void setValidateOtpDomain(ValidateOtpDomain validateOtpDomain) {
        this.validateOtpDomain = validateOtpDomain;
    }

    public ValidateOtpDomain getValidateOtpDomain() {
        return validateOtpDomain;
    }

    public void setVerifyPhoneDomain(VerifyPhoneNumberDomain verifyPhoneDomain) {
        this.verifyPhoneDomain = verifyPhoneDomain;
    }

    public VerifyPhoneNumberDomain getVerifyPhoneDomain() {
        return verifyPhoneDomain;
    }

}
