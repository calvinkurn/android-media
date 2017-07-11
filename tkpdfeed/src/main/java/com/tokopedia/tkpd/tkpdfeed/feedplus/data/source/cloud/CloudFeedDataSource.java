package com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.cloud;

import android.content.Context;

import com.apollographql.android.rx.RxApollo;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloWatcher;
import com.tkpdfeed.feeds.Feeds;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetFeedsUseCase;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public class CloudFeedDataSource {

    private ApolloClient apolloClient;
    private Context context;
    private FeedListMapper feedListMapper;
    protected GlobalCacheManager globalCacheManager;
    protected FeedResultMapper feedResultMapper;

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

    public Observable<FeedResult> getNextPageFeedsList(RequestParams requestParams) {
        return getFeedsList(requestParams).map(feedResultMapper);
    }

    protected Observable<FeedDomain> getFeedsList(RequestParams requestParams) {
        String cursor = requestParams.getString(GetFeedsUseCase.PARAM_CURSOR, "");
        ApolloWatcher<Feeds.Data> apolloWatcher = apolloClient.newCall(Feeds.builder()
                .userID(requestParams.getInt(GetFeedsUseCase.PARAM_USER_ID, 0))
                .page(requestParams.getInt(GetFeedsUseCase.PARAM_PAGE,0))
                .limit(3)
                .cursor(cursor)
                .build()).watcher();

        return RxApollo.from(apolloWatcher).map(feedListMapper);
    }
}
