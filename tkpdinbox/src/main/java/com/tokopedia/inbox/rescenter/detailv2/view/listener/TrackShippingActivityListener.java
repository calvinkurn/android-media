package com.tokopedia.inbox.rescenter.detailv2.view.listener;

import android.app.Fragment;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by milhamj on 24/11/17.
 */

public interface TrackShippingActivityListener {
    interface View extends CustomerView {
        void inflateFragment(Fragment fragment, String TAG);
    }

    interface Presenter extends CustomerPresenter<TrackShippingActivityListener.View> {
        void initFragment();
    }
}
