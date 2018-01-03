package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model;

import java.util.List;

/**
 * Created by yfsx on 29/12/17.
 */

public class KolFollowingResultDomain {
    private boolean isCanLoadMore;
    private String lastCursor;
    private List<KolFollowingDomain> kolFollowingDomainList;
    private String buttonText;
    private String buttonApplink;

    public KolFollowingResultDomain(
            boolean isCanLoadMore,
            String lastCursor,
            List<KolFollowingDomain> kolFollowingDomainList,
            String buttonText,
            String buttonApplink) {
        this.isCanLoadMore = isCanLoadMore;
        this.lastCursor = lastCursor;
        this.kolFollowingDomainList = kolFollowingDomainList;
        this.buttonText = buttonText;
        this.buttonApplink = buttonApplink;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getButtonApplink() {
        return buttonApplink;
    }

    public void setButtonApplink(String buttonApplink) {
        this.buttonApplink = buttonApplink;
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
