package com.tokopedia.inbox.rescenter.discussion.domain.model;

import java.util.List;

/**
 * Created by nisie on 3/30/17.
 */

public class ActionDiscussionModel {


    private boolean success;
    private String messageError;
    private ActionDiscussionData listDiscussionData;
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

    public List<DiscussionItemData> getListDiscussionData() {
        return listDiscussionData;
    }

    public void setListDiscussionData(List<DiscussionItemData> listDiscussionData) {
        this.listDiscussionData = listDiscussionData;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }


}
