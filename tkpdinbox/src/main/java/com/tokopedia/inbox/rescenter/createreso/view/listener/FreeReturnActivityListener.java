package com.tokopedia.inbox.rescenter.createreso.view.listener;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.base.view.listener.CustomerView;

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
