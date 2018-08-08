package com.tokopedia.session.domain.pojo.token;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by stevenfredian on 5/30/16.
 */

public class ErrorModel {

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("error_description")
    @Expose
    private String errorDescription;

    @SerializedName("state")
    @Expose
    private String state;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
