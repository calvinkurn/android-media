package com.tokopedia.tokocash.qrpayment.data.datasource;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.storage.GlobalCacheManager;
import com.tokopedia.tokocash.qrpayment.data.entity.BalanceTokoCashEntity;
import com.tokopedia.tokocash.util.CacheUtil;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 1/26/18.
 */

public class TokoCashBalanceLocalDataSource implements TokoCashBalanceDataSource {

    private static final String TAG = TokoCashBalanceCloudDataSource.class.getName();
    private GlobalCacheManager globalCacheManager;

    public TokoCashBalanceLocalDataSource(GlobalCacheManager globalCacheManager) {
        this.globalCacheManager = globalCacheManager;
    }

    @Override
    public Observable<BalanceTokoCashEntity> getBalanceTokoCash() {
        Log.d(TAG, "call: cache");
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
        return globalCacheManager.get(CacheUtil.KEY_TOKOCASH_BALANCE_CACHE);
    }
}
