package com.tokopedia.inbox.rescenter.inboxv2.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.inbox.rescenter.inboxv2.data.mapper.GetInboxMapper;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemResultViewModel;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by yfsx on 24/01/18.
 */

public class ResoInboxFactory {

    private ResolutionApi resolutionApi;
    GetInboxMapper getInboxMapper;

    public ResoInboxFactory(ResolutionApi resolutionApi, GetInboxMapper getInboxMapper) {
        this.resolutionApi = resolutionApi;
        this.getInboxMapper = getInboxMapper;
    }

    public Observable<InboxItemResultViewModel> getInboxBuyer(RequestParams requestParams) {
       return resolutionApi.getInboxBuyer(requestParams.getParameters()).map(getInboxMapper);
    }

    public Observable<InboxItemResultViewModel> getInboxSeller(RequestParams requestParams) {
        return resolutionApi.getInboxSeller(requestParams.getParameters()).map(getInboxMapper);
    }
}
