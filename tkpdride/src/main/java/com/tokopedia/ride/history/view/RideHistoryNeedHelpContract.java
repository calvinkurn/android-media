package com.tokopedia.ride.history.view;

import android.content.Context;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by alvarisi on 4/20/17.
 */

public interface RideHistoryNeedHelpContract {

    interface View extends CustomerView {

        Context getActivity();

        void renderUi();

        void showToast(int resourceId);
    }

    interface Presenter extends CustomerPresenter<View> {
        void initialize();

        void copyToClipboard(Context context, String text);
    }
}
