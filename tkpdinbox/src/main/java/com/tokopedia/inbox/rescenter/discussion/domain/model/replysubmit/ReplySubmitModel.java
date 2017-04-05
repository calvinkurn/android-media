package com.tokopedia.inbox.rescenter.discussion.domain.model.replysubmit;

/**
 * Created by nisie on 4/3/17.
 */

public class ReplySubmitModel {

    private boolean success;
    private String messageError;
    private ReplySubmitData replySubmitData;
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

    public ReplySubmitData getReplySubmitData() {
        return replySubmitData;
    }

    public void setReplySubmitData(ReplySubmitData replySubmitData) {
        this.replySubmitData = replySubmitData;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

}
