package com.tokopedia.shop.common.domain.interactor;

import com.tokopedia.cacheapi.domain.interactor.CacheApiDataDeleteUseCase;
import com.tokopedia.shop.common.constant.ShopCommonUrl;
import com.tokopedia.shop.common.constant.ShopUrl;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by zulfikarrahman on 7/7/17.
 */

public class DeleteShopInfoUseCase extends CacheApiDataDeleteUseCase {

    public Observable<Boolean> createObservable() {
        RequestParams newRequestParams = CacheApiDataDeleteUseCase.createParams(ShopUrl.BASE_URL, ShopCommonUrl.SHOP_INFO_PATH);
        return createObservable(newRequestParams);
    }
}