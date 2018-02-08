package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model;

/**
 * @author by nisie on 11/3/17.
 */

public class LikeKolDomain {
    private final boolean isSuccess;

    public LikeKolDomain(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
