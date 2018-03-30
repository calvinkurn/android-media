package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.apollographql.android.rx.RxApollo;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloWatcher;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.tokocash.CacheUtil;
import com.tokopedia.tokocash.anals.GetTokocashQuery;
import com.tokopedia.tokocash.network.api.WalletApi;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by nabillasabbaha on 2/15/18.
 */

public class CloudBalanceDataSource implements BalanceDataSource {

    private static final String TAG = CloudBalanceDataSource.class.getName();
    private static final int DURATION_SAVE_TO_CACHE = 60;

    private CacheManager cacheManager;
    private ApolloClient apolloClient;

    public CloudBalanceDataSource(CacheManager cacheManager, ApolloClient aplClient) {
        this.cacheManager = cacheManager;
        this.apolloClient = aplClient;
    }

    @Override
    public Observable<GetTokocashQuery.Data> getBalanceTokoCash(RequestParams requestParams) {

        //CommonUtils.dumper("rxapollo called userID " + requestParams.getInt(GetUserAttributesUseCase.PARAM_USER_ID, 0));

        ApolloWatcher<GetTokocashQuery.Data> apolloWatcher = apolloClient.newCall(new GetTokocashQuery()).watcher();

        return RxApollo.from(apolloWatcher).doOnNext(new Action1<GetTokocashQuery.Data>() {
            @Override
            public void call(GetTokocashQuery.Data dataResponseResponse) {
                if (dataResponseResponse != null) {
                    cacheManager.save(CacheUtil.KEY_TOKOCASH_BALANCE_CACHE,
                            CacheUtil.convertModelToString(dataResponseResponse,
                                    new TypeToken<GetTokocashQuery.Data>() {
                                    }.getType()), DURATION_SAVE_TO_CACHE);
                }
            }
        });
    }

}
