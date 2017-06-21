package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model;

import java.util.List;

import javax.annotation.Nullable;

/**
 * @author ricoharisin .
 */

public class FeedDomain {

    private final
    @Nullable
    List<DataFeedDomain> listFeed;

    private final
    @Nullable
    List<DataInspirationDomain> listInspiration;

    private final
    @Nullable
    boolean hasNext;

    public FeedDomain(@Nullable List<DataFeedDomain> listFeed,
                      @Nullable List<DataInspirationDomain> listInspiration,
                      @Nullable boolean hasNext) {
        this.listFeed = listFeed;
        this.listInspiration = listInspiration;
        this.hasNext = hasNext;
    }

    @Nullable
    public List<DataFeedDomain> getListFeed() {
        return listFeed;
    }

    @Nullable
    public boolean isHasNext() {
        return hasNext;
    }

    @Nullable
    public List<DataInspirationDomain> getListInspiration() {
        return listInspiration;
    }
}
