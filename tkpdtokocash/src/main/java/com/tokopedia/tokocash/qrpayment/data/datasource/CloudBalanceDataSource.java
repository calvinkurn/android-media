package com.tokopedia.tokocash.qrpayment.data.datasource;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.core.drawer2.data.pojo.UserData;
import com.tokopedia.core.drawer2.data.pojo.Wallet;
import com.tokopedia.tokocash.CacheUtil;
import com.tokopedia.tokocash.network.api.WalletBalanceApi;

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

    private static final String QUERY = "{\"query\":\"{\\n  wallet{\\n    linked\\n    balance\\n    rawBalance\\n    errors{\\n      name\\n      message\\n    }\\n    text\\n    total_balance\\n    raw_total_balance\\n    hold_balance\\n    raw_hold_balance\\n    redirect_url\\n    applinks\\n    ab_tags{\\n      tag\\n    }\\n    action{\\n      text\\n      redirect_url\\n      applinks\\n      visibility\\n    }\\n  }\\n}\",\"variables\":null,\"operationName\":null}";

    private WalletBalanceApi walletApi;
    private CacheManager cacheManager;


    private Context context;

    public CloudBalanceDataSource(WalletBalanceApi walletApi, CacheManager cacheManager) {
        this.walletApi = walletApi;
        this.cacheManager = cacheManager;
    }

    @Override
    public Observable<Wallet> getBalanceTokoCash() {
        return walletApi.getBalance(QUERY)
                .doOnNext(new Action1<Response<GraphqlResponse<UserData>>>() {
                    @Override
                    public void call(Response<GraphqlResponse<UserData>> dataResponseResponse) {
                        if (dataResponseResponse.body().getData() != null) {
                            cacheManager.save(CacheUtil.KEY_TOKOCASH_BALANCE_CACHE,
                                    CacheUtil.convertModelToString(dataResponseResponse.body().getData().getWallet(),
                                            new TypeToken<Wallet>() {
                                            }.getType()), DURATION_SAVE_TO_CACHE);
                        }
                    }
                })
                .map(new Func1<Response<GraphqlResponse<UserData>>, Wallet>() {
                    @Override
                    public Wallet call(Response<GraphqlResponse<UserData>> userData) {
                        return userData.body().getData().getWallet();
                    }
                });
    }
}