package com.tokopedia.seller.shop.common.domain.interactor;

import com.tokopedia.cacheapi.domain.interactor.CacheApiDataDeleteUseCase;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by zulfikarrahman on 7/7/17.
 */

public class DeleteShopInfoUseCase extends CacheApiDataDeleteUseCase {

    public Observable<Boolean> createObservable() {
        RequestParams newRequestParams = CacheApiDataDeleteUseCase.createParams(TkpdBaseURL.BASE_DOMAIN,
                TkpdBaseURL.Shop.PATH_SHOP + TkpdBaseURL.Shop.PATH_GET_SHOP_INFO);
        return createObservable(newRequestParams);
    }
}