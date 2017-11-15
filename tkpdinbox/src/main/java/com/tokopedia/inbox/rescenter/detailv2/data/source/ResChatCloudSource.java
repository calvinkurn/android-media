package com.tokopedia.inbox.rescenter.detailv2.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.GetDetailResChatMapper;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.GetResChatUseCase;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;

import rx.Observable;

/**
 * Created by yoasfs on 11/10/17.
 */

public class ResChatCloudSource {
    private GetDetailResChatMapper getDetailResChatMapper;
    private ResolutionApi resolutionApi;

    public ResChatCloudSource(GetDetailResChatMapper getDetailResChatMapper,
                              ResolutionApi resolutionApi) {
        this.getDetailResChatMapper = getDetailResChatMapper;
        this.resolutionApi = resolutionApi;
    }

    public Observable<DetailResChatDomain> getDetailResChatCloud(RequestParams params) {
        return resolutionApi.getConversation(
                params.getString(GetResChatUseCase.PARAMS_RESO_ID, ""),
                params.getParameters())
                .map(getDetailResChatMapper);
    }
}
