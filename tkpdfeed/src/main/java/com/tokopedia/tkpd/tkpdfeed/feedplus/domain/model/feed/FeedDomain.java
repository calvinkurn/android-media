package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed;

import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.inspiration.DataInspirationDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.recentview.RecentViewProductDomain;

import java.util.List;

import javax.annotation.Nullable;

/**
 * @author ricoharisin .
 */

public class FeedDomain {
    @Nullable
    private final List<DataFeedDomain> listFeed;

    @Nullable
    private final boolean hasNext;

    @Nullable
    private List<RecentViewProductDomain> recentProduct;

    public FeedDomain(@Nullable List<DataFeedDomain> listFeed,
                      boolean hasNext) {
        this.listFeed = listFeed;
        this.hasNext = hasNext;
    }

    @Nullable
    public List<DataFeedDomain> getListFeed() {
        return listFeed;
    }

    public boolean isHasNext() {
        return hasNext;
    }

//    @Nullable
//    public List<DataInspirationDomain> getListInspiration() {
//        return listInspiration;
//    }

    public void setRecentProduct(@Nullable List<RecentViewProductDomain> recentProduct) {
        this.recentProduct = recentProduct;
    }

    @Nullable
    public List<RecentViewProductDomain> getRecentProduct() {
        return recentProduct;
    }
}
