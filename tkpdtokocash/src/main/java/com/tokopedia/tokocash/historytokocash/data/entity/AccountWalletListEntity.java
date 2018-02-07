package com.tokopedia.tokocash.historytokocash.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 11/6/17.
 */

public class AccountWalletListEntity {

    @SerializedName("client_id")
    @Expose
    private String client_id;
    @SerializedName("client_name")
    @Expose
    private String client_name;
    @SerializedName("identifier")
    @Expose
    private String identifier;
    @SerializedName("identifier_type")
    @Expose
    private String identifier_type;
    @SerializedName("img_url")
    @Expose
    private String img_url;
    @SerializedName("auth_date_fmt")
    @Expose
    private String auth_date_fmt;
    @SerializedName("refresh_token")
    @Expose
    private String refresh_token;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier_type() {
        return identifier_type;
    }

    public void setIdentifier_type(String identifier_type) {
        this.identifier_type = identifier_type;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getAuth_date_fmt() {
        return auth_date_fmt;
    }

    public void setAuth_date_fmt(String auth_date_fmt) {
        this.auth_date_fmt = auth_date_fmt;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
