package com.tokopedia.topads.sdk.base;

import com.tokopedia.topads.sdk.domain.TopAdsParams;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class Config {

    public static final String TOPADS_URL = "https://ta.tokopedia.com/promo/";
    public static final int ERROR_CODE_INVALID_RESPONSE = 911;
    public static final String DEFAULT_DEVICE = "android";
    public static final String DEFAULT_CLIENT_ID = "12";

    private String baseUrl;
    private String userId;
    private String sessionId;
    private String device;
    private String clientId;
    private Endpoint endpoint;
    private boolean withPreferedCategory;

    public Config(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.userId = builder.userId;
        this.sessionId = builder.sessionId;
        this.device = builder.device;
        this.clientId = builder.clientId;
        this.withPreferedCategory = builder.withPreferedCategory;
        this.endpoint = builder.endpoint;
    }

    public boolean isWithPreferedCategory() {
        return withPreferedCategory;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getUserId() {
        return userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getDevice() {
        return device;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public String getClientId() {
        return clientId;
    }

    public static class Builder {

        private String baseUrl;
        private String userId;
        private String sessionId;
        private String device;
        private String clientId;
        private Endpoint endpoint;
        private boolean withPreferedCategory;

        public Builder() {
            baseUrl = TOPADS_URL;
            device = DEFAULT_DEVICE;
            clientId = DEFAULT_CLIENT_ID;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setSessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public Builder setEndpoint(Endpoint endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder withPreferedCategory() {
            this.withPreferedCategory = true;
            return this;
        }



        public Config build() {
            return new Config(this);
        }
    }

}
