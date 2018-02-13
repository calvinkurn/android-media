package com.tokopedia.tokocash.historytokocash.data.datasource;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tokocash.historytokocash.data.entity.HelpHistoryTokoCashEntity;
import com.tokopedia.tokocash.historytokocash.data.entity.TokoCashHistoryEntity;
import com.tokopedia.tokocash.historytokocash.presentation.Util;
import com.tokopedia.tokocash.network.api.WalletApi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

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
        return walletApi.getHistoryTokocash(mapParams)
                .map(new Func1<Response<DataResponse<TokoCashHistoryEntity>>, TokoCashHistoryEntity>() {
                    @Override
                    public TokoCashHistoryEntity call(Response<DataResponse<TokoCashHistoryEntity>> dataResponseResponse) {
                        return dataResponseResponse.body().getData();
                    }
                });
    }

    @Override
    public Observable<List<HelpHistoryTokoCashEntity>> getHelpHistoryData() {
        String helpHistoryList = Util.loadJSONFromAsset("help_history_tokocash.json");
        return Observable.just(Arrays.asList((HelpHistoryTokoCashEntity[]) gson.fromJson(helpHistoryList,
                HelpHistoryTokoCashEntity[].class)));
    }
}