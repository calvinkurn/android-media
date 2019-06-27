package com.tokopedia.transaction.orders.orderlist.data.surveyresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckResponseSurveyData {

    @SerializedName("status_insert")
    @Expose
    private String statusInsert;

    @SerializedName("is_eligible")
    @Expose
    private boolean isEligible;

    @SerializedName("success")
    @Expose
    private boolean isSuccess;

    public String getStatusInsert() {
        return statusInsert;
    }

    public boolean isEligible() {
        return isEligible;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
