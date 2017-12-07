package com.tokopedia.session.login.loginphonenumber.domain.model;

/**
 * @author by nisie on 12/6/17.
 */

public class AccessTokenTokoCashDomain {
    private final String accessToken;
    private final int expiresIn;
    private final String scope;
    private final String tokenType;

    public AccessTokenTokoCashDomain(String accessToken, int expiresIn, String scope, String tokenType) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public String getTokenType() {
        return tokenType;
    }
}
