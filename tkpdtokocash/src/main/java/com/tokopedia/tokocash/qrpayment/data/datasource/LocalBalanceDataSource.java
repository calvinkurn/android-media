package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.tokocash.CacheUtil;
import com.tokopedia.tokocash.qrpayment.data.entity.BalanceTokoCashEntity;

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
    public Observable<BalanceTokoCashEntity> getBalanceTokoCash() {
        return Observable.just(true).map(new Func1<Boolean, BalanceTokoCashEntity>() {
            @Override
            public BalanceTokoCashEntity call(Boolean aBoolean) {
                if (getCache() != null) {
                    return (CacheUtil.convertStringToModel(getCache(), new TypeToken<BalanceTokoCashEntity>() {
                    }.getType()));
                } else
                    throw new RuntimeException("Cache has expired");
            }
        });
    }

    private String getCache() {
        return cacheManager.get(CacheUtil.KEY_TOKOCASH_BALANCE_CACHE);
    }

}
