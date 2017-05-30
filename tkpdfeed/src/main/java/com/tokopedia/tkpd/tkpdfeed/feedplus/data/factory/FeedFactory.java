package com.tokopedia.tkpd.tkpdfeed.feedplus.data.factory;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.cloud.CloudFeedDataSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.local.LocalFeedDataSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedDetailListMapper;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.CloudFeedDetailDataSource;
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

    public FeedFactory(Context context,
                       ApolloClient apolloClient,
                       FeedListMapper feedListMapper,
                       FeedResultMapper feedResultMapperCloud,
                       FeedResultMapper feedResultMapperLocal,
                       GlobalCacheManager globalCacheManager,
                       FeedDetailListMapper feedDetailListMapper) {

        this.apolloClient = apolloClient;
        this.context = context;
        this.feedListMapper = feedListMapper;
        this.feedDetailListMapper = feedDetailListMapper;
        this.feedResultMapperCloud = feedResultMapperCloud;
        this.feedResultMapperLocal = feedResultMapperLocal;
        this.globalCacheManager = globalCacheManager;
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
}
