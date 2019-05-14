package com.tokopedia.seller.shop.common.domain.interactor;

import com.tokopedia.cacheapi.domain.interactor.CacheApiDataDeleteUseCase;
import com.tokopedia.config.url.TokopediaUrl;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by zulfikarrahman on 7/7/17.
 */

/**
 * Temporary solution to delete shop info for different path between tome and ws. Need to remove if already use shop common
 */
@Deprecated
public class DeleteShopInfoTomeUseCase extends CacheApiDataDeleteUseCase {

    private static final String SHOP_INFO_PATH = "v1/web-service/shop/get_shop_info";

    public Observable<Boolean> createObservable() {
        return createObservable(RequestParams.create());
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        RequestParams newRequestParams = CacheApiDataDeleteUseCase.createParams(TokopediaUrl.Companion.getInstance().getTOME(), SHOP_INFO_PATH);
        return super.createObservable(newRequestParams);
    }
}