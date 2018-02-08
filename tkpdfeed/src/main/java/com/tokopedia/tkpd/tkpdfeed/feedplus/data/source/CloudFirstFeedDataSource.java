package com.tokopedia.tkpd.tkpdfeed.feedplus.data.source;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.base.common.dbManager.RecentProductDbManager;
import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.RecentProductMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.cloud.CloudFeedDataSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.local.LocalFeedDataSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by nisie on 7/3/17.
 */

public class CloudFirstFeedDataSource extends CloudFeedDataSource {

    public CloudFirstFeedDataSource(ApolloClient apolloClient,
                                    FeedListMapper feedListMapper,
                                    FeedResultMapper feedResultMapperCloud,
                                    GlobalCacheManager globalCacheManager) {
        super(apolloClient, feedListMapper, feedResultMapperCloud, globalCacheManager);
    }

    public Observable<FeedResult> getFirstPageFeedsList(RequestParams requestParams) {
        return getFeedsList(requestParams)
                .doOnNext(saveToCache())
                .map(feedResultMapper);
    }

    private Action1<FeedDomain> saveToCache() {
        return new Action1<FeedDomain>() {
            @Override
            public void call(FeedDomain dataFeedDomains) {
                globalCacheManager.setKey(LocalFeedDataSource.KEY_FEED_PLUS);
                globalCacheManager.setValue(
                        CacheUtil.convertModelToString(dataFeedDomains,
                                new TypeToken<FeedDomain>() {
                                }.getType()));
                globalCacheManager.store();
            }
        };
    }


}
