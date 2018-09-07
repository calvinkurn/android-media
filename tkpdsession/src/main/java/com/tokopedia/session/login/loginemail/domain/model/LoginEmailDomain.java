package com.tokopedia.session.login.loginemail.domain.model;

import com.tokopedia.core.profile.model.GetUserInfoDomainModel;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;

/**
 * @author by nisie on 12/19/17.
 */

public class LoginEmailDomain {
    private TokenViewModel token;
    private GetUserInfoDomainModel info;
    private MakeLoginDomain loginResult;

    public void setToken(TokenViewModel token) {
        this.token = token;
    }

    public TokenViewModel getToken() {
        return token;
    }

    public void setInfo(GetUserInfoDomainModel info) {
        this.info = info;
    }

    public GetUserInfoDomainModel getInfo() {
        return info;
    }

    public void setLoginResult(MakeLoginDomain loginResult) {
        this.loginResult = loginResult;
    }

    public MakeLoginDomain getLoginResult() {
        return loginResult;
    }
}
