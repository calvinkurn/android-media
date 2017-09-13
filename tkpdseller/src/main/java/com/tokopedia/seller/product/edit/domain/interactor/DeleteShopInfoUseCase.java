package com.tokopedia.seller.product.edit.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.cache.data.source.ApiCacheDataSource;
import com.tokopedia.core.cache.domain.interactor.CacheApiDataDeleteUseCase;
import com.tokopedia.core.cache.domain.model.CacheApiDataDomain;
import com.tokopedia.core.cache.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.seller.product.edit.domain.ShopInfoRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 7/7/17.
 */

public class DeleteShopInfoUseCase extends UseCase<Boolean> {
    private final CacheApiDataDeleteUseCase cacheApiDataDeleteUseCase;

    @Inject
    public DeleteShopInfoUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                 CacheApiDataDeleteUseCase cacheApiDataDeleteUseCase) {
        super(threadExecutor, postExecutionThread);
        this.cacheApiDataDeleteUseCase = cacheApiDataDeleteUseCase;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        CacheApiDataDomain cacheApiDataDomain = ApiCacheDataSource.from(TkpdBaseURL.BASE_DOMAIN,
                TkpdBaseURL.Shop.PATH_SHOP + TkpdBaseURL.Shop.PATH_GET_SHOP_INFO);
        RequestParams newRequestParams = CacheApiDataDeleteUseCase.createParams(cacheApiDataDomain);
        return cacheApiDataDeleteUseCase.createObservable(newRequestParams);
    }
}
