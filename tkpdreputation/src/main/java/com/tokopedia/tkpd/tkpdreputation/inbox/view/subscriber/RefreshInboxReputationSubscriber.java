package com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputation;

import rx.Subscriber;

/**
 * @author by nisie on 8/18/17.
 */

public class RefreshInboxReputationSubscriber extends GetFirstTimeInboxReputationSubscriber {

    public RefreshInboxReputationSubscriber(InboxReputation.View viewListener) {
        super(viewListener);
    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorRefresh(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(InboxReputationDomain inboxReputationDomain) {
        viewListener.onSuccessRefresh(mappingToViewModel(inboxReputationDomain));
    }
}
