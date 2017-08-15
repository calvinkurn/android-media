package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model;

/**
 * @author by nisie on 8/15/17.
 */

public class DebugLockingStatusDomain {

    private int previousStatus;
    private int nextStatus;
    private int dayLeft;

    public DebugLockingStatusDomain(int previousStatus, int nextStatus, int dayLeft) {
        this.previousStatus = previousStatus;
        this.nextStatus = nextStatus;
        this.dayLeft = dayLeft;
    }
}
