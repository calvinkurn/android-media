package com.tokopedia.session.register.domain.model;

import com.tokopedia.core.profile.model.GetUserInfoDomainModel;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;

/**
 * @author by nisie on 10/11/17.
 */

public class RegisterSosmedDomain {
    private TokenViewModel tokenModel;
    private GetUserInfoDomainModel info;

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
}
