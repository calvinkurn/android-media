package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.tokocash.CacheUtil;
import com.tokopedia.tokocash.network.api.WalletApi;
import com.tokopedia.tokocash.qrpayment.data.entity.BalanceTokoCashEntity;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 2/15/18.
 */

public class CloudBalanceDataSource implements BalanceDataSource {

    private static final String TAG = CloudBalanceDataSource.class.getName();
    private static final int DURATION_SAVE_TO_CACHE = 60;

    private WalletApi walletApi;
    private CacheManager cacheManager;

    public CloudBalanceDataSource(WalletApi walletApi, CacheManager cacheManager) {
        this.walletApi = walletApi;
        this.cacheManager = cacheManager;
    }

    @Override
    public Observable<BalanceTokoCashEntity> getBalanceTokoCash() {
        return walletApi.getBalanceTokoCash()
                .doOnNext(new Action1<Response<DataResponse<BalanceTokoCashEntity>>>() {
                    @Override
                    public void call(Response<DataResponse<BalanceTokoCashEntity>> dataResponseResponse) {
                        if (dataResponseResponse.body().getData() != null) {
                            cacheManager.save(CacheUtil.KEY_TOKOCASH_BALANCE_CACHE,
                                    CacheUtil.convertModelToString(dataResponseResponse.body().getData(),
                                            new TypeToken<BalanceTokoCashEntity>() {
                                            }.getType()), DURATION_SAVE_TO_CACHE);
                        }
                    }
                })
                .map(new Func1<Response<DataResponse<BalanceTokoCashEntity>>, BalanceTokoCashEntity>() {
                    @Override
                    public BalanceTokoCashEntity call(Response<DataResponse<BalanceTokoCashEntity>> dataResponseResponse) {
                        return dataResponseResponse.body().getData();
                    }
                });
    }

}
