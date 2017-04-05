
package com.tokopedia.inbox.rescenter.discussion.data.pojo.replyvalidation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dispute {

    @SerializedName("dispute_deadline")
    @Expose
    private String disputeDeadline;
    @SerializedName("dispute_cs_is_rejected")
    @Expose
    private int disputeCsIsRejected;
    @SerializedName("dispute_status")
    @Expose
    private int disputeStatus;
    @SerializedName("dispute_is_expired")
    @Expose
    private int disputeIsExpired;
    @SerializedName("dispute_is_call_admin")
    @Expose
    private int disputeIsCallAdmin;

    public String getDisputeDeadline() {
        return disputeDeadline;
    }

    public void setDisputeDeadline(String disputeDeadline) {
        this.disputeDeadline = disputeDeadline;
    }

    public int getDisputeCsIsRejected() {
        return disputeCsIsRejected;
    }

    public void setDisputeCsIsRejected(int disputeCsIsRejected) {
        this.disputeCsIsRejected = disputeCsIsRejected;
    }

    public int getDisputeStatus() {
        return disputeStatus;
    }

    public void setDisputeStatus(int disputeStatus) {
        this.disputeStatus = disputeStatus;
    }

    public int getDisputeIsExpired() {
        return disputeIsExpired;
    }

    public void setDisputeIsExpired(int disputeIsExpired) {
        this.disputeIsExpired = disputeIsExpired;
    }

    public int getDisputeIsCallAdmin() {
        return disputeIsCallAdmin;
    }

    public void setDisputeIsCallAdmin(int disputeIsCallAdmin) {
        this.disputeIsCallAdmin = disputeIsCallAdmin;
    }

}
