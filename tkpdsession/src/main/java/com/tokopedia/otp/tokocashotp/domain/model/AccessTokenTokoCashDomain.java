package com.tokopedia.otp.tokocashotp.domain.model;

/**
 * @author by nisie on 12/5/17.
 */

public class AccessTokenTokoCashDomain {
    private final String accessToken;

    public AccessTokenTokoCashDomain(String code) {
        this.accessToken = code;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
