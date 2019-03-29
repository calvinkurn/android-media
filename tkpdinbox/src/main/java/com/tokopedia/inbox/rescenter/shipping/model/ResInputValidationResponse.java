package com.tokopedia.inbox.rescenter.shipping.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResInputValidationResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("messageError")
    @Expose
    private List<String> messageError;

    @SerializedName("data")
    @Expose
    private ResInputResponseData resInputResponseData;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getMessageError() {
        return messageError;
    }

    public void setMessageError(List<String> messageError) {
        this.messageError = messageError;
    }

    public ResInputResponseData getResInputResponseData() {
        return resInputResponseData;
    }

    public void setResInputResponseData(ResInputResponseData resInputResponseData) {
        this.resInputResponseData = resInputResponseData;
    }
}
