package com.tokopedia.inbox.rescenter.detailchat.view.listener;

import android.support.v4.app.Fragment;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by yoasfs on 10/6/17.
 */

public interface DetailResChatActivityListener {

    interface View extends CustomerView {
        void inflateFragment(Fragment fragment, String TAG);
    }

    interface Presenter extends CustomerPresenter<View> {
        void initFragment();
    }
}