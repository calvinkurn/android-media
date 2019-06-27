package com.tokopedia.transaction.orders.orderlist.data.surveyresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckResponseData {

    @SerializedName("header")
    @Expose
    private CheckResponseHeaders checkResponseHeaders;

    @SerializedName("data")
    @Expose
    private CheckResponseSurveyData checkResponseSurveyData;

    public CheckResponseHeaders getCheckResponseHeaders() {
        return checkResponseHeaders;
    }

    public CheckResponseSurveyData getCheckResponseSurveyData() {
        return checkResponseSurveyData;
    }
}
