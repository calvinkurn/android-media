package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model;

import java.util.List;

/**
 * Created by yfsx on 29/12/17.
 */

public class KolFollowingResultDomain {
    private boolean isCanLoadMore;
    private String lastCursor;
    private List<KolFollowingDomain> kolFollowingDomainList;

    public KolFollowingResultDomain(boolean isCanLoadMore, String lastCursor, List<KolFollowingDomain> kolFollowingDomainList) {
        this.isCanLoadMore = isCanLoadMore;
        this.lastCursor = lastCursor;
        this.kolFollowingDomainList = kolFollowingDomainList;
    }

    public boolean isCanLoadMore() {
        return isCanLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        isCanLoadMore = canLoadMore;
    }

    public String getLastCursor() {
        return lastCursor;
    }

    public void setLastCursor(String lastCursor) {
        this.lastCursor = lastCursor;
    }

    public List<KolFollowingDomain> getKolFollowingDomainList() {
        return kolFollowingDomainList;
    }

    public void setKolFollowingDomainList(List<KolFollowingDomain> kolFollowingDomainList) {
        this.kolFollowingDomainList = kolFollowingDomainList;
    }
}
