package com.tokopedia.flight.orderlist.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author by furqan on 30/04/18.
 */

public class CancellationEntity {
    @SerializedName("refund_id")
    @Expose
    private int refundId;
    @SerializedName("details")
    @Expose
    private ArrayList<CancellationDetailsAttribute> details;
    @SerializedName("create_time")
    @Expose
    private String createTime;
    @SerializedName("estimated_refund")
    @Expose
    private String estimatedRefund;
    @SerializedName("estimated_refund_numeric")
    @Expose
    private long estimatedRefundNumeric;
    @SerializedName("real_refund")
    @Expose
    private String realRefund;
    @SerializedName("real_refund_numeric")
    @Expose
    private long realRefundNumeric;
    @SerializedName("status")
    @Expose
    private int status;

    public int getRefundId() {
        return refundId;
    }

    public ArrayList<CancellationDetailsAttribute> getDetails() {
        return details;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getEstimatedRefund() {
        return estimatedRefund;
    }

    public long getEstimatedRefundNumeric() {
        return estimatedRefundNumeric;
    }

    public String getRealRefund() {
        return realRefund;
    }

    public long getRealRefundNumeric() {
        return realRefundNumeric;
    }

    public int getStatus() {
        return status;
    }
}
