package com.tokopedia.inbox.rescenter.detailv2.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.GetDetailResChatMapper;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.GetDetailResChatMoreMapper;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.GetResChatUseCase;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationListDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;

import rx.Observable;

/**
 * Created by milhamj on 22/11/17.
 */

public class ResChatMoreCloudSource {
    private GetDetailResChatMoreMapper getDetailResChatMoreMapper;
    private ResolutionApi resolutionApi;

    public ResChatMoreCloudSource(GetDetailResChatMoreMapper getDetailResChatMoreMapper,
                              ResolutionApi resolutionApi) {
        this.getDetailResChatMoreMapper = getDetailResChatMoreMapper;
        this.resolutionApi = resolutionApi;
    }

    public Observable<ConversationListDomain> getDetailResChatMoreCloud(RequestParams params) {
        return resolutionApi.getConversationMore(
                params.getString(GetResChatUseCase.PARAMS_RESO_ID, ""),
                params.getParameters())
                .map(getDetailResChatMoreMapper);
    }
}
