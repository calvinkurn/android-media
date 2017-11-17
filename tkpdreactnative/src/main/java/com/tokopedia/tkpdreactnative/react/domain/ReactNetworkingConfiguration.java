package com.tokopedia.tkpdreactnative.react.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.HashMap;
import java.util.Map;

/**
 * @author  by alvarisi on 10/6/17.
 */

public class ReactNetworkingConfiguration {
    private String url;
    private TKPDMapParam<String, String> params;
    private String method;
    private String encoding;
    private HashMap<String, String> headers;
    private String authorizationMode;

    private ReactNetworkingConfiguration(String url, TKPDMapParam<String, String> params, String method, String encoding, HashMap<String, String> headers, String authorizationMode) {
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

    public TKPDMapParam<String, String> getParams() {
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
        private HashMap<String, Object> params;
        private String method;
        private String encoding;
        private HashMap<String, Object> headers;
        private String authorizationMode;

        public Builder() {
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setParams(HashMap<String, Object> params) {
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

        public Builder setHeaders(HashMap<String, Object> headers) {
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
            TKPDMapParam<String, String> stringMapParams = new TKPDMapParam<>();
            if (this.params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    stringMapParams.put(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            if (this.encoding == null) {
                this.encoding = "urlencoded";
            }
            HashMap<String, String> stringMapHeader = new HashMap<>();
            if (this.headers != null) {
                for (Map.Entry<String, Object> entry : headers.entrySet()) {
                    stringMapHeader.put(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            if (this.authorizationMode == null) {
                this.authorizationMode = "none";
            }
            if (this.url == null) {
                throw new RuntimeException("Url must nnot empty");
            }
            return new ReactNetworkingConfiguration(url, stringMapParams, method, encoding, stringMapHeader, authorizationMode);
        }
    }
}
