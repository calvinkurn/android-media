package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model;

/**
 * @author by nisie on 8/15/17.
 */

public class ReputationBadgeDomain {

    private int level;
    private int set;

    public ReputationBadgeDomain(int level, int set) {
        this.level = level;
        this.set = set;
    }
}
