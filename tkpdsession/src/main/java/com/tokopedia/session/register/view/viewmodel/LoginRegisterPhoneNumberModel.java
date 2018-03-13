package com.tokopedia.session.register.view.viewmodel;

import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.register.data.model.RegisterPhoneNumberModel;

/**
 * @author by yfsx on 13/03/18.
 */

public class LoginRegisterPhoneNumberModel {
    private RegisterPhoneNumberModel registerPhoneNumberModel;
    private MakeLoginDomain makeLoginDomain;

    public RegisterPhoneNumberModel getRegisterPhoneNumberModel() {
        return registerPhoneNumberModel;
    }

    public void setRegisterPhoneNumberModel(RegisterPhoneNumberModel registerPhoneNumberModel) {
        this.registerPhoneNumberModel = registerPhoneNumberModel;
    }

    public MakeLoginDomain getMakeLoginDomain() {
        return makeLoginDomain;
    }

    public void setMakeLoginDomain(MakeLoginDomain makeLoginDomain) {
        this.makeLoginDomain = makeLoginDomain;
    }
}
