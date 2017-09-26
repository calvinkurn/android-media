package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model;

import java.util.List;

/**
 * @author by nisie on 8/14/17.
 */

public class InboxReputationDomain {

    private List<InboxReputationItemDomain> inboxReputation = null;

    private PagingDomain paging;

    public InboxReputationDomain(List<InboxReputationItemDomain> inboxReputation, PagingDomain paging) {
        this.inboxReputation = inboxReputation;
        this.paging = paging;
    }

    public List<InboxReputationItemDomain> getInboxReputation() {
        return inboxReputation;
    }

    public PagingDomain getPaging() {
        return paging;
    }
}
