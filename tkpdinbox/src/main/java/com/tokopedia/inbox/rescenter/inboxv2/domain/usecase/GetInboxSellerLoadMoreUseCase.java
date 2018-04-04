package com.tokopedia.inbox.rescenter.inboxv2.domain.usecase;

import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.inbox.rescenter.inboxv2.data.mapper.GetInboxMapper;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemResultViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by yfsx on 24/01/18.
 */

public class GetInboxSellerLoadMoreUseCase extends UseCase<InboxItemResultViewModel> {

    private ResolutionApi resolutionApi;
    private GetInboxMapper getInboxMapper;

    public GetInboxSellerLoadMoreUseCase(ResolutionApi resolutionApi, GetInboxMapper getInboxMapper) {
        this.resolutionApi = resolutionApi;
        this.getInboxMapper = getInboxMapper;
    }

    @Override
    public Observable<InboxItemResultViewModel> createObservable(RequestParams requestParams) {
        return resolutionApi.getInboxSeller(requestParams.getParameters()).map(getInboxMapper);
    }

}
