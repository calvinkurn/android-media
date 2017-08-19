
package com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inbox;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InboxReputationPojo {

    @SerializedName("inbox_reputation")
    @Expose
    private List<InboxReputation> inboxReputation = null;
    @SerializedName("notification")
    @Expose
    private Notification notification;
    @SerializedName("paging")
    @Expose
    private Paging paging;

    public List<InboxReputation> getInboxReputation() {
        return inboxReputation;
    }

    public void setInboxReputation(List<InboxReputation> inboxReputation) {
        this.inboxReputation = inboxReputation;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

}
