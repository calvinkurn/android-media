package com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter;


import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox.GetFirstTimeInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox.GetInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputation;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.GetFilteredInboxReputationSubscriber;
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
    public void getNextPage(int lastItemPosition, int visibleItem,
                            String query, String timeFilter,
                            String scoreFilter, int tab) {
        if (hasNextPage() && isOnLastPosition(lastItemPosition,
                visibleItem)) {
            viewListener.showLoadingNext();
            pagingHandler.nextPage();
            getInboxReputationUseCase.execute(
                    GetInboxReputationUseCase.getParam(pagingHandler.getPage(),
                            query,
                            timeFilter,
                            scoreFilter,
                            tab),
                    new GetNextPageInboxReputationSubscriber(viewListener));
        }
    }

    @Override
    public void getFilteredInboxReputation(String query, String timeFilter, String statusFilter, int tab) {
        viewListener.showRefreshing();
        pagingHandler.resetPage();
        getInboxReputationUseCase.execute(
                GetInboxReputationUseCase.getParam(pagingHandler.getPage(),
                        query,
                        timeFilter,
                        statusFilter,
                        tab),
                new GetFilteredInboxReputationSubscriber(viewListener));
    }

    private boolean hasNextPage() {
        return pagingHandler.CheckNextPage();
    }

    private boolean isOnLastPosition(int itemPosition, int visibleItem) {
        return itemPosition == visibleItem;
    }

    public void refreshPage(String query, String timeFilter, String scoreFilter, int tab) {
        pagingHandler.resetPage();
        getInboxReputationUseCase.execute(
                GetInboxReputationUseCase.getParam(pagingHandler.getPage(), query, timeFilter,
                        scoreFilter, tab),
                new RefreshInboxReputationSubscriber(viewListener, isUsingFilter(query,
                        timeFilter,scoreFilter)));
    }

    private boolean isUsingFilter(String query, String timeFilter, String scoreFilter) {
        return !query.isEmpty() || !timeFilter.isEmpty() || !scoreFilter.isEmpty();
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
