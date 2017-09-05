package com.tokopedia.inbox.rescenter.createreso.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateResoStep1Mapper;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoStep1Domain;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.CreateResoStep1UseCase;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class CreateResoStep1CloudSource {
    private Context context;
    private CreateResoStep1Mapper createResoStep1Mapper;
    private ResolutionApi resolutionApi;

    public CreateResoStep1CloudSource(Context context,
                                      CreateResoStep1Mapper createResoStep1Mapper,
                                      ResolutionApi resolutionApi) {
        this.context = context;
        this.createResoStep1Mapper = createResoStep1Mapper;
        this.resolutionApi = resolutionApi;
    }

    public Observable<CreateResoStep1Domain> createResoStep1Response(RequestParams requestParams) {
        try {
            return resolutionApi.postCreateResolution(requestParams.getString(CreateResoStep1UseCase.ORDER_ID, ""),
                    requestParams.getString(CreateResoStep1UseCase.PARAM_RESULT, ""))
                    .map(createResoStep1Mapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
