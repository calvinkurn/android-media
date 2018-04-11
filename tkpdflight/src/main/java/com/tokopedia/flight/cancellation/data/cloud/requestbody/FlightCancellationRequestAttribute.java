package com.tokopedia.flight.cancellation.data.cloud.requestbody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by furqan on 11/04/18.
 */

public class FlightCancellationRequestAttribute {
    @SerializedName("invoice_id")
    @Expose
    private String invoiceId;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("attachments")
    @Expose
    private List<String> attachments;
    @SerializedName("estimated_refund")
    @Expose
    private long estimatedRefund;
    @SerializedName("details")
    @Expose
    private List<FlightCancellationDetailRequestBody> details;

    public FlightCancellationRequestAttribute() {
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public long getEstimatedRefund() {
        return estimatedRefund;
    }

    public void setEstimatedRefund(long estimatedRefund) {
        this.estimatedRefund = estimatedRefund;
    }

    public List<FlightCancellationDetailRequestBody> getDetails() {
        return details;
    }

    public void setDetails(List<FlightCancellationDetailRequestBody> details) {
        this.details = details;
    }
}
