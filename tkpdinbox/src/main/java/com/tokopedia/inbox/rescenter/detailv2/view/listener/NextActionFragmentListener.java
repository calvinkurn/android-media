package com.tokopedia.inbox.rescenter.detailv2.view.listener;


import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;

/**
 * Created by yoasfs on 10/6/17.
 */

public interface NextActionFragmentListener {

    interface View extends CustomerView {

        void showProgressBar();

        void dismissProgressBar();

        void populateMainView(NextActionDomain nextActionDomain);
    }

    interface Presenter extends CustomerPresenter<View> {

        void initPresenter(NextActionDomain nextActionDomain);

        void initPresenter(String resolutionId);
    }
}