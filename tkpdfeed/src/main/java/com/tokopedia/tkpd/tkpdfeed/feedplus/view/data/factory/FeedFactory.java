package com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.factory;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedResultMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.source.cloud.CloudFeedDataSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.source.local.LocalFeedDataSource;

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

    public FeedFactory(Context context,
                       ApolloClient apolloClient,
                       FeedListMapper feedListMapper,
                       FeedResultMapper feedResultMapperCloud,
                       FeedResultMapper feedResultMapperLocal,
                       GlobalCacheManager globalCacheManager) {

        this.apolloClient = apolloClient;
        this.context = context;
        this.feedListMapper = feedListMapper;
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

}
