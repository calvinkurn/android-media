package com.tokopedia.flight.cancellation.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by furqan on 11/04/18.
 */

public class CancellationRequestAttribute {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("journey_id")
    @Expose
    private int journeyId;
    @SerializedName("passenger_id")
    @Expose
    private int passengerId;
    @SerializedName("estimated_refund")
    @Expose
    private long estimatedRefund;
    @SerializedName("real_refund")
    @Expose
    private long realRefund;
    @SerializedName("agent_fee")
    @Expose
    private long agent_fee;
    @SerializedName("tkpd_fee")
    @Expose
    private long tkpdFee;

    public CancellationRequestAttribute() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(int journeyId) {
        this.journeyId = journeyId;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public long getEstimatedRefund() {
        return estimatedRefund;
    }

    public void setEstimatedRefund(long estimatedRefund) {
        this.estimatedRefund = estimatedRefund;
    }

    public long getRealRefund() {
        return realRefund;
    }

    public void setRealRefund(long realRefund) {
        this.realRefund = realRefund;
    }

    public long getAgent_fee() {
        return agent_fee;
    }

    public void setAgent_fee(long agent_fee) {
        this.agent_fee = agent_fee;
    }

    public long getTkpdFee() {
        return tkpdFee;
    }

    public void setTkpdFee(long tkpdFee) {
        this.tkpdFee = tkpdFee;
    }
}
