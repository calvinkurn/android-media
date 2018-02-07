package com.tokopedia.tkpd.tkpdfeed.feedplus.data.source;

import com.apollographql.android.rx.RxApollo;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloWatcher;
import com.tkpdfeed.feeds.FeedCheck;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.CheckNewFeedMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.CheckFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.CheckNewFeedUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetFeedsUseCase;

import rx.Observable;

/**
 * @author by nisie on 8/23/17.
 */

public class CloudCheckNewFeedDataSource {

    private final ApolloClient apolloClient;
    private final CheckNewFeedMapper checkNewFeedMapper;

    public CloudCheckNewFeedDataSource(ApolloClient apolloClient,
                                       CheckNewFeedMapper checkNewFeedMapper) {
        this.apolloClient = apolloClient;
        this.checkNewFeedMapper = checkNewFeedMapper;

    }

    public Observable<CheckFeedDomain> checkNewFeed(RequestParams requestParams) {
        ApolloWatcher<FeedCheck.Data> apolloWatcher = apolloClient.newCall(FeedCheck.builder()
                .userID(requestParams.getInt(GetFeedsUseCase.PARAM_USER_ID, 0))
                .cursor(requestParams.getString(CheckNewFeedUseCase.PARAM_CURSOR, ""))
                .build()).watcher();

        return RxApollo.from(apolloWatcher).map(checkNewFeedMapper);
    }
}
