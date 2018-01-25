package com.tokopedia.inbox.rescenter.inboxv2.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemResultViewModel;

import java.util.List;

/**
 * Created by yfsx on 24/01/18.
 */

public interface ResoInboxFragmentListener {

    interface View extends CustomerView {

        void onSuccessGetInbox(InboxItemResultViewModel result);

        void onErrorGetInbox(String err);

        void onSuccessLoadMoreInbox(InboxItemResultViewModel result);

        void onErrorLoadMoreInbox(String err);

        void showProgressBar();

        void dismissProgressBar();

        void onItemClicked(int resolutionId, String sellerName, String customerName);

    }

    interface Presenter extends CustomerPresenter<View> {

        void initPresenterData(Context context, boolean isSeller);

        void getInbox();

        void loadMoreInbox(String token);

        void quickFilterClicked(int orderValue);
    }
}
