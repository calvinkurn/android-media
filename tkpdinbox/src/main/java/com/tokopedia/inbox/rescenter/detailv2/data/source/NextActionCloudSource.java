package com.tokopedia.inbox.rescenter.detailv2.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.GetNextActionMapper;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.GetResChatUseCase;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;

import rx.Observable;

/**
 * Created by yoasfs on 11/10/17.
 */

public class NextActionCloudSource {
    private GetNextActionMapper getNextActionMapper;
    private ResolutionApi resolutionApi;

    public NextActionCloudSource(GetNextActionMapper getNextActionMapper,
                                 ResolutionApi resolutionApi) {
        this.getNextActionMapper = getNextActionMapper;
        this.resolutionApi = resolutionApi;
    }

    public Observable<NextActionDomain> getNextAction(RequestParams params) {
        return resolutionApi.getNextAction(params.getString(GetResChatUseCase.PARAMS_RESO_ID, ""))
                .map(getNextActionMapper);
    }
}
