package com.tokopedia.tkpd.tkpdfeed.feedplus.data.factory;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.tokopedia.core.base.common.dbManager.RecentProductDbManager;
import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.RecentProductMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.CloudFirstFeedDataSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.cloud.CloudFeedDataSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.cloud.CloudRecentProductDataSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.local.LocalFeedDataSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedDetailListMapper;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.cloud.CloudFeedDetailDataSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedResultMapper;

/**
 * @author ricoharisin .
 */

public class FeedFactory {

    private ApolloClient apolloClient;
    private Context context;
    private FeedListMapper feedListMapper;
    private FeedResultMapper feedResultMapperLocal;
    private FeedResultMapper feedResultMapperCloud;
    private GlobalCacheManager globalCacheManager;
    private FeedDetailListMapper feedDetailListMapper;
    private RecentProductDbManager recentProductDbManager;
    private final MojitoService mojitoService;
    private RecentProductMapper recentProductMapper;

    public FeedFactory(Context context,
                       ApolloClient apolloClient,
                       FeedListMapper feedListMapper,
                       FeedResultMapper feedResultMapperCloud,
                       FeedResultMapper feedResultMapperLocal,
                       GlobalCacheManager globalCacheManager,
                       FeedDetailListMapper feedDetailListMapper,
                       RecentProductDbManager recentProductDbManager,
                       MojitoService mojitoService,
                       RecentProductMapper recentProductMapper) {

        this.apolloClient = apolloClient;
        this.context = context;
        this.feedListMapper = feedListMapper;
        this.feedDetailListMapper = feedDetailListMapper;
        this.feedResultMapperCloud = feedResultMapperCloud;
        this.feedResultMapperLocal = feedResultMapperLocal;
        this.globalCacheManager = globalCacheManager;
        this.recentProductDbManager = recentProductDbManager;
        this.mojitoService = mojitoService;
        this.recentProductMapper = recentProductMapper;
    }

    public CloudFeedDataSource createCloudFeedDataSource() {
        return new CloudFeedDataSource(context, apolloClient, feedListMapper, feedResultMapperCloud, globalCacheManager);
    }

    public LocalFeedDataSource createLocalFeedDataSource() {
        return new LocalFeedDataSource(globalCacheManager, feedResultMapperLocal);
    }

    public CloudFeedDetailDataSource createCloudDetailFeedDataSource() {
        return new CloudFeedDetailDataSource(context, apolloClient, feedDetailListMapper);
    }

    public CloudFirstFeedDataSource createCloudFirstFeedDataSource() {
        return new CloudFirstFeedDataSource(context, apolloClient,
                feedListMapper, feedResultMapperCloud, globalCacheManager,
                recentProductDbManager, mojitoService, recentProductMapper);
    }

    public CloudRecentProductDataSource createCloudRecentViewedProductSource() {
        return new CloudRecentProductDataSource(context, recentProductDbManager, mojitoService, recentProductMapper);
    }
}
