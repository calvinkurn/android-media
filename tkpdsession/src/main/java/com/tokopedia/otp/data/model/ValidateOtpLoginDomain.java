package com.tokopedia.otp.data.model;

import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;

/**
 * @author by nisie on 10/23/17.
 */

public class ValidateOtpLoginDomain {

    ValidateOtpDomain validateOtpDomain;
    MakeLoginDomain makeLoginDomain;

    public ValidateOtpDomain getValidateOtpDomain() {
        return validateOtpDomain;
    }

    public void setValidateOtpDomain(ValidateOtpDomain validateOtpDomain) {
        this.validateOtpDomain = validateOtpDomain;
    }

    public MakeLoginDomain getMakeLoginDomain() {
        return makeLoginDomain;
    }

    public void setMakeLoginDomain(MakeLoginDomain makeLoginDomain) {
        this.makeLoginDomain = makeLoginDomain;
    }
}
