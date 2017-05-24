package com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.source.cloud;

import android.content.Context;

import com.apollographql.android.rx.RxApollo;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloWatcher;
import com.apollographql.apollo.api.Response;
import com.google.gson.reflect.TypeToken;
import com.tkpdfeed.feeds.Feeds;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedResultMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.source.local.LocalFeedDataSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.DataFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.FeedResult;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author ricoharisin .
 */

public class CloudFeedDataSource {

    private ApolloClient apolloClient;
    private Context context;
    private FeedListMapper feedListMapper;
    private GlobalCacheManager globalCacheManager;
    private FeedResultMapper feedResultMapper;
    private static final String KEY_FEED_PLUS = "FEED_PLUS";
    public static final String KEY_CURSOR = "cursor";

    public CloudFeedDataSource(Context context,
                               ApolloClient apolloClient,
                               FeedListMapper feedListMapper,
                               FeedResultMapper feedResultMapper,
                               GlobalCacheManager globalCacheManager) {

        this.apolloClient = apolloClient;
        this.context = context;
        this.feedListMapper = feedListMapper;
        this.globalCacheManager = globalCacheManager;
        this.feedResultMapper = feedResultMapper;
    }

    public Observable<FeedResult> getFirstPageFeedsList() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(KEY_CURSOR, "");
        return getFeedsList(requestParams)
                .doOnNext(new Action1<List<DataFeedDomain>>() {
                    @Override
                    public void call(List<DataFeedDomain> dataFeedDomains) {
                        globalCacheManager.setKey(KEY_FEED_PLUS);
                        globalCacheManager.setValue(
                                CacheUtil.convertListModelToString(dataFeedDomains,
                                        new TypeToken<List<DataFeedDomain>>(){}.getType()));
                        globalCacheManager.store();
                    }
                }).map(feedResultMapper);
    }

    public Observable<FeedResult> getNextPageFeedsList(RequestParams requestParams) {
       return getFeedsList(requestParams).map(feedResultMapper);
    }

    private Observable<List<DataFeedDomain>> getFeedsList(RequestParams requestParams) {
        String cursor = requestParams.getString(KEY_CURSOR, "");
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
