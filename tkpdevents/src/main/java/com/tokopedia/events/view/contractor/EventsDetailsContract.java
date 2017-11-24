package com.tokopedia.events.view.contractor;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by ashwanityagi on 23/11/17.
 */

public class EventsDetailsContract {
    public interface View extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        RequestParams getParams();

    }

    public interface Presenter extends CustomerPresenter<View> {

        void initialize();

        void onDestroy();
    }

}
