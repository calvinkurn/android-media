package com.tokopedia.inbox.rescenter.detailchat.view.listener;

import android.support.v4.app.Fragment;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by yoasfs on 10/6/17.
 */

public interface DetailResChatFragmentListener {

    interface View extends CustomerView {

        void populateView();
    }

    interface Presenter extends CustomerPresenter<DetailResChatFragmentListener.View> {

        void initView();
    }
}