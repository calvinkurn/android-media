package com.tokopedia.inbox.rescenter.createreso.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.EditSolutionMapper;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.EditSolutionResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetEditSolutionUseCase;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class GetEditSolutionCloudSource {
    private EditSolutionMapper solutionMapper;
    private ResolutionApi resolutionApi;

    public GetEditSolutionCloudSource(EditSolutionMapper solutionMapper,
                                      ResolutionApi resolutionApi) {
        this.solutionMapper = solutionMapper;
        this.resolutionApi = resolutionApi;
    }

    public Observable<EditSolutionResponseDomain> getEditSolutionResponse(RequestParams requestParams) {
        return resolutionApi.getEditSolution(requestParams.getString(GetEditSolutionUseCase.RESO_ID, ""))
                .map(solutionMapper);
    }
}
