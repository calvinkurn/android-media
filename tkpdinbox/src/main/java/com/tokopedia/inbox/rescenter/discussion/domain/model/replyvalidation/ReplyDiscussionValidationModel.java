package com.tokopedia.inbox.rescenter.discussion.domain.model.replyvalidation;

import com.tokopedia.inbox.rescenter.discussion.domain.model.reply.ReplyDiscussionDomainData;

/**
 * Created by nisie on 4/3/17.
 */

public class ReplyDiscussionValidationModel {

    private boolean success;
    private String messageError;
    private ReplyDiscussionDomainData replyDiscussionData;
    private int responseCode;
    private String postKey;
    private String token;

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

    public ReplyDiscussionDomainData getReplyDiscussionData() {
        return replyDiscussionData;
    }

    public void setReplyDiscussionData(ReplyDiscussionDomainData replyDiscussionData) {
        this.replyDiscussionData = replyDiscussionData;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
