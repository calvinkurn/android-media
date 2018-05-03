package com.tokopedia.tokocash.autosweepmf.data.source.cloud;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.tokocash.autosweepmf.data.mapper.AutoSweepLimitMapperEntity;
import com.tokopedia.tokocash.autosweepmf.data.model.ResponseAutoSweepLimit;
import com.tokopedia.tokocash.autosweepmf.data.source.AutoSweepLimitDataStore;
import com.tokopedia.tokocash.autosweepmf.data.source.cloud.api.AutoSweepApi;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimitDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

public class AutoSweepLimitDataCloud implements AutoSweepLimitDataStore {
    private AutoSweepApi mApi;
    private AutoSweepLimitMapperEntity mMapper;

    @Inject
    public AutoSweepLimitDataCloud(AutoSweepApi api, AutoSweepLimitMapperEntity mapper) {
        this.mApi = api;
        this.mMapper = mapper;
    }


    @Override
    public Observable<AutoSweepLimitDomain> autoSweepLimit(JsonObject data) {
        return mApi.postAutoSweepLimit(data).map(new Func1<Response<ResponseAutoSweepLimit>, AutoSweepLimitDomain>() {
            @Override
            public AutoSweepLimitDomain call(Response<ResponseAutoSweepLimit> response) {
                if (response.isSuccessful()) {
                    ResponseAutoSweepLimit data = response.body();
                    return mMapper.transform(data);
                } else {
                    throw new RuntimeException(
                            ErrorNetMessage.MESSAGE_ERROR_DEFAULT
                    );
                }
            }
        });
    }
}
