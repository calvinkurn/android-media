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
    private String cursor;

    private FeedFactory(Context context,
                        ApolloClient apolloClient,
                        FeedListMapper feedListMapper,
                        String cursor) {

        this.apolloClient = apolloClient;
        this.context = context;
        this.feedListMapper = feedListMapper;
        this.cursor = cursor;
    }

    public CloudFeedDataSource createCloudFeedDataSource() {
        return new CloudFeedDataSource(context, apolloClient, feedListMapper, cursor);
    }
}
