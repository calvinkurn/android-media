package com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputation;

/**
 * @author by nisie on 8/18/17.
 */

public class RefreshInboxReputationSubscriber extends GetFirstTimeInboxReputationSubscriber {

    private final boolean isUsingFilter;

    public RefreshInboxReputationSubscriber(InboxReputation.View viewListener, boolean isUsingFilter) {
        super(viewListener);
        this.isUsingFilter = isUsingFilter;
    }

    @Override
    public void onError(Throwable e) {
        viewListener.finishRefresh();
        viewListener.onErrorRefresh(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(InboxReputationDomain inboxReputationDomain) {
        viewListener.finishRefresh();
        if (inboxReputationDomain.getInboxReputation().isEmpty() && isUsingFilter) {
            viewListener.onShowEmptyFilteredInboxReputation();
        } else if (inboxReputationDomain.getInboxReputation().isEmpty() && !isUsingFilter) {
            viewListener.onShowEmpty();
        } else {
            viewListener.onSuccessRefresh(mappingToViewModel(inboxReputationDomain));
        }
    }
}
