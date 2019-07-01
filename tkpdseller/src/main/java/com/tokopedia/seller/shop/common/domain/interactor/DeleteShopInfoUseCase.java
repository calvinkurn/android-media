package com.tokopedia.seller.shop.common.domain.interactor;

import android.content.Context;

import com.tokopedia.cacheapi.domain.interactor.CacheApiDataDeleteUseCase;
import com.tokopedia.config.url.TokopediaUrl;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;
import rx.functions.Func2;

/**
 * Created by zulfikarrahman on 7/7/17.
 */

@Deprecated
public class DeleteShopInfoUseCase extends CacheApiDataDeleteUseCase {

    private final DeleteShopInfoTomeUseCase deleteShopInfoTomeUseCase;

    public DeleteShopInfoUseCase(Context context, DeleteShopInfoTomeUseCase deleteShopInfoTomeUseCase) {
        super(context);
        this.deleteShopInfoTomeUseCase = deleteShopInfoTomeUseCase;
    }

    public Observable<Boolean> createObservable() {
        return createObservable(RequestParams.create());
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        RequestParams newRequestParams = CacheApiDataDeleteUseCase.createParams(TokopediaUrl.Companion.getInstance().getWS(),
                TkpdBaseURL.Shop.PATH_SHOP + TkpdBaseURL.Shop.PATH_GET_SHOP_INFO);
        return Observable.zip(super.createObservable(newRequestParams), deleteShopInfoTomeUseCase.createObservable(), new Func2<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean aBoolean, Boolean aBoolean2) {
                return true;
            }
        });
    }
}