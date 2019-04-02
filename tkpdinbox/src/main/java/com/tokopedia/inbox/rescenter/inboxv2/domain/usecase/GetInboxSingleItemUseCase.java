package com.tokopedia.inbox.rescenter.inboxv2.domain.usecase;

import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.inbox.rescenter.inboxv2.data.mapper.GetInboxSingleItemMapper;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.SingleItemInboxResultViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by yfsx on 24/01/18.
 */

public class GetInboxSingleItemUseCase extends UseCase<SingleItemInboxResultViewModel> {

    private static final String PARAM_RESO_ID = "resolutionId";

    private ResolutionApi resolutionApi;
    private GetInboxSingleItemMapper getInboxMapper;

    public GetInboxSingleItemUseCase(ResolutionApi resolutionApi, GetInboxSingleItemMapper getInboxMapper) {
        this.resolutionApi = resolutionApi;
        this.getInboxMapper = getInboxMapper;
    }

    @Override
    public Observable<SingleItemInboxResultViewModel> createObservable(RequestParams requestParams) {
        return resolutionApi.getInboxSingleItem(requestParams.getInt(PARAM_RESO_ID, 0),
                requestParams.getParameters()).map(getInboxMapper);
    }

    public static RequestParams getParams(int resolutionId) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_RESO_ID, resolutionId);
        return params;
    }
}
