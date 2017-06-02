package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model;

import java.util.List;

import javax.annotation.Nullable;

/**
 * @author ricoharisin .
 */

public class FeedDomain {

    private final @Nullable
    List<DataFeedDomain> list;

    private final @Nullable boolean hasNext;

    public FeedDomain(@Nullable List<DataFeedDomain> list, @Nullable boolean hasNext) {
        this.list = list;
        this.hasNext = hasNext;
    }

    @Nullable
    public List<DataFeedDomain> getList() {
        return list;
    }

    @Nullable
    public boolean isHasNext() {
        return hasNext;
    }
}
