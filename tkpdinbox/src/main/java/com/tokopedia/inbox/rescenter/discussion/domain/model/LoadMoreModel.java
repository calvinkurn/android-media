package com.tokopedia.inbox.rescenter.discussion.domain.model;

import java.util.List;

/**
 * Created by nisie on 3/31/17.
 */

public class LoadMoreModel {

    private boolean success;
    private String messageError;
    private List<LoadMoreItemData> listDiscussionData;
    private boolean canLoadMore;
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

    public List<LoadMoreItemData> getListDiscussionData() {
        return listDiscussionData;
    }

    public void setListDiscussionData(List<LoadMoreItemData> listDiscussionData) {
        this.listDiscussionData = listDiscussionData;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public boolean canLoadMore() {
        return canLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }
}
