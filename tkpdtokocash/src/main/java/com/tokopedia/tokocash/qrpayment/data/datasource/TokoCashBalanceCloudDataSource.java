package com.tokopedia.tokocash.qrpayment.data.datasource;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.data.model.storage.GlobalCacheManager;
import com.tokopedia.tokocash.network.api.WalletApi;
import com.tokopedia.tokocash.qrpayment.data.entity.BalanceTokoCashEntity;
import com.tokopedia.tokocash.util.CacheUtil;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 1/26/18.
 */

public class TokoCashBalanceCloudDataSource implements TokoCashBalanceDataSource {

    private static final String TAG = TokoCashBalanceCloudDataSource.class.getName();

    private WalletApi walletApi;
    private GlobalCacheManager globalCacheManager;

    public TokoCashBalanceCloudDataSource(WalletApi walletApi, GlobalCacheManager globalCacheManager) {
        this.walletApi = walletApi;
        this.globalCacheManager = globalCacheManager;
    }

    @Override
    public Observable<BalanceTokoCashEntity> getBalanceTokoCash() {
        Log.d(TAG, "call: network");
        return walletApi.getBalanceTokoCash()
                .map(new Func1<Response<DataResponse<BalanceTokoCashEntity>>, BalanceTokoCashEntity>() {
                    @Override
                    public BalanceTokoCashEntity call(Response<DataResponse<BalanceTokoCashEntity>> dataResponseResponse) {
                        Log.d(TAG, "call: " + dataResponseResponse.body().getData().getBalance());
                        return dataResponseResponse.body().getData();
                    }
                })
                .doOnNext(new Action1<BalanceTokoCashEntity>() {
                    @Override
                    public void call(BalanceTokoCashEntity balanceTokoCashEntity) {
                        Log.d(TAG, "call: success save balance to cache");
                        if (balanceTokoCashEntity != null) {
                            globalCacheManager.save(CacheUtil.KEY_TOKOCASH_BALANCE_CACHE,
                                    CacheUtil.convertModelToString(balanceTokoCashEntity,
                                            new TypeToken<BalanceTokoCashEntity>() {
                                            }.getType()), 60);
                        }
                    }
                });
    }
}
