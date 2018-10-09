package com.tokopedia.transaction.purchase.detail.model.detail.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 2/6/18. Tokopedia
 */

public class Insurance {

    @SerializedName("type")
    @Expose
    private String insuranceType = "";
    @SerializedName("note")
    @Expose
    private String insuranceNote = "";

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getInsuranceNote() {
        return insuranceNote;
    }

    public void setInsuranceNote(String insuranceNote) {
        this.insuranceNote = insuranceNote;
    }
}
