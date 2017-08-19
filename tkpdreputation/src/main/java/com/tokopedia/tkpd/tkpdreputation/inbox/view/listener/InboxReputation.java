package com.tokopedia.tkpd.tkpdreputation.inbox.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationViewModel;

/**
 * @author by nisie on 8/10/17.
 */

public interface InboxReputation {
    interface View extends CustomerView {
        void showLoadingFull();

        void onErrorGetFirstTimeInboxReputation(String errorMessage);

        void onSuccessGetFirstTimeInboxReputation(InboxReputationViewModel inboxReputationViewModel);

        void onErrorGetNextPage(String errorMessage);

        void onSuccessGetNextPage(InboxReputationViewModel inboxReputationViewModel);

        void onErrorRefresh(String errorMessage);

        void onSuccessRefresh(InboxReputationViewModel inboxReputationViewModel);

        void showLoadingNext();

        void onGoToDetail(String reputationId, int adapterPosition);
    }

    interface Presenter extends CustomerPresenter<View> {

        void getFirstTimeInboxReputation(int tab);

        void getNextPage(int lastItemPosition, int visibleItem, String query, int timeFilter,
              int tab);

        void refreshItem(String id, int tab);
    }
}