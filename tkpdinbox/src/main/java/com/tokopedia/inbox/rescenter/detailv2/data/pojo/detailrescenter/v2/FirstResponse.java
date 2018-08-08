package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yfsx on 07/11/17.
 */
public class FirstResponse {

    @SerializedName("buyerRemark")
    private String buyerRemark;

    @SerializedName("attachments")
    private List<AttachmentResponse> attachments;

    public List<AttachmentResponse> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentResponse> attachments) {
        this.attachments = attachments;
    }

    public String getBuyerRemark() {
        return buyerRemark;
    }

    public void setBuyerRemark(String buyerRemark) {
        this.buyerRemark = buyerRemark;
    }
}
