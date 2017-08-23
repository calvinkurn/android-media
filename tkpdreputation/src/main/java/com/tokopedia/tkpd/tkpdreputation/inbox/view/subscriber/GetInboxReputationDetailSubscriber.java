package com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.InboxReputationDetailDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.InboxReputationDetailItemDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailHeaderViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailItemViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by nisie on 8/19/17.
 */

public class GetInboxReputationDetailSubscriber extends Subscriber<InboxReputationDetailDomain> {

    private final InboxReputationDetail.View viewListener;

    public GetInboxReputationDetailSubscriber(InboxReputationDetail.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorGetInboxDetail(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(InboxReputationDetailDomain inboxReputationDetailDomain) {
        viewListener.onSuccessGetInboxDetail(
                mappingToHeaderViewModel(inboxReputationDetailDomain),
                mappingToListItemViewModel(inboxReputationDetailDomain)
        );
    }

    private InboxReputationDetailHeaderViewModel mappingToHeaderViewModel(InboxReputationDetailDomain
                                                                                  inboxReputationDetailDomain) {
        return new InboxReputationDetailHeaderViewModel();
    }

    private List<Visitable> mappingToListItemViewModel(InboxReputationDetailDomain
                                                               inboxReputationDetailDomain) {
        List<Visitable> list = new ArrayList<>();
        if (inboxReputationDetailDomain.getData() != null) {
            for (InboxReputationDetailItemDomain detailDomain : inboxReputationDetailDomain.getData()) {
                list.add(convertToInboxReputationDetailItemViewModel(detailDomain));
            }
        }
        return list;

    }

    private Visitable convertToInboxReputationDetailItemViewModel(InboxReputationDetailItemDomain detailDomain) {
        return new InboxReputationDetailItemViewModel();
    }
}
