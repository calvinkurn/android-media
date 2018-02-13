package com.tokopedia.inbox.rescenter.inboxv2.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yfsx on 24/01/18.
 */

public class InboxSingleDataResponse {
    @SerializedName("actionBy")
    private int actionBy;
    @SerializedName("inboxData")
    private InboxDataResponse inbox;
    @SerializedName("quickFilter")
    private List<FilterResponse> quickFilterResponse;

    public int getActionBy() {
            return actionBy;
        }

    public void setActionBy(int actionBy) {
            this.actionBy = actionBy;
        }

    public InboxDataResponse getInbox() {
        return inbox;
    }

    public void setInbox(InboxDataResponse inbox) {
        this.inbox = inbox;
    }

    public List<FilterResponse> getQuickFilterResponse() {
        return quickFilterResponse;
    }

    public void setQuickFilterResponse(List<FilterResponse> quickFilterResponse) {
        this.quickFilterResponse = quickFilterResponse;
    }
}
