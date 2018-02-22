package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.tokocash.CacheUtil;
import com.tokopedia.tokocash.apiservice.WalletService;
import com.tokopedia.tokocash.qrpayment.data.entity.BalanceTokoCashEntity;
import com.tokopedia.tokocash.qrpayment.data.entity.TokoCashEntity;

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
                .doOnNext(new Action1<Response<TokoCashEntity>>() {
                    @Override
                    public void call(Response<TokoCashEntity> dataResponseResponse) {
                        if (dataResponseResponse.body() != null) {
                            dataResponseResponse.body().setSuccess(true);
                            cacheManager.save(CacheUtil.KEY_TOKOCASH_BALANCE_CACHE,
                                    CacheUtil.convertModelToString(dataResponseResponse.body(),
                                            new TypeToken<TokoCashEntity>() {
                                            }.getType()), 60);
                        }
                    }
                })
                .map(new Func1<Response<TokoCashEntity>, BalanceTokoCashEntity>() {
                    @Override
                    public BalanceTokoCashEntity call
                            (Response<TokoCashEntity> dataResponseResponse) {
                        return dataResponseResponse.body().getData();
                    }
                });
    }

}
