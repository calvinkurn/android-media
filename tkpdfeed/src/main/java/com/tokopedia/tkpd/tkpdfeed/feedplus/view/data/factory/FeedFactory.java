package com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.factory;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedDetailListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.source.CloudFeedDetailDataSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.source.cloud.CloudFeedDataSource;

/**
 * @author ricoharisin .
 */

public class FeedFactory {

    private ApolloClient apolloClient;
    private Context context;
    private FeedListMapper feedListMapper;
    private FeedDetailListMapper feedDetailListMapper;

    public FeedFactory(Context context,
                       ApolloClient apolloClient,
                       FeedListMapper feedListMapper,
                       FeedDetailListMapper feedDetailListMapper) {

        this.apolloClient = apolloClient;
        this.context = context;
        this.feedListMapper = feedListMapper;
        this.feedDetailListMapper = feedDetailListMapper;
    }

    public CloudFeedDataSource createCloudFeedDataSource() {
        return new CloudFeedDataSource(context, apolloClient, feedListMapper);
    }

    public CloudFeedDetailDataSource createCloudDetailFeedDataSource() {
        return new CloudFeedDetailDataSource(context, apolloClient, feedDetailListMapper);
    }
}
