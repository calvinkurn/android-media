package com.tokopedia.inbox.rescenter.discussion.domain.model.replyvalidation;

/**
 * Created by nisie on 4/3/17.
 */

public class ReplyDiscussionValidationModel {

    private boolean success;
    private String messageError;
    private ReplyDiscussionData replyDiscussionData;
    private int responseCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public ReplyDiscussionData getReplyDiscussionData() {
        return replyDiscussionData;
    }

    public void setReplyDiscussionData(ReplyDiscussionData replyDiscussionData) {
        this.replyDiscussionData = replyDiscussionData;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
