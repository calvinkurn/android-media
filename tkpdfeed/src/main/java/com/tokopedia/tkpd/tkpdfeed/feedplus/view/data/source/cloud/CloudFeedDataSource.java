package com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.source.cloud;

import android.content.Context;

import com.apollographql.android.rx.RxApollo;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloWatcher;
import com.apollographql.apollo.api.Response;
import com.tkpdfeed.feeds.Feeds;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.DataFeedDomain;

import java.util.List;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public class CloudFeedDataSource {

    private ApolloClient apolloClient;
    private Context context;
    private FeedListMapper feedListMapper;
    private String cursor;

    public CloudFeedDataSource(Context context,
                               ApolloClient apolloClient,
                               FeedListMapper feedListMapper,
                               String cursor) {

        this.apolloClient = apolloClient;
        this.context = context;
        this.feedListMapper = feedListMapper;
        this.cursor = cursor;
    }

    public Observable<List<DataFeedDomain>> getFeedsList() {
        ApolloWatcher<Feeds.Data> apolloWatcher = apolloClient.newCall(Feeds.builder()
                .userID(getLoginId(context))
                .limit(5)
                .cursor(cursor)
                .build()).watcher();
       return RxApollo.from(apolloWatcher).map(feedListMapper);
    }

    private int getLoginId(Context context) {
        if (SessionHandler.getLoginID(context).equals("")){
            return 0;
        } else {
            return Integer.parseInt(SessionHandler.getLoginID(context));
        }
    }
}
