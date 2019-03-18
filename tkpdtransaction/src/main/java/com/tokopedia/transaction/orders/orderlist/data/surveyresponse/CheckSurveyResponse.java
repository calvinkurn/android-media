package com.tokopedia.transaction.orders.orderlist.data.surveyresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckSurveyResponse {

    @SerializedName("check_eligible_survey")
    @Expose
    private CheckResponseData checkResponseData;


    public CheckResponseData getCheckResponseData() {
        return checkResponseData;
    }
}
