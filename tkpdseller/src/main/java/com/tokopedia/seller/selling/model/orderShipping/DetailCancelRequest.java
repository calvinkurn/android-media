package com.tokopedia.seller.selling.model.orderShipping;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by zulfikarrahman on 12/1/16.
 */

@Parcel
public class DetailCancelRequest {
    @SerializedName("cancel_request")
    @Expose
    Integer cancelRequest;
    @SerializedName("reason_time")
    @Expose
    String reasonTime;
    @SerializedName("reason")
    @Expose
    String reason;

    /**
     *
     * @return
     * The cancelRequest
     */
    public Integer getCancelRequest() {
        return cancelRequest;
    }

    /**
     *
     * @param cancelRequest
     * The cancel_request
     */
    public void setCancelRequest(Integer cancelRequest) {
        this.cancelRequest = cancelRequest;
    }

    /**
     *
     * @return
     * The reasonTime
     */
    public String getReasonTime() {
        return reasonTime;
    }

    /**
     *
     * @param reasonTime
     * The reason_time
     */
    public void setReasonTime(String reasonTime) {
        this.reasonTime = reasonTime;
    }

    /**
     *
     * @return
     * The reason
     */
    public String getReason() {
        return reason;
    }

    /**
     *
     * @param reason
     * The reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

}
