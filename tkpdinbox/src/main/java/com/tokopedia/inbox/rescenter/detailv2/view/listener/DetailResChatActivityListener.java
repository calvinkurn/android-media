package com.tokopedia.inbox.rescenter.detailv2.view.listener;


import android.app.Fragment;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.base.view.listener.CustomerView;

/**
 * Created by yoasfs on 10/6/17.
 */

public interface DetailResChatActivityListener {

    interface View extends CustomerView {
        void inflateFragment(Fragment fragment, String TAG, boolean isReload);
    }

    interface Presenter extends CustomerPresenter<View> {
        void initFragment(boolean isSeller, String resolutionId, boolean isReload);
    }
}