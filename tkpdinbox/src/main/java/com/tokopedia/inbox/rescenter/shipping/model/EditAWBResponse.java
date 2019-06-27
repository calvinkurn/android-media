package com.tokopedia.inbox.rescenter.shipping.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditAWBResponse {

    @SerializedName("edit_awb")
    @Expose
    private ResInputValidationResponse resInputValidationResponse;

    public ResInputValidationResponse getResInputValidationResponse() {
        return resInputValidationResponse;
    }

    public void setResInputValidationResponse(ResInputValidationResponse resInputValidationResponse) {
        this.resInputValidationResponse = resInputValidationResponse;
    }
}
