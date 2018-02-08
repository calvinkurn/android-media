package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yfsx on 12/12/17.
 */

public class AttachmentUserResponse {

    @SerializedName("actionBy")
    private int actionBy;

    @SerializedName("attachments")
    private List<AttachmentResponse> attachments;

    public int getActionBy() {
        return actionBy;
    }

    public void setActionBy(int actionBy) {
        this.actionBy = actionBy;
    }

    public List<AttachmentResponse> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentResponse> attachments) {
        this.attachments = attachments;
    }
}
