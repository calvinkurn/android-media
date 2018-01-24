package com.tokopedia.inbox.rescenter.inboxv2.domain.usecase;

import com.tokopedia.inbox.rescenter.inboxv2.data.factory.ResoInboxFactory;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemResultViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * Created by yfsx on 24/01/18.
 */

public class GetInboxSellerUseCase extends UseCase<InboxItemResultViewModel> {

    private ResoInboxFactory factory;

    public GetInboxSellerUseCase(ResoInboxFactory factory) {
        this.factory = factory;
    }

    @Override
    public Observable<InboxItemResultViewModel> createObservable(RequestParams requestParams) {
        return factory.getInboxSeller(requestParams);
    }

}
