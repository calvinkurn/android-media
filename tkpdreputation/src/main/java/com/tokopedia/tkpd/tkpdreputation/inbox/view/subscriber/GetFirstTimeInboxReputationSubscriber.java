package com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationItemDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputation;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationItemViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by nisie on 8/14/17.
 */

public class GetFirstTimeInboxReputationSubscriber extends Subscriber<InboxReputationDomain> {

    protected final InboxReputation.View viewListener;

    public GetFirstTimeInboxReputationSubscriber(InboxReputation.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorGetFirstTimeInboxReputation(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(InboxReputationDomain inboxReputationDomain) {
        viewListener.onSuccessGetFirstTimeInboxReputation(mappingToViewModel(inboxReputationDomain));

    }

    protected InboxReputationViewModel mappingToViewModel(InboxReputationDomain inboxReputationDomain) {
        return new InboxReputationViewModel
                (convertToInboxReputationList(inboxReputationDomain.getInboxReputation()),
                        inboxReputationDomain.getPaging().isHasNext()
                );
    }

    private List<InboxReputationItemViewModel> convertToInboxReputationList(List<InboxReputationItemDomain> inboxReputationDomain) {
        List<InboxReputationItemViewModel> list = new ArrayList<>();
        for (InboxReputationItemDomain domain : inboxReputationDomain) {
            list.add(new InboxReputationItemViewModel(
                    String.valueOf(domain.getReputationId()),
                    domain.getRevieweeData().getRevieweeName(),
                    domain.getOrderData().getCreateTimeFmt().getDateTimeFmt1(),
                    domain.getRevieweeData().getRevieweePicture(),
                    domain.getReputationData().isShowLockingDeadline(),
                    String.valueOf(domain.getReputationData().getLockingDeadlineDays()),
                    domain.getOrderData().getInvoiceRefNum(),
                    domain.getReputationData().isShowBookmark()
            ));
        }
        return list;
    }
}
