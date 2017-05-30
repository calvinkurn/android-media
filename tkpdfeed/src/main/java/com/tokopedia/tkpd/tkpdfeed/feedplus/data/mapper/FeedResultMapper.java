package com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper;

import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.DataFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.FeedResult;

import java.util.List;

import rx.functions.Func1;

/**
 * @author ricoharisin .
 */

public class FeedResultMapper implements Func1<List<DataFeedDomain>, FeedResult> {

    private int dataSource;

    public FeedResultMapper(int dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public FeedResult call(List<DataFeedDomain> dataFeedDomains) {
        return new FeedResult(dataFeedDomains, dataSource);
    }
}
