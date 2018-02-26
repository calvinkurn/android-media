package com.tokopedia.tokocash.qrpayment.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 2/22/18.
 */

public class TokoCashEntity {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("data")
    @Expose
    private BalanceTokoCashEntity data;
    @SerializedName("error")
    @Expose
    private String errorMessage;
    @SerializedName("status")
    @Expose
    private String statusMessage;
    @SerializedName("code")
    @Expose
    private int responseCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public BalanceTokoCashEntity getData() {
        return data;
    }

    public void setData(BalanceTokoCashEntity data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
