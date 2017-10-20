package com.tokopedia.inbox.rescenter.createreso.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.EditAppealResolutionResponseMapper;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.EditAppealResolutionSolutionDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.PostEditSolutionUseCase;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class PostEditSolutionCloudSource {
    private EditAppealResolutionResponseMapper mapper;
    private ResolutionApi resolutionApi;

    public PostEditSolutionCloudSource(EditAppealResolutionResponseMapper mapper,
                                       ResolutionApi resolutionApi) {
        this.mapper = mapper;
        this.resolutionApi = resolutionApi;
    }

    public Observable<EditAppealResolutionSolutionDomain>
    postEditSolutionCloudSource(RequestParams requestParams) {
        try {
            return resolutionApi.postEditSolution(
                    requestParams.getString(PostEditSolutionUseCase.RESO_ID, ""),
                    requestParams.getParameters())
                    .map(mapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
