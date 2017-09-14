package com.tokopedia.inbox.rescenter.createreso.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateValidateMapper;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateValidateDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.CreateResoStep1UseCase;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class CreateValidateCloudSource {
    private CreateValidateMapper createValidateMapper;
    private ResolutionApi resolutionApi;

    public CreateValidateCloudSource(CreateValidateMapper createValidateMapper,
                                     ResolutionApi resolutionApi) {
        this.createValidateMapper = createValidateMapper;
        this.resolutionApi = resolutionApi;
    }

    public Observable<CreateValidateDomain> createValidate(RequestParams requestParams) {
        try {
            return resolutionApi.postCreateValidateResolution(requestParams.getString(
                    CreateResoStep1UseCase.ORDER_ID, ""),
                    requestParams.getString(CreateResoStep1UseCase.PARAM_RESULT, ""))
                    .map(createValidateMapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
