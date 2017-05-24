package com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.source.local;


import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedResultMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.DataFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.FeedResult;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author ricoharisin .
 */

public class LocalFeedDataSource {

    private static final String KEY_FEED_PLUS = "FEED_PLUS";
    private GlobalCacheManager cacheManager;
    private FeedResultMapper feedResultMapper;

    public LocalFeedDataSource(GlobalCacheManager globalCacheManager,
                               FeedResultMapper feedResultMapper) {
        this.cacheManager = globalCacheManager;
        this.feedResultMapper = feedResultMapper;
    }

    public Observable<FeedResult> getFeeds() {

        return Observable.fromCallable(new Callable<List<DataFeedDomain>>() {
           @Override
           public List<DataFeedDomain> call() throws Exception {
               cacheManager = new GlobalCacheManager();
               cacheManager.setKey(KEY_FEED_PLUS);
               return CacheUtil.convertStringToListModel(cacheManager.getValueString(KEY_FEED_PLUS),
                       new TypeToken<List<DataFeedDomain>>(){}.getType());
           }
       }).doOnNext(new Action1<List<DataFeedDomain>>() {
            @Override
            public void call(List<DataFeedDomain> dataFeedDomains) {
                if (dataFeedDomains == null || dataFeedDomains.size() == 0) {
                    throw new RuntimeException("No Data");
                }
            }
        }).map(feedResultMapper);

    }
}
