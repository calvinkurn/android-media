package com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.cloud;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.RecentProductMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.local.LocalFeedDataSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.recentview.RecentViewProductDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetRecentViewUseCase;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * @author Kulomady on 12/9/16.
 */

public class CloudRecentProductDataSource {

    private final GlobalCacheManager globalCacheManager;
    private final MojitoService mojitoService;
    private RecentProductMapper recentProductMapper;

    public CloudRecentProductDataSource(GlobalCacheManager globalCacheManager,
                                        MojitoService mojitoService,
                                        RecentProductMapper recentProductMapper) {
        this.mojitoService = mojitoService;
        this.recentProductMapper = recentProductMapper;
        this.globalCacheManager = globalCacheManager;
    }

    public Observable<List<RecentViewProductDomain>> getRecentProduct(RequestParams requestParams) {

        return mojitoService.getRecentProduct(
                String.valueOf(requestParams.getParameters()
                        .get(GetRecentViewUseCase.PARAM_USER_ID)))
                .doOnNext(validateError())
                .map(recentProductMapper)
                .doOnNext(saveToCache());
    }

    private Action1<List<RecentViewProductDomain>> saveToCache() {
        return new Action1<List<RecentViewProductDomain>>() {
            @Override
            public void call(final List<RecentViewProductDomain> listRecentView) {
                FeedDomain feedDomain = globalCacheManager.getConvertObjData(
                        LocalFeedDataSource.KEY_FEED_PLUS, FeedDomain.class);

                if (feedDomain != null) {
                    feedDomain.setRecentProduct(listRecentView);

                    globalCacheManager.setKey(LocalFeedDataSource.KEY_FEED_PLUS);
                    globalCacheManager.setValue(
                            CacheUtil.convertModelToString(feedDomain,
                                    new TypeToken<FeedDomain>() {
                                    }.getType()));
                    globalCacheManager.store();
                }
            }
        };
    }

    private Action1<Response<String>> validateError() {
        return new Action1<Response<String>>() {
            @Override
            public void call(Response<String> stringResponse) {
                if (stringResponse.code() != 200) {
                    throw new RuntimeException(String.valueOf(stringResponse.code()));
                }
            }
        };
    }


}
