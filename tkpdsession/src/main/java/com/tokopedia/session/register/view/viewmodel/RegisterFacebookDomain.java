package com.tokopedia.session.register.view.viewmodel;

import com.tokopedia.session.domain.model.TokenViewModel;

/**
 * @author by nisie on 10/11/17.
 */

public class RegisterFacebookDomain {
    private TokenViewModel tokenModel;

    public void setTokenModel(TokenViewModel tokenModel) {
        this.tokenModel = tokenModel;
    }

    public TokenViewModel getTokenModel() {
        return tokenModel;
    }
}
