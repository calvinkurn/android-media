package com.tokopedia.inbox.rescenter.inboxv2.domain.usecase;

import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.inbox.rescenter.inboxv2.data.mapper.GetInboxSingleItemMapper;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by yfsx on 24/01/18.
 */

public class GetInboxBuyerSingleItemUseCase extends UseCase<InboxItemViewModel> {

    private static final String PARAM_INBOX_ID = "inbox_id";

    private ResolutionApi resolutionApi;
    private GetInboxSingleItemMapper getInboxMapper;

    public GetInboxBuyerSingleItemUseCase(ResolutionApi resolutionApi, GetInboxSingleItemMapper getInboxMapper) {
        this.resolutionApi = resolutionApi;
        this.getInboxMapper = getInboxMapper;
    }

    @Override
    public Observable<InboxItemViewModel> createObservable(RequestParams requestParams) {
        return resolutionApi.getInboxBuyerSingleItem(requestParams.getInt(PARAM_INBOX_ID, 0),
                requestParams.getParameters()).map(getInboxMapper);
    }

    public static RequestParams getParams(int inboxId) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_INBOX_ID, inboxId);
        return params;
    }
}
