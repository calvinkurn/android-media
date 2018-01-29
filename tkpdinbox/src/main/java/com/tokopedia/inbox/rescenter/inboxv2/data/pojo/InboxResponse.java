package com.tokopedia.inbox.rescenter.inboxv2.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yfsx on 24/01/18.
 */

public class InboxResponse {
    @SerializedName("actionBy")
    private int actionBy;
    @SerializedName("inboxes")
    private List<InboxDataResponse> inboxes;
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

    public List<InboxDataResponse> getInboxes() {
        return inboxes;
    }

    public void setInboxes(List<InboxDataResponse> inboxes) {
            this.inboxes = inboxes;
        }

}
