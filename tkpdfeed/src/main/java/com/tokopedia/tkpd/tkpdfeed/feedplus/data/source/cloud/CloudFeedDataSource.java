package com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.cloud;

import android.content.Context;

import com.apollographql.android.rx.RxApollo;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloWatcher;
import com.google.gson.reflect.TypeToken;
import com.tkpdfeed.feeds.Feeds;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.DataFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.FeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetFeedsUseCase;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author ricoharisin .
 */

public class CloudFeedDataSource {

    private ApolloClient apolloClient;
    private Context context;
    private FeedListMapper feedListMapper;
    private GlobalCacheManager globalCacheManager;
    private FeedResultMapper feedResultMapper;
    private static final String KEY_FEED_PLUS = "FEED_PLUS";

    public CloudFeedDataSource(Context context,
                               ApolloClient apolloClient,
                               FeedListMapper feedListMapper,
                               FeedResultMapper feedResultMapper,
                               GlobalCacheManager globalCacheManager) {

        this.apolloClient = apolloClient;
        this.context = context;
        this.feedListMapper = feedListMapper;
        this.globalCacheManager = globalCacheManager;
        this.feedResultMapper = feedResultMapper;
    }

    public Observable<FeedResult> getFirstPageFeedsList(RequestParams requestParams) {
        return getFeedsList(requestParams)
                .doOnNext(new Action1<FeedDomain>() {
                    @Override
                    public void call(FeedDomain dataFeedDomains) {
                        globalCacheManager.setKey(KEY_FEED_PLUS);
                        globalCacheManager.setValue(
                                CacheUtil.convertListModelToString(dataFeedDomains.getList(),
                                        new TypeToken<List<DataFeedDomain>>() {
                                        }.getType()));
                        globalCacheManager.store();
                    }
                }).map(feedResultMapper);
    }

    public Observable<FeedResult> getNextPageFeedsList(RequestParams requestParams) {
        return getFeedsList(requestParams).map(feedResultMapper);
    }

    private Observable<FeedDomain> getFeedsList(RequestParams requestParams) {
        String cursor = requestParams.getString(GetFeedsUseCase.PARAM_CURSOR, "");
        ApolloWatcher<Feeds.Data> apolloWatcher = apolloClient.newCall(Feeds.builder()
                .userID(requestParams.getInt(GetFeedsUseCase.PARAM_USER_ID, 0))
                .limit(5)
                .cursor(cursor)
                .build()).watcher();

        return RxApollo.from(apolloWatcher).map(feedListMapper);
    }
}
