package com.tokopedia.otp.tokocashotp.view.viewmodel;

import com.tokopedia.core.profile.model.GetUserInfoDomainModel;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;
import com.tokopedia.session.login.loginphonenumber.domain.model.CodeTokoCashDomain;

/**
 * @author by nisie on 12/5/17.
 */

public class LoginTokoCashViewModel {
    private CodeTokoCashDomain tokoCashCode;
    private TokenViewModel accountsToken;
    private MakeLoginDomain makeLoginDomain;
    private GetUserInfoDomainModel userInfoDomain;

    public void setTokoCashCode(CodeTokoCashDomain tokoCashCode) {
        this.tokoCashCode = tokoCashCode;
    }

    public CodeTokoCashDomain getTokoCashCode() {
        return tokoCashCode;
    }

    public void setAccountsToken(TokenViewModel accountsToken) {
        this.accountsToken = accountsToken;
    }

    public TokenViewModel getAccountsToken() {
        return accountsToken;
    }

    public void setMakeLoginDomain(MakeLoginDomain makeLoginDomain) {
        this.makeLoginDomain = makeLoginDomain;
    }

    public MakeLoginDomain getMakeLoginDomain() {
        return makeLoginDomain;
    }

    public void setUserInfoDomain(GetUserInfoDomainModel userInfoDomain) {
        this.userInfoDomain = userInfoDomain;
    }

    public GetUserInfoDomainModel getUserInfoDomain() {
        return userInfoDomain;
    }
}
