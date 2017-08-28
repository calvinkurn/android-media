
package com.tokopedia.seller.transaction.neworder.data.source.cloud.model.neworder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailCancelRequest {

    @SerializedName("cancel_request")
    @Expose
    private int cancelRequest;
    @SerializedName("reason_time")
    @Expose
    private String reasonTime;
    @SerializedName("reason")
    @Expose
    private String reason;

    public int getCancelRequest() {
        return cancelRequest;
    }

    public void setCancelRequest(int cancelRequest) {
        this.cancelRequest = cancelRequest;
    }

    public String getReasonTime() {
        return reasonTime;
    }

    public void setReasonTime(String reasonTime) {
        this.reasonTime = reasonTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
