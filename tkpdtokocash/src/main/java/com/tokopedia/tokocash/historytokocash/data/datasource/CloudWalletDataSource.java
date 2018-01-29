package com.tokopedia.tokocash.historytokocash.data.datasource;

import com.google.gson.Gson;
import com.tokopedia.tokocash.historytokocash.data.entity.HelpHistoryTokoCashEntity;
import com.tokopedia.tokocash.historytokocash.data.entity.TokoCashHistoryEntity;
import com.tokopedia.tokocash.historytokocash.presentation.Util;
import com.tokopedia.tokocash.network.api.WalletApi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by nabillasabbaha on 12/18/17.
 */

public class CloudWalletDataSource implements WalletDataSource {

    private WalletApi walletApi;
    private Gson gson;

    public CloudWalletDataSource(WalletApi walletApi, Gson gson) {
        this.walletApi = walletApi;
        this.gson = gson;
    }

    @Override
    public Observable<TokoCashHistoryEntity> getTokoCashHistoryData(HashMap<String, Object> mapParams) {
        return null;
//        return walletApi.getHistoryTokocash(mapParams)
//                .flatMap(new Func1<Response<TkpdTokoCashResponse>, Observable<TokoCashHistoryEntity>>() {
//                    @Override
//                    public Observable<TokoCashHistoryEntity> call(Response<TkpdTokoCashResponse> response) {
//                        return Observable
//                                .just(response.body().convertDataObj(TokoCashHistoryEntity.class));
//                    }
//                });
    }

    @Override
    public Observable<List<HelpHistoryTokoCashEntity>> getHelpHistoryData() {
        String helpHistoryList = Util.loadJSONFromAsset("help_history_tokocash.json");
        return Observable.just(Arrays.asList((HelpHistoryTokoCashEntity[]) gson.fromJson(helpHistoryList,
                HelpHistoryTokoCashEntity[].class)));
    }
}