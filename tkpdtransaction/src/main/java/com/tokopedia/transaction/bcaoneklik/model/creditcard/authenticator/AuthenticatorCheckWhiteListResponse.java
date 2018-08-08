package com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kris on 10/11/17. Tokopedia
 */

public class AuthenticatorCheckWhiteListResponse {

    @SerializedName("data")
    @Expose
    private List<AuthenticatorCheckWhitelistData> datas;

    @SerializedName("status_code")
    @Expose
    private int statusCode;

    @SerializedName("message")
    @Expose
    private String message;

    public List<AuthenticatorCheckWhitelistData> getDatas() {
        return datas;
    }

    public void setDatas(List<AuthenticatorCheckWhitelistData> datas) {
        this.datas = datas;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
