package com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.factory;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.source.cloud.CloudFeedDataSource;

/**
 * @author ricoharisin .
 */

public class FeedFactory {

    private ApolloClient apolloClient;
    private Context context;
    private FeedListMapper feedListMapper;

    public FeedFactory(Context context,
                       ApolloClient apolloClient,
                       FeedListMapper feedListMapper) {

        this.apolloClient = apolloClient;
        this.context = context;
        this.feedListMapper = feedListMapper;
    }

    public CloudFeedDataSource createCloudFeedDataSource() {
        return new CloudFeedDataSource(context, apolloClient, feedListMapper);
    }
}
