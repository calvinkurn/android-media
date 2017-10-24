package com.tokopedia.otp.data.model;

import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;

/**
 * @author by nisie on 10/23/17.
 */

public class ValidateOTPLoginDomain {

    ValidateOTPDomain validateOTPDomain;
    MakeLoginDomain makeLoginDomain;

    public ValidateOTPDomain getValidateOTPDomain() {
        return validateOTPDomain;
    }

    public void setValidateOTPDomain(ValidateOTPDomain validateOTPDomain) {
        this.validateOTPDomain = validateOTPDomain;
    }

    public MakeLoginDomain getMakeLoginDomain() {
        return makeLoginDomain;
    }

    public void setMakeLoginDomain(MakeLoginDomain makeLoginDomain) {
        this.makeLoginDomain = makeLoginDomain;
    }
}
