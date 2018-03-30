package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.tokocash.CacheUtil;
import com.tokopedia.tokocash.anals.GetTokocashQuery;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 2/15/18.
 */

public class LocalBalanceDataSource implements BalanceDataSource {

    private static final String TAG = LocalBalanceDataSource.class.getName();
    private CacheManager cacheManager;

    public LocalBalanceDataSource(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public Observable<GetTokocashQuery.Data> getBalanceTokoCash(RequestParams requestParams) {
        return Observable.just(true).map(new Func1<Boolean, GetTokocashQuery.Data>() {
            @Override
            public GetTokocashQuery.Data call(Boolean aBoolean) {
                if (getCache() != null) {
                    return (CacheUtil.convertStringToModel(getCache(), new TypeToken<GetTokocashQuery.Data>() {
                    }.getType()));
                } else
                    throw new RuntimeException("Cache has expired");
            }
        }).map(new Func1<GetTokocashQuery.Data, GetTokocashQuery.Data>() {
            @Override
            public GetTokocashQuery.Data call(GetTokocashQuery.Data balanceTokoCashEntity) {
                return balanceTokoCashEntity;
            }
        });
    }

    private String getCache() {
        return cacheManager.get(CacheUtil.KEY_TOKOCASH_BALANCE_CACHE);
    }

}
