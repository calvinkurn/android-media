package com.tokopedia.inbox.rescenter.discussion.view.viewmodel;

import java.util.List;

/**
 * Created by nisie on 3/30/17.
 */

public class SendReplyDiscussionParam {
    String message;
    List<AttachmentViewModel> attachment;
    private String resolutionId;
    private int flagReceived;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AttachmentViewModel> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<AttachmentViewModel> attachment) {
        this.attachment = attachment;
    }

    public String getResolutionId() {
        return resolutionId;
    }

    public void setResolutionId(String resolutionId) {
        this.resolutionId = resolutionId;
    }

    public void setFlagReceived(int flagReceived) {
        this.flagReceived = flagReceived;
    }

    public int getFlagReceived() {
        return flagReceived;
    }
}
