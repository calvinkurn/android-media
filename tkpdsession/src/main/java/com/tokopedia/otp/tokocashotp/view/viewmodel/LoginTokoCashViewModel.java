package com.tokopedia.otp.tokocashotp.view.viewmodel;

import com.tokopedia.otp.tokocashotp.domain.model.AccessTokenTokoCashDomain;

/**
 * @author by nisie on 12/5/17.
 */

public class LoginTokoCashViewModel {
    private AccessTokenTokoCashDomain accessToken;

    public void setAccessToken(AccessTokenTokoCashDomain accessToken) {
        this.accessToken = accessToken;
    }

    public AccessTokenTokoCashDomain getAccessToken() {
        return accessToken;
    }
}
