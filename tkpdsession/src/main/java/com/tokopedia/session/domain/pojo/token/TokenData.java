package com.tokopedia.session.domain.pojo.token;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by stevenfredian on 5/26/16.
 */

public class TokenData {

    /**
     * access_token : 2YotnFZFEjr1zCsicMWpAA
     * token_type : example
     * expires_in : 3600
     * refresh_token : tGzv3JOkF0XG5Qx2TlKWIA
     * error : access_denied
     * error_description : The resource owner or authorization server denied the request.
     */

    @SerializedName("access_token")
    @Expose
    private String accessToken;

    @SerializedName("token_type")
    @Expose
    private String tokenType;

    @SerializedName("expires_in")
    @Expose
    private int expiresIn;

    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getExpires_in() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
