package com.tokopedia.seller.opportunity.viewmodel.opportunitylist;

/**
 * Created by nisie on 3/21/17.
 */

public class DetailCancelRequestViewModel {
    private int cancelRequest;
    private String reasonTime;
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
