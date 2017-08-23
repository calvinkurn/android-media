package com.tokopedia.tkpd.tkpdreputation.inbox.view.listener;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailHeaderViewModel;

import java.util.List;

/**
 * @author by nisie on 8/19/17.
 */

public interface InboxReputationDetail {

    interface View extends CustomerView {

        void showLoading();

        void onErrorGetInboxDetail(String errorMessage);

        void onSuccessGetInboxDetail(InboxReputationDetailHeaderViewModel model, List<Visitable> list);
    }

    interface Presenter extends CustomerPresenter<View> {

        void getInboxDetail(String id, int anInt);

        void getNextPage(int lastItemPosition, int visibleItem);
    }
}
