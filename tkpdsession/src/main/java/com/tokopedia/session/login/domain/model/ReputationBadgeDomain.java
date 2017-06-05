package com.tokopedia.session.login.domain.model;

/**
 * @author by nisie on 5/26/17.
 */

public class ReputationBadgeDomain {

    private final int level;
    private final int set;

    public ReputationBadgeDomain(int level, int set) {
        this.level = level;
        this.set = set;
    }
}
