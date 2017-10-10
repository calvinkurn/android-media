package com.tokopedia.tkpdreactnative.react.domain;

import java.util.HashMap;

/**
 * @author  by alvarisi on 10/6/17.
 */

public class ReactNetworkingConfiguration {
    private String url;
    private HashMap<String, String> params;
    private String method;
    private String encoding;
    private HashMap<String, String> headers;
    private String authorizationMode;

    private ReactNetworkingConfiguration(String url, HashMap<String, String> params, String method, String encoding, HashMap<String, String> headers, String authorizationMode) {
        this.url = url;
        this.params = params;
        this.method = method;
        this.encoding = encoding;
        this.headers = headers;
        this.authorizationMode = authorizationMode;
    }


    public String getUrl() {
        return url;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public String getMethod() {
        return method;
    }

    public String getEncoding() {
        return encoding;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public String getAuthorizationMode() {
        return authorizationMode;
    }

    public static final class Builder {
        private String url;
        private HashMap<String, String> params;
        private String method;
        private String encoding;
        private HashMap<String, String> headers;
        private String authorizationMode;

        public Builder() {
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setParams(HashMap<String, String> params) {
            this.params = params;
            return this;
        }

        public Builder setMethod(String method) {
            this.method = method;
            return this;
        }

        public Builder setEncoding(String encoding) {
            this.encoding = encoding;
            return this;
        }

        public Builder setHeaders(HashMap<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder setAuthorizationMode(String authorizationMode) {
            this.authorizationMode = authorizationMode;
            return this;
        }

        public ReactNetworkingConfiguration build() {
            if (this.method == null) {
                this.method = "GET";
            }
            if (this.params == null) {
                this.params = new HashMap<>();
            }
            if (this.encoding == null) {
                this.encoding = "urlencoded";
            }
            if (this.headers == null) {
                this.headers = new HashMap<>();
            }
            if (this.authorizationMode == null) {
                this.authorizationMode = "none";
            }
            if (this.url == null) {
                throw new RuntimeException("Url must nnot empty");
            }
            return new ReactNetworkingConfiguration(url, params, method, encoding, headers, authorizationMode);
        }
    }
}
