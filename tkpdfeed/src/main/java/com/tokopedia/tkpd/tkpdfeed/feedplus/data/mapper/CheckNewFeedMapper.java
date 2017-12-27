package com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper;

import com.tkpdfeed.feeds.FeedCheck;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.CheckFeedDomain;

import rx.functions.Func1;

/**
 * @author by nisie on 8/23/17.
 */

public class CheckNewFeedMapper implements Func1<FeedCheck.Data, CheckFeedDomain> {
    @Override
    public CheckFeedDomain call(FeedCheck.Data data) {
        if (data != null
                && data.checkFeed() != null
                && data.checkFeed().data() != null)
            return new CheckFeedDomain(data.checkFeed().data());
        else
            return new CheckFeedDomain("0");
    }
}
