package com.tokopedia.session.register.domain.model;

import com.tokopedia.core.profile.model.GetUserInfoDomainModel;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;

/**
 * @author by nisie on 10/11/17.
 */

public class LoginSosmedDomain {
    private TokenViewModel tokenModel;
    private GetUserInfoDomainModel info;
    private MakeLoginDomain makeLoginModel;

    public void setTokenModel(TokenViewModel tokenModel) {
        this.tokenModel = tokenModel;
    }

    public TokenViewModel getTokenModel() {
        return tokenModel;
    }

    public void setInfo(GetUserInfoDomainModel info) {
        this.info = info;
    }

    public GetUserInfoDomainModel getInfo() {
        return info;
    }

    public void setMakeLoginModel(MakeLoginDomain makeLoginModel) {
        this.makeLoginModel = makeLoginModel;
    }

    public MakeLoginDomain getMakeLoginModel() {
        return makeLoginModel;
    }
}
