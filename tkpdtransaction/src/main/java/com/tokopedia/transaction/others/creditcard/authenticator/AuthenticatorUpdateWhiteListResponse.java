package com.tokopedia.transaction.others.creditcard.authenticator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kris on 10/11/17. Tokopedia
 */

public class AuthenticatorUpdateWhiteListResponse {

    @SerializedName("data")
    @Expose
    private List<AuthenticatorUpdateData> datas;

    @SerializedName("status_code")
    @Expose
    private int statusCode;

    @SerializedName("message")
    @Expose
    private String message;

    public List<AuthenticatorUpdateData> getDatas() {
        return datas;
    }

    public void setDatas(List<AuthenticatorUpdateData> datas) {
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
