package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.core.drawer2.data.pojo.Wallet;
import com.tokopedia.tokocash.CacheUtil;

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
    public Observable<Wallet> getBalanceTokoCash() {
        return Observable.just(true).map(new Func1<Boolean, Wallet>() {
            @Override
            public Wallet call(Boolean aBoolean) {
                if (getCache() != null) {
                    return (CacheUtil.convertStringToModel(getCache(), new TypeToken<Wallet>() {
                    }.getType()));
                } else
                    throw new RuntimeException("Cache has expired");
            }
        }).map(new Func1<Wallet, Wallet>() {
            @Override
            public Wallet call(Wallet wallet) {
                return wallet;
            }
        });
    }

    private String getCache() {
        return cacheManager.get(CacheUtil.KEY_TOKOCASH_BALANCE_CACHE);
    }

}