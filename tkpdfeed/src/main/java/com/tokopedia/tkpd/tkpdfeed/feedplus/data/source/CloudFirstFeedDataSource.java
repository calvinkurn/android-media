package com.tokopedia.tkpd.tkpdfeed.feedplus.data.source;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.base.common.dbManager.RecentProductDbManager;
import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.database.model.DbRecentProduct;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.RecentProductMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.cloud.CloudFeedDataSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.local.LocalFeedDataSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.recentview.RecentViewProductDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetFirstPageFeedsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetRecentProductUsecase;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;

/**
 * @author by nisie on 7/3/17.
 */

public class CloudFirstFeedDataSource extends CloudFeedDataSource {
    private final RecentProductDbManager recentProductDbManager;
    private final MojitoService mojitoService;
    private final RecentProductMapper recentProductMapper;

    public CloudFirstFeedDataSource(Context context,
                                    ApolloClient apolloClient,
                                    FeedListMapper feedListMapper,
                                    FeedResultMapper feedResultMapperCloud,
                                    GlobalCacheManager globalCacheManager,
                                    RecentProductDbManager recentProductDbManager,
                                    MojitoService mojitoService,
                                    RecentProductMapper recentProductMapper) {
        super(context, apolloClient, feedListMapper, feedResultMapperCloud, globalCacheManager);
        this.recentProductDbManager = recentProductDbManager;
        this.mojitoService = mojitoService;
        this.recentProductMapper = recentProductMapper;
    }

    public Observable<FeedResult> getFirstPageFeedsList(final RequestParams requestParams) {
        return Observable.zip(getFeedObservable(requestParams),
                getRecentProduct(requestParams), new Func2<FeedResult, List<RecentViewProductDomain>, FeedResult>() {
                    @Override
                    public FeedResult call(FeedResult feedResult, List<RecentViewProductDomain> recentViewProductDomains) {
                        feedResult.getFeedDomain().setRecentProduct(recentViewProductDomains);
                        return feedResult;
                    }
                });
    }

    public Observable<List<RecentViewProductDomain>> getRecentProduct(RequestParams requestParams) {

        return mojitoService.getRecentProduct(
                String.valueOf(requestParams.getInt(GetFirstPageFeedsUseCase.PARAM_USER_ID, 0)))
                .doOnNext(validateError())
                .doOnNext(saveToCache())
                .map(recentProductMapper);
    }

    private Action1<Response<String>> saveToCache() {
        return new Action1<Response<String>>() {
            @Override
            public void call(Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DbRecentProduct recentProductDb = new DbRecentProduct();
                    recentProductDb.setId(1);
                    recentProductDb.setLastUpdated(System.currentTimeMillis());
                    recentProductDb.setContentRecentProduct(response.body());
                    recentProductDbManager.store(recentProductDb);
                }
            }
        };
    }

    private Action1<Response<String>> validateError() {
        return new Action1<Response<String>>() {
            @Override
            public void call(Response<String> stringResponse) {
                if (stringResponse.code() >= 500 && stringResponse.code() < 600) {
                    throw new ErrorMessageException("Server Error!");
                } else if (stringResponse.code() >= 400 && stringResponse.code() < 500) {
                    throw new ErrorMessageException("Client Error!");
                }
            }
        };
    }

    public Observable<FeedResult> getFeedObservable(RequestParams requestParams) {
        return getFeedsList(requestParams)
                .doOnNext(new Action1<FeedDomain>() {
                    @Override
                    public void call(FeedDomain dataFeedDomains) {
                        globalCacheManager.setKey(LocalFeedDataSource.KEY_FEED_PLUS);
                        globalCacheManager.setValue(
                                CacheUtil.convertModelToString(dataFeedDomains,
                                        new TypeToken<FeedDomain>() {
                                        }.getType()));
                        globalCacheManager.store();
                    }
                }).map(feedResultMapper);
    }
}
