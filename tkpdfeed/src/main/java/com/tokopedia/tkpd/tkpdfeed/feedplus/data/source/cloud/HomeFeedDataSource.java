package com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.cloud;

import com.apollographql.android.rx.RxApollo;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloWatcher;
import com.tkpdfeed.feeds.FeedQuery;
import com.tkpdfeed.feeds.HomeFeedQuery;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.HomeFeedMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetFeedsUseCase;

import rx.Observable;

/**
 * Created by henrypriyono on 12/29/17.
 */

public class HomeFeedDataSource {
    private static final int PRODUCT_PER_CARD_LIMIT = 4;
    private ApolloClient apolloClient;
    private HomeFeedMapper homeFeedMapper;
    protected FeedResultMapper feedResultMapper;

    public HomeFeedDataSource(ApolloClient apolloClient,
                               HomeFeedMapper homeFeedMapper,
                               FeedResultMapper feedResultMapper) {

        this.apolloClient = apolloClient;
        this.homeFeedMapper = homeFeedMapper;
        this.feedResultMapper = feedResultMapper;
    }

    public Observable<FeedResult> getHomeFeeds(RequestParams requestParams) {
        return getHomeFeedsList(requestParams).map(feedResultMapper);
    }

    private Observable<FeedDomain> getHomeFeedsList(RequestParams requestParams) {
        String cursor = requestParams.getString(GetFeedsUseCase.PARAM_CURSOR, "");
        ApolloWatcher<HomeFeedQuery.Data> apolloWatcher = apolloClient.newCall(HomeFeedQuery.builder()
                .userID(requestParams.getInt(GetFeedsUseCase.PARAM_USER_ID, 0))
                .limit(PRODUCT_PER_CARD_LIMIT)
                .cursor(cursor)
                .build()).watcher();

        return RxApollo.from(apolloWatcher).map(homeFeedMapper);
    }
}
