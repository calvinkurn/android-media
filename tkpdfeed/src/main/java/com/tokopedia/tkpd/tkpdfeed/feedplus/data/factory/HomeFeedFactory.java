package com.tokopedia.tkpd.tkpdfeed.feedplus.data.factory;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.CheckNewFeedMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedDetailListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.HomeFeedMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.RecentProductMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.cloud.HomeFeedDataSource;

/**
 * Created by henrypriyono on 12/29/17.
 */

public class HomeFeedFactory {

    private final ApolloClient apolloClient;
    private final HomeFeedMapper homeFeedMapper;
    private final FeedResultMapper feedResultMapper;

    public HomeFeedFactory(ApolloClient apolloClient,
                           HomeFeedMapper homeFeedMapper,
                           FeedResultMapper feedResultMapper) {

        this.apolloClient = apolloClient;
        this.homeFeedMapper = homeFeedMapper;
        this.feedResultMapper = feedResultMapper;
    }

    public HomeFeedDataSource createHomeFeedDataSource() {
        return new HomeFeedDataSource(apolloClient, homeFeedMapper, feedResultMapper);
    }
}
