package com.tokopedia.session.register.registerphonenumber.view.viewmodel;

import com.tokopedia.core.profile.model.GetUserInfoDomainModel;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.register.data.model.RegisterPhoneNumberModel;

/**
 * @author by yfsx on 13/03/18.
 */

public class LoginRegisterPhoneNumberModel {
    private RegisterPhoneNumberModel registerPhoneNumberModel;
    private MakeLoginDomain makeLoginDomain;
    private GetUserInfoDomainModel getUserInfoDomainModel;

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

    public GetUserInfoDomainModel getGetUserInfoDomainModel() {
        return getUserInfoDomainModel;
    }

    public void setGetUserInfoDomainModel(GetUserInfoDomainModel getUserInfoDomainModel) {
        this.getUserInfoDomainModel = getUserInfoDomainModel;
    }
}
