
package com.tokopedia.transaction.orders.orderdetails.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestCancelInfo {

    @SerializedName("isRequestCancelAvail")
    @Expose
    private Boolean isRequestCancelAvail;

    @SerializedName("isRequestedCancel")
    @Expose
    private Boolean isRequestedCancel;

    @SerializedName("requestCancelMinTime")
    @Expose
    private String requestCancelMinTime;

    @SerializedName("requestCancelNote")
    @Expose
    private String requestCancelNote;

    public RequestCancelInfo(Boolean isRequestCancelAvail, Boolean isRequestedCancel, String requestCancelMinTime, String requestCancelNote) {
        this.isRequestCancelAvail = isRequestCancelAvail;
        this.isRequestedCancel = isRequestedCancel;
        this.requestCancelMinTime = requestCancelMinTime;
        this.requestCancelNote = requestCancelNote;
    }

    public Boolean getIsRequestCancelAvail() {
        return isRequestCancelAvail;
    }

    public Boolean getIsRequestedCancel() {
        return isRequestedCancel;
    }

    public String getRequestCancelMinTime() {
        return requestCancelMinTime;
    }

    public String getRequestCancelNote() {
        return requestCancelNote;
    }


    @Override
    public String toString() {
        return "[RequestCancelInfo:{" + " " +
                "isRequestCancelAvail=" + isRequestCancelAvail + " " +
                "isRequestedCancel=" + isRequestedCancel + " " +
                "requestCancelMinTime=" + requestCancelMinTime + " " +
                "requestCancelNote=" + requestCancelNote
                + "}]";
    }
}
