package com.tokopedia.transaction.orders.orderlist.data.surveyresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckResponseHeaders {

    @SerializedName("process_time")
    @Expose
    private Double parseTime;

    @SerializedName("messages")
    @Expose
    private List<String> messages;

    @SerializedName("reason")
    @Expose
    private String reason;

    @SerializedName("error_code")
    @Expose
    private String errorCode;

    public Double getParseTime() {
        return parseTime;
    }

    public List<String> getMessages() {
        return messages;
    }

    public String getReason() {
        return reason;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
