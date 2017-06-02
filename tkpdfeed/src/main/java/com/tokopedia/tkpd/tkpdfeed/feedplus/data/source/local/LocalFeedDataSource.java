package com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.local;


import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.DataFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.FeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.FeedResult;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author ricoharisin .
 */

public class LocalFeedDataSource {

    private static final String KEY_FEED_PLUS = "FEED_PLUS";
    private static final int CACHE_DURATION = 10;
    private GlobalCacheManager cacheManager;
    private FeedResultMapper feedResultMapper;

    public LocalFeedDataSource(GlobalCacheManager globalCacheManager,
                               FeedResultMapper feedResultMapper) {
        this.cacheManager = globalCacheManager;
        this.feedResultMapper = feedResultMapper;
    }

    public Observable<FeedResult> getFeeds() {

        return Observable.fromCallable(new Callable<FeedDomain>() {
           @Override
           public FeedDomain call() throws Exception {
               cacheManager = new GlobalCacheManager();
               cacheManager.setKey(KEY_FEED_PLUS);
               cacheManager.setCacheDuration(CACHE_DURATION);
               List<DataFeedDomain> list = CacheUtil.convertStringToListModel(cacheManager.getValueString(KEY_FEED_PLUS),
                       new TypeToken<List<DataFeedDomain>>() {
                       }.getType());
               return new FeedDomain(list, true);
           }
       }).doOnNext(new Action1<FeedDomain>() {
            @Override
            public void call(FeedDomain dataFeedDomains) {
                if (dataFeedDomains.getList() == null || dataFeedDomains.getList().size() == 0) {
                    throw new RuntimeException("No Data");
                }
            }
        }).map(feedResultMapper);

    }
}
