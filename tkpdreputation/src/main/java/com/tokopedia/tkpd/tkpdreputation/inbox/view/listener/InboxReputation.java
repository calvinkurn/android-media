package com.tokopedia.tkpd.tkpdreputation.inbox.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.ReputationDataViewModel;

/**
 * @author by nisie on 8/10/17.
 */

public interface InboxReputation {
    interface View extends CustomerView {
        void showLoadingFull();

        void onErrorGetFirstTimeInboxReputation(String errorMessage);

        void onSuccessGetFirstTimeInboxReputation(InboxReputationViewModel inboxReputationViewModel);

        void finishLoadingFull();

        void onErrorGetNextPage(String errorMessage);

        void onSuccessGetNextPage(InboxReputationViewModel inboxReputationViewModel);

        void onErrorRefresh(String errorMessage);

        void onSuccessRefresh(InboxReputationViewModel inboxReputationViewModel);

        void showLoadingNext();

        void finishLoading();

        void onGoToDetail(String reputationId, String invoice, String createTime,
                          String revieweeName, String revieweeImage,
                          ReputationDataViewModel reputationDataViewModel, String textDeadline,
                          int adapterPosition, int role);

        void showRefreshing();

        void onSuccessGetFilteredInboxReputation();

        void onErrorGetFilteredInboxReputation(String errorMessage);

        void finishRefresh();

        void onShowEmpty();

    }

    interface Presenter extends CustomerPresenter<View> {

        void getFirstTimeInboxReputation(int tab);

        void getNextPage(int lastItemPosition, int visibleItem, String query,
                         String timeFilter, String statusFilter, int tab);

        void refreshItem(String id, int tab);

        void getFilteredInboxReputation(String query, String timeFilter, String statusFilter, int tab);
    }
}