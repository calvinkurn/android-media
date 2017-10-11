package com.tokopedia.inbox.rescenter.detailv2.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;

/**
 * Created by yoasfs on 10/6/17.
 */

public interface DetailResChatFragmentListener {

    interface View extends CustomerView {

        void populateView();

        void successGetConversation(DetailResChatDomain detailResChatDomain);

        void errorGetConversation(String error);
    }

    interface Presenter extends CustomerPresenter<DetailResChatFragmentListener.View> {

        void initView(String resolutionId);
    }
}