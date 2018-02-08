package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model;

/**
 * @author by nisie on 8/15/17.
 */

public class PagingDomain {

    private boolean hasNext;
    private boolean hasPrev;

    public PagingDomain(boolean hasNext, boolean hasPrev) {
        this.hasNext = hasNext;
        this.hasPrev = hasPrev;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public boolean isHasPrev() {
        return hasPrev;
    }
}
