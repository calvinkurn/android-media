package com.tokopedia.events.view.contractor;

import android.app.Activity;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by ashwanityagi on 03/11/17.
 */

public class EventsContract {
    public interface View extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

       // List<Event> getEvents();

       // void renderEvents(List<Event> events);

        RequestParams getParams();

    }

    public interface Presenter extends CustomerPresenter<View> {

        void initialize();

        void onDestroy();
    }
}
