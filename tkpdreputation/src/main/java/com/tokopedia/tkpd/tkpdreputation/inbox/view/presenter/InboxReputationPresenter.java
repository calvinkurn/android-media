package com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter;


import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.GetFirstTimeInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.GetInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputation;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.GetFirstTimeInboxReputationSubscriber;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.GetNextPageInboxReputationSubscriber;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.RefreshInboxReputationSubscriber;

import javax.inject.Inject;

/**
 * @author by nisie on 8/10/17.
 */

public class InboxReputationPresenter
        extends BaseDaggerPresenter<InboxReputation.View>
        implements InboxReputation.Presenter {

    private final GetFirstTimeInboxReputationUseCase getFirstTimeInboxReputationUseCase;
    private final GetInboxReputationUseCase getInboxReputationUseCase;
    private InboxReputation.View viewListener;
    private PagingHandler pagingHandler;

    @Inject
    InboxReputationPresenter(GetFirstTimeInboxReputationUseCase
                                     getFirstTimeInboxReputationUseCase,
                             GetInboxReputationUseCase getInboxReputationUseCase) {
        this.getFirstTimeInboxReputationUseCase = getFirstTimeInboxReputationUseCase;
        this.getInboxReputationUseCase = getInboxReputationUseCase;
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

    @Override
    public void getNextPage(int lastItemPosition, int visibleItem, String query, int timeFilter,
                            int tab) {
        if (hasNextPage() && isOnLastPosition(lastItemPosition,
                visibleItem)) {
            viewListener.showLoadingNext();
            pagingHandler.nextPage();
            getInboxReputationUseCase.execute(
                    GetInboxReputationUseCase.getParam(pagingHandler.getPage(),
                            query,
                            timeFilter,
                            tab),
                    new GetNextPageInboxReputationSubscriber(viewListener));
        }
    }

    private boolean hasNextPage() {
        return pagingHandler.CheckNextPage();
    }

    private boolean isOnLastPosition(int itemPosition, int visibleItem) {
        return itemPosition == visibleItem;
    }

    public void refreshPage(int tab) {
        getFirstTimeInboxReputationUseCase.execute(
                GetFirstTimeInboxReputationUseCase.getFirstTimeParam(tab),
                new RefreshInboxReputationSubscriber(viewListener));
    }

    public void setHasNextPage(boolean hasNextPage) {
        pagingHandler.setHasNext(hasNextPage);
    }

    @Override
    public void detachView() {
        super.detachView();
        getFirstTimeInboxReputationUseCase.unsubscribe();
        getInboxReputationUseCase.unsubscribe();
    }
}
