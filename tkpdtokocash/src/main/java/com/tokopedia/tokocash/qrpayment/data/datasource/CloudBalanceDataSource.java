package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.tokocash.CacheUtil;
import com.tokopedia.tokocash.apiservice.WalletService;
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

    private WalletService walletService;
    private CacheManager cacheManager;

    public CloudBalanceDataSource(WalletService walletService, CacheManager cacheManager) {
        this.walletService = walletService;
        this.cacheManager = cacheManager;
    }

    @Override
    public Observable<BalanceTokoCashEntity> getBalanceTokoCash() {
        return walletService.getApi().getBalanceTokoCash()
                .map(new Func1<Response<DataResponse<BalanceTokoCashEntity>>, BalanceTokoCashEntity>() {
                    @Override
                    public BalanceTokoCashEntity call(Response<DataResponse<BalanceTokoCashEntity>> dataResponseResponse) {
                        return dataResponseResponse.body().getData();
                    }
                })
                .doOnNext(new Action1<BalanceTokoCashEntity>() {
                    @Override
                    public void call(BalanceTokoCashEntity balanceTokoCashEntity) {
                        if (balanceTokoCashEntity != null) {
                            cacheManager.save(CacheUtil.KEY_TOKOCASH_BALANCE_CACHE,
                                    CacheUtil.convertModelToString(balanceTokoCashEntity,
                                            new TypeToken<BalanceTokoCashEntity>() {
                                            }.getType()), 60);
                        }
                    }
                });
    }

}
