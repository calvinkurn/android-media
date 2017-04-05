
package com.tokopedia.inbox.rescenter.discussion.data.pojo.replysubmit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dispute {

    @SerializedName("dispute_status")
    @Expose
    private Integer disputeStatus;
    @SerializedName("dispute_cs_is_rejected")
    @Expose
    private Integer disputeCsIsRejected;
    @SerializedName("dispute_is_call_admin")
    @Expose
    private Integer disputeIsCallAdmin;
    @SerializedName("dispute_deadline")
    @Expose
    private String disputeDeadline;
    @SerializedName("dispute_is_expired")
    @Expose
    private Integer disputeIsExpired;

    public Integer getDisputeStatus() {
        return disputeStatus;
    }

    public void setDisputeStatus(Integer disputeStatus) {
        this.disputeStatus = disputeStatus;
    }

    public Integer getDisputeCsIsRejected() {
        return disputeCsIsRejected;
    }

    public void setDisputeCsIsRejected(Integer disputeCsIsRejected) {
        this.disputeCsIsRejected = disputeCsIsRejected;
    }

    public Integer getDisputeIsCallAdmin() {
        return disputeIsCallAdmin;
    }

    public void setDisputeIsCallAdmin(Integer disputeIsCallAdmin) {
        this.disputeIsCallAdmin = disputeIsCallAdmin;
    }

    public String getDisputeDeadline() {
        return disputeDeadline;
    }

    public void setDisputeDeadline(String disputeDeadline) {
        this.disputeDeadline = disputeDeadline;
    }

    public Integer getDisputeIsExpired() {
        return disputeIsExpired;
    }

    public void setDisputeIsExpired(Integer disputeIsExpired) {
        this.disputeIsExpired = disputeIsExpired;
    }

}
