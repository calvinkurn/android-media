package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model;

/**
 * @author by nisie on 8/23/17.
 */

public class CheckFeedDomain {
    private final int totalData;

    public CheckFeedDomain(int totalData) {
        this.totalData = totalData;
    }

    public int getTotalData() {
        return totalData;
    }
}
