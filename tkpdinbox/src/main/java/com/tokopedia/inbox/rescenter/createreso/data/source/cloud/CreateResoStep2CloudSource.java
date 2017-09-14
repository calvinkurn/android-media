package com.tokopedia.inbox.rescenter.createreso.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateResoStep2Mapper;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoStep2Domain;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetSolutionUseCase;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class CreateResoStep2CloudSource {
    private Context context;
    private CreateResoStep2Mapper createResoStep2Mapper;
    private ResolutionApi resolutionApi;

    public CreateResoStep2CloudSource(Context context,
                                      CreateResoStep2Mapper createResoStep2Mapper,
                                      ResolutionApi resolutionApi) {
        this.context = context;
        this.createResoStep2Mapper = createResoStep2Mapper;
        this.resolutionApi = resolutionApi;
    }

    public Observable<CreateResoStep2Domain> createResoStep2Response(RequestParams requestParams) {
        try {
            return resolutionApi.postCreateResolution(requestParams.getString(
                    GetSolutionUseCase.ORDER_ID, ""),
                    requestParams.getString(GetSolutionUseCase.PARAM_PROBLEM, ""))
                    .map(createResoStep2Mapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
