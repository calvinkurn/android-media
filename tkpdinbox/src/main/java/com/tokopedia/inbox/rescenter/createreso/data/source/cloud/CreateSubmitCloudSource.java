package com.tokopedia.inbox.rescenter.createreso.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateSubmitMapper;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateSubmitDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetSolutionUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach.CreateSubmitUseCase;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class CreateSubmitCloudSource {
    private CreateSubmitMapper createSubmitMapper;
    private ResolutionApi resolutionApi;

    public CreateSubmitCloudSource(CreateSubmitMapper createSubmitMapper,
                                   ResolutionApi resolutionApi) {
        this.createSubmitMapper = createSubmitMapper;
        this.resolutionApi = resolutionApi;
    }

    public Observable<CreateSubmitDomain> createSubmit(RequestParams requestParams) {
        try {
            return resolutionApi.postCreateSubmitResolution(requestParams.getString(
                    GetSolutionUseCase.ORDER_ID, ""),
                    requestParams.getString(CreateSubmitUseCase.PARAM_JSON, ""))
                    .map(createSubmitMapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
