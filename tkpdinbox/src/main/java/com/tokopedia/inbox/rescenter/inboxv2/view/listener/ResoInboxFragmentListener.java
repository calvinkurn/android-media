package com.tokopedia.inbox.rescenter.inboxv2.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemResultViewModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.ResoInboxFilterModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.ResoInboxSortModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.SingleItemInboxResultViewModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.SortModel;


/**
 * Created by yfsx on 24/01/18.
 */

public interface ResoInboxFragmentListener {

    interface View extends CustomerView {

        void getInboxWithParams(ResoInboxFilterModel inboxFilterModel);

        void onSuccessGetInbox(InboxItemResultViewModel result);

        void onErrorGetInbox(String err);

        void onEmptyGetInbox(InboxItemResultViewModel result);

        void onSuccessLoadMoreInbox(InboxItemResultViewModel result);

        void onErrorLoadMoreInbox(String err);

        void onEmptyGetInboxWithFilter(InboxItemResultViewModel result);

        void onSuccessGetInboxWithFilter(InboxItemResultViewModel result);

        void onErrorGetInboxWithFilter(String err);

        void onSuccessGetSingleInboxItem(SingleItemInboxResultViewModel model);

        void onErrorGetSingleInboxItem(String err);

        void removeLoadingItem();

        void showProgressBar();

        void dismissProgressBar();

        void showSwipeToRefresh();

        void dismissSwipeToRefresh();

        void onItemClicked(int resolutionId, String sellerName, String customerName);

        void onSortItemClicked(SortModel sortModel);

        void onResetFilterButtonClicked();

        ResoInboxFilterModel getInboxFilterModel();

        ResoInboxSortModel getInboxSortModel();

    }

    interface Presenter extends CustomerPresenter<View> {

        void initPresenterData(Context context, boolean isSeller);

        void getInbox();

        void getInboxResetFilter();

        void getInboxWithParams(ResoInboxSortModel inboxSortModel, ResoInboxFilterModel inboxFilterModel);

        void getSingleItemInbox(int inboxId);

        void loadMoreInbox(String token);

        void resetFilterClicked();

        void quickFilterClicked(int orderValue);
    }
}
