package com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper;

import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.DataFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.FeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.FeedResult;

import java.util.List;

import rx.functions.Func1;

/**
 * @author ricoharisin .
 */

public class FeedResultMapper implements Func1<FeedDomain, FeedResult> {

    private int dataSource;

    public FeedResultMapper(int dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public FeedResult call(FeedDomain feedDomain) {
        return new FeedResult(feedDomain.getList(), dataSource, feedDomain.isHasNext());
    }
}
