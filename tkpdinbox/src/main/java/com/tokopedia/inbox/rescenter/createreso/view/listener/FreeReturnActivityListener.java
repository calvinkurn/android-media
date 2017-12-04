package com.tokopedia.inbox.rescenter.createreso.view.listener;

import android.app.Fragment;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by yoasfs on 24/08/17.
 */

public interface FreeReturnActivityListener {

    interface View extends CustomerView {
        void inflateFragment(Fragment fragment, String TAG);
    }

    interface Presenter extends CustomerPresenter<View> {
        void initFragment(String url);
    }
}
