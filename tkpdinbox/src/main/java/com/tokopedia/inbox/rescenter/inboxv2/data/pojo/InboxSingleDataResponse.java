package com.tokopedia.inbox.rescenter.inboxv2.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yfsx on 24/01/18.
 */

public class InboxSingleDataResponse {
    @SerializedName("actionBy")
    private int actionBy;
    @SerializedName("inbox")
    private InboxDataResponse inbox;
    @SerializedName("quickFilter")
    private QuickFilterResponse quickFilter;

    public QuickFilterResponse getQuickFilter() {
        return quickFilter;
    }

    public void setQuickFilter(QuickFilterResponse quickFilter) {
        this.quickFilter = quickFilter;
    }

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
}
