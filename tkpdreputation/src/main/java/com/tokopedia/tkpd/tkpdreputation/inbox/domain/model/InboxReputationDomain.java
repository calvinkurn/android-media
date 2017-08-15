package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model;

import java.util.List;

/**
 * @author by nisie on 8/14/17.
 */

public class InboxReputationDomain {

    private List<InboxReputationItemDomain> inboxReputation = null;

    private NotificationDomain notification;

    private PagingDomain paging;

    public InboxReputationDomain(List<InboxReputationItemDomain> inboxReputation,
                                 NotificationDomain notification, PagingDomain paging) {
        this.inboxReputation = inboxReputation;
        this.notification = notification;
        this.paging = paging;
    }

    public List<InboxReputationItemDomain> getInboxReputation() {
        return inboxReputation;
    }

    public NotificationDomain getNotification() {
        return notification;
    }

    public PagingDomain getPaging() {
        return paging;
    }
}
