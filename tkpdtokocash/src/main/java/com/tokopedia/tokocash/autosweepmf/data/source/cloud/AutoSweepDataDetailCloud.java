package com.tokopedia.tokocash.autosweepmf.data.source.cloud;

import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.tokocash.autosweepmf.data.mapper.AutoSweepDetailMapper;
import com.tokopedia.tokocash.autosweepmf.data.model.ResponseAutoSweepDetail;
import com.tokopedia.tokocash.autosweepmf.data.source.AutoSweepDetailDataStore;
import com.tokopedia.tokocash.autosweepmf.data.source.cloud.api.AutoSweepApi;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepDetail;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

public class AutoSweepDataDetailCloud implements AutoSweepDetailDataStore {
    private AutoSweepApi mApi;
    private AutoSweepDetailMapper mMapper;

    @Inject
    public AutoSweepDataDetailCloud(AutoSweepApi api, AutoSweepDetailMapper mapper) {
        this.mApi = api;
        this.mMapper = mapper;
    }

    @Override
    public Observable<AutoSweepDetail> autoSweepDetail() {
        return mApi.getAutoSweepDetail().map(new Func1<Response<ResponseAutoSweepDetail>, AutoSweepDetail>() {
            @Override
            public AutoSweepDetail call(Response<ResponseAutoSweepDetail> response) {
                if (response.isSuccessful()) {
                    ResponseAutoSweepDetail data = response.body();
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
