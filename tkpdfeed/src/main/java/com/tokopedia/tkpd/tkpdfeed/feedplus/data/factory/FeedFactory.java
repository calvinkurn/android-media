package com.tokopedia.tkpd.tkpdfeed.feedplus.data.factory;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.CheckNewFeedMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedDetailListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.RecentProductMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.CloudCheckNewFeedDataSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.CloudFirstFeedDataSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.cloud.CloudFeedDataSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.cloud.CloudFeedDetailDataSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.cloud.CloudRecentProductDataSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.local.LocalFeedDataSource;

/**
 * @author ricoharisin .
 */

public class FeedFactory {

    private final CheckNewFeedMapper checkNewFeedMapper;
    private ApolloClient apolloClient;
    private Context context;
    private FeedListMapper feedListMapper;
    private FeedResultMapper feedResultMapperLocal;
    private FeedResultMapper feedResultMapperCloud;
    private GlobalCacheManager globalCacheManager;
    private FeedDetailListMapper feedDetailListMapper;
    private final MojitoService mojitoService;
    private RecentProductMapper recentProductMapper;

    public FeedFactory(Context context,
                       ApolloClient apolloClient,
                       FeedListMapper feedListMapper,
                       FeedResultMapper feedResultMapperCloud,
                       FeedResultMapper feedResultMapperLocal,
                       GlobalCacheManager globalCacheManager,
                       FeedDetailListMapper feedDetailListMapper,
                       MojitoService mojitoService,
                       RecentProductMapper recentProductMapper,
                       CheckNewFeedMapper checkNewFeedMapper) {

        this.apolloClient = apolloClient;
        this.context = context;
        this.feedListMapper = feedListMapper;
        this.feedDetailListMapper = feedDetailListMapper;
        this.feedResultMapperCloud = feedResultMapperCloud;
        this.feedResultMapperLocal = feedResultMapperLocal;
        this.globalCacheManager = globalCacheManager;
        this.mojitoService = mojitoService;
        this.recentProductMapper = recentProductMapper;
        this.checkNewFeedMapper = checkNewFeedMapper;
    }

    public CloudFeedDataSource createCloudFeedDataSource() {
        return new CloudFeedDataSource(apolloClient, feedListMapper, feedResultMapperCloud,
                globalCacheManager);
    }

    public LocalFeedDataSource createLocalFeedDataSource() {
        return new LocalFeedDataSource(globalCacheManager, feedResultMapperLocal);
    }

    public CloudFeedDetailDataSource createCloudDetailFeedDataSource() {
        return new CloudFeedDetailDataSource(context, apolloClient, feedDetailListMapper);
    }

    public CloudFirstFeedDataSource createCloudFirstFeedDataSource() {
        return new CloudFirstFeedDataSource(apolloClient, feedListMapper, feedResultMapperCloud,
                globalCacheManager);
    }

    public CloudRecentProductDataSource createCloudRecentViewedProductSource() {
        return new CloudRecentProductDataSource(globalCacheManager, mojitoService,
                recentProductMapper);
    }

    public CloudCheckNewFeedDataSource createCloudCheckNewFeedDataSource() {
        return new CloudCheckNewFeedDataSource(apolloClient,
                checkNewFeedMapper);
    }
}
