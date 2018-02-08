package com.tokopedia.inbox.rescenter.createreso.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.AppealSolutionMapper;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.AppealSolutionResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetEditSolutionUseCase;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class GetAppealSolutionCloudSource {
    private AppealSolutionMapper solutionMapper;
    private ResolutionApi resolutionApi;

    public GetAppealSolutionCloudSource(AppealSolutionMapper solutionMapper,
                                        ResolutionApi resolutionApi) {
        this.solutionMapper = solutionMapper;
        this.resolutionApi = resolutionApi;
    }

    public Observable<AppealSolutionResponseDomain> getAppealSolutionResponse(RequestParams requestParams) {
        return resolutionApi.getAppealSolution(requestParams.getString(GetEditSolutionUseCase.RESO_ID, ""))
                .map(solutionMapper);
    }
}
