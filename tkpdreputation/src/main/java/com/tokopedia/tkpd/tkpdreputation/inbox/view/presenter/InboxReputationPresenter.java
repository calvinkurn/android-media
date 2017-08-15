package com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter;


import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.GetFirstTimeInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputation;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.GetFirstTimeInboxReputationSubscriber;

import javax.inject.Inject;

/**
 * @author by nisie on 8/10/17.
 */

public class InboxReputationPresenter
        extends BaseDaggerPresenter<InboxReputation.View>
        implements InboxReputation.Presenter {

    private final GetFirstTimeInboxReputationUseCase getFirstTimeInboxReputationUseCase;
    private InboxReputation.View viewListener;
    private PagingHandler pagingHandler;

    @Inject
    InboxReputationPresenter(GetFirstTimeInboxReputationUseCase getFirstTimeInboxReputationUseCase) {
        this.getFirstTimeInboxReputationUseCase = getFirstTimeInboxReputationUseCase;
        this.pagingHandler = new PagingHandler();
    }

    @Override
    public void attachView(InboxReputation.View view) {
        super.attachView(view);
        this.viewListener = view;

    }

    @Override
    public void getFirstTimeInboxReputation(int tab) {
        viewListener.showLoadingFull();
        getFirstTimeInboxReputationUseCase.execute(
                GetFirstTimeInboxReputationUseCase.getFirstTimeParam(tab),
                new GetFirstTimeInboxReputationSubscriber(viewListener));
    }
}
