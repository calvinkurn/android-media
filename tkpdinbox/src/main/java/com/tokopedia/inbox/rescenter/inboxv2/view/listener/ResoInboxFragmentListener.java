package com.tokopedia.inbox.rescenter.inboxv2.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemResultViewModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemViewModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.ResoInboxFilterModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.ResoInboxSortModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.SortModel;


/**
 * Created by yfsx on 24/01/18.
 */

public interface ResoInboxFragmentListener {

    interface View extends CustomerView {

        void onSuccessGetInbox(InboxItemResultViewModel result);

        void onErrorGetInbox(String err);

        void onSuccessLoadMoreInbox(InboxItemResultViewModel result);

        void onErrorLoadMoreInbox(String err);

        void onSuccessGetInboxWithFilter(InboxItemResultViewModel result);

        void onErrorGetInboxWithFilter(String err);

        void onSuccessGetSingleInboxItem(InboxItemViewModel model);

        void onErrorGetSingleInboxItem(String err);

        void showProgressBar();

        void dismissProgressBar();

        void onItemClicked(int resolutionId, String sellerName, String customerName);

        void onSortItemClicked(SortModel sortModel);

    }

    interface Presenter extends CustomerPresenter<View> {

        void initPresenterData(Context context, boolean isSeller);

        void getInbox();

        void getInboxWithParams(ResoInboxSortModel inboxSortModel, ResoInboxFilterModel inboxFilterModel);

        void getSingleItemInbox(int inboxId);

        void loadMoreInbox(String token);

        void resetFilterClicked();

        void quickFilterClicked(int orderValue);
    }
}
